package com.welfare.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.welfare.common.constants.*;
import com.welfare.common.enums.ShoppingActionTypeEnum;
import com.welfare.common.exception.BizAssert;
import com.welfare.common.exception.BizException;
import com.welfare.common.exception.ExceptionCode;
import com.welfare.common.util.DistributedLockUtil;
import com.welfare.common.util.MerchantUserHolder;
import com.welfare.persist.dao.*;
import com.welfare.persist.entity.*;
import com.welfare.service.*;
import com.welfare.service.dto.AccountReq;
import com.welfare.service.dto.BatchSequence;
import com.welfare.service.dto.DepartmentTree;
import com.welfare.service.dto.Deposit;
import com.welfare.service.dto.nhc.*;
import com.welfare.service.enums.ApprovalStatus;
import com.welfare.service.sync.event.AccountEvt;
import com.welfare.service.utils.AccountUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/4/13 10:29 上午
 */
@Service
@Slf4j
public class NhcServiceImpl implements NhcService {

    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountDao accountDao;
    @Autowired
    private SequenceService sequenceService;
    @Autowired
    private MerchantService merchantService;
    @Autowired
    private AccountAmountTypeDao accountAmountTypeDao;
    @Autowired
    private AccountTypeDao accountTypeDao;
    @Autowired
    private SubAccountDao subAccountDao;
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private AccountAmountTypeGroupService accountAmountTypeGroupService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private AccountChangeEventRecordService accountChangeEventRecordService;
    @Autowired
    private AccountAmountTypeGroupDao accountAmountTypeGroupDao;
    @Autowired
    private AccountDepositApplyDao accountDepositApplyDao;
    @Autowired
    private AccountDepositApplyService accountDepositApplyService;
    @Autowired
    private AccountAmountTypeService accountAmountTypeService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveOrUpdateUser(NhcUserReq userReq) {
        Account account;
        Merchant merchant = merchantService.getMerchantByMerCode(userReq.getMerCode());
        BizAssert.notNull(merchant,
                ExceptionCode.ILLEGALITY_ARGURMENTS, "商户不存在");
        boolean joinGroup = false;
        if (StringUtils.isNoneBlank(userReq.getAccountCode())) {
            // 修改
            account = accountService.getByAccountCode(Long.parseLong(userReq.getAccountCode()));
            BizAssert.notNull(account, ExceptionCode.ILLEGALITY_ARGURMENTS, String.format("员工不存在[%s]", userReq.getAccountCode()));
            BizAssert.isTrue(userReq.getMerCode().equals(account.getMerCode()), ExceptionCode.ILLEGALITY_ARGURMENTS, "无权限操作！");
            account.setAccountName(userReq.getUserName());
            account.setPhone(userReq.getPhone());
        } else {
            // 新增
            account = assemblyUser(NhcAccountReq.of(userReq), merchant);
            if (StringUtils.isNoneBlank(userReq.getFamilyUserCode())) {
                joinGroup = true;
            }
        }
        BizAssert.isTrue(accountDao.saveOrUpdate(account));
        // 加入家庭
        if (joinGroup) {
            boolean success = accountAmountTypeGroupService.addByAccountCodeAndMerAccountTypeCode(
                    account.getMerCode(),
                    Long.parseLong(userReq.getAccountCode()),
                    Long.parseLong(userReq.getFamilyUserCode()),
                    WelfareConstant.MerAccountTypeCode.MALL_POINT.code());
            BizAssert.isTrue(success);
        }
        // 同步商户
        applicationContext.publishEvent(AccountEvt.builder()
                .typeEnum(StringUtils.isNoneBlank(userReq.getAccountCode()) ? ShoppingActionTypeEnum.UPDATE : ShoppingActionTypeEnum.ADD)
                .accountList(Collections.singletonList(account)).build());
        return String.valueOf(account.getAccountCode());
    }

    private Account assemblyUser(NhcAccountReq accountReq, Merchant merchant) {
        Account account = new Account();
        Long accountCode = sequenceService.nextNo(WelfareConstant.SequenceType.ACCOUNT_CODE.code());
        account.setCreateUser(MerchantUserHolder.getMerchantUser().getUsername());
        account.setAccountCode(accountCode);
        account.setAccountName(accountReq.getAccountName());
        account.setMerCode(accountReq.getMerCode());
        account.setPhone(accountReq.getPhone());
        account.setAccountStatus(AccountStatus.ENABLE.getCode());
        account.setOfflineLock(WelfareConstant.AccountOfflineFlag.ENABLE.code());
        account.setBinding(AccountBindStatus.NO_BIND.getCode());
        List<AccountType> accountTypes = accountTypeDao.getByMerCode(accountReq.getMerCode());
        BizAssert.notEmpty(accountTypes, ExceptionCode.ILLEGALITY_ARGURMENTS, "商户没有员工类型");
        account.setAccountTypeCode(accountTypes.get(0).getTypeCode());
        if (StringUtils.isBlank(accountReq.getDepartmentCode())) {
            List<DepartmentTree> departmentTrees = departmentService.tree(accountReq.getMerCode());
            account.setDepartment(departmentTrees.get(0).getDepartmentCode());
        } else {
            account.setDepartment(accountReq.getDepartmentCode());
        }
        // 创建甜橙卡子账户
        SubAccount subAccount = new SubAccount();
        subAccount.setSubAccountType(WelfareConstant.PaymentChannel.WELFARE.code());
        subAccount.setAccountCode(account.getAccountCode());
        BizAssert.isTrue(subAccountDao.save(subAccount));
        // 创建积分福利账户
        AccountAmountType accountAmountType1 = new AccountAmountType();
        accountAmountType1.setAccountCode(accountCode);
        accountAmountType1.setMerAccountTypeCode(WelfareConstant.MerAccountTypeCode.MALL_POINT.code());
        accountAmountType1.setJoinedGroup(false);
        AccountAmountType accountAmountType2 = new AccountAmountType();
        accountAmountType2.setAccountCode(accountCode);
        accountAmountType2.setMerAccountTypeCode(WelfareConstant.MerAccountTypeCode.WHOLESALE.code());
        accountAmountTypeDao.saveBatch(Lists.newArrayList(accountAmountType1,accountAmountType2));

        AccountChangeEventRecord accountChangeEventRecord = AccountUtils
                .assemableChangeEvent(AccountChangeType.ACCOUNT_NEW, account.getAccountCode(),
                        account.getCreateUser());
        accountChangeEventRecordService.save(accountChangeEventRecord);
        account.setChangeEventId(accountChangeEventRecord.getId());
        return account;
    }

    @Override
    public NhcUserInfoDTO getUserInfo(NhcQueryUserReq queryUserReq) {
        Account account;
        if (StringUtils.isNoneBlank(queryUserReq.getAccountCode())) {
            account = accountService.getByAccountCode(Long.parseLong(queryUserReq.getAccountCode()));
        } else if (StringUtils.isNoneBlank(queryUserReq.getPhone())) {
            account = accountDao.getByMerCodeAndPhone(MerchantUserHolder.getMerchantUser().getMerchantCode(), queryUserReq.getPhone());
        } else {
            throw new BizException("参数为空");
        }
        BizAssert.notNull(account, ExceptionCode.ILLEGALITY_ARGURMENTS, "员工不存在");
        Merchant merchant = merchantService.getMerchantByMerCode(account.getMerCode());
        BizAssert.notNull(merchant, ExceptionCode.ILLEGALITY_ARGURMENTS, "商户不存在");
        AccountAmountType accountAmountType = accountAmountTypeDao.queryByAccountCodeAndAmountType(account.getAccountCode(),
                WelfareConstant.MerAccountTypeCode.MALL_POINT.code());
        return NhcUserInfoDTO.of(account, accountAmountType, merchant);
    }

    @Override
    public void rechargeMallPoint(NhcUserPointRechargeReq req) {
        String lockKey = RedisKeyConstant.buidKey(RedisKeyConstant.ACCOUNT_DEPOSIT_APPLY__ID, req.getRequestId());
        RLock lock = DistributedLockUtil.lockFairly(lockKey);
        try {
            List<AccountDepositApply> applyList = accountDepositApplyService.listByRequestId(req.getRequestId());
            BizAssert.isTrue(CollectionUtils.isEmpty(applyList), ExceptionCode.ILLEGALITY_ARGURMENTS, "不能重复充值");
            List<Long> accountCodes = req.getAccountCodes().stream().map(Long::valueOf).collect(Collectors.toList());
            List<AccountAmountType> accountAmountTypes = accountAmountTypeDao.listByAccountCodes(accountCodes, WelfareConstant.MerAccountTypeCode.MALL_POINT.code());
            BizAssert.isTrue(CollectionUtils.isNotEmpty(accountAmountTypes) && accountAmountTypes.size() == req.getAccountCodes().size(),
                    ExceptionCode.ILLEGALITY_ARGURMENTS,
                    "员工福利账户不存在");
            // 筛选出没有加入组的员工
            List<AccountAmountType> noGroupAccountAmountTypes = accountAmountTypes.stream().filter(accountAmountType ->
                    Objects.isNull(accountAmountType.getAccountAmountTypeGroupId()) && (Objects.isNull(accountAmountType.getJoinedGroup()) || !accountAmountType.getJoinedGroup()))
                    .collect(Collectors.toList());
            // 筛选出已加入组的员工
            Map<Long, List<AccountAmountType>> groupAccountAmountTypeMap = accountAmountTypes.stream().filter(accountAmountType ->
                    Objects.nonNull(accountAmountType.getAccountAmountTypeGroupId()) && Objects.nonNull(accountAmountType.getJoinedGroup()) && accountAmountType.getJoinedGroup())
                    .collect(Collectors.groupingBy(AccountAmountType::getAccountAmountTypeGroupId));
            BatchSequence sequences = sequenceService.batchGenerate(WelfareConstant.SequenceType.DEPOSIT.code(), accountAmountTypes.size());
            // 员工组充值
            List<Sequence> groupSequences = sequences.getSequences().subList(0, groupAccountAmountTypeMap.size());
            accountAmountTypeGroupService.batchUpdateGroupAmount(GroupDeposit.of(req.getAmount(), groupSequences, groupAccountAmountTypeMap));
            // 员工充值
            List<Sequence> noGroupSequences = sequences.getSequences().subList(groupAccountAmountTypeMap.size(), noGroupAccountAmountTypes.size());
            accountAmountTypeService.batchUpdateAccountAmountType(Deposit.of(req.getAmount(), noGroupSequences, noGroupAccountAmountTypes));
        } finally {
            DistributedLockUtil.unlock(lock);
        }
    }

    public static void main(String[] args) {
        List<Integer> list = Lists.newArrayList(1,2,3,4,5);
        System.out.println(list.subList(0,1));
    //    System.out.println(list.subList(1,));
    }
    @Override
    public Page<NhcAccountBillDetailDTO> getUserBillPage(NhcUserPageReq userPageReq) {
        return null;
    }

    @Override
    public Boolean leaveFamily(String merCode, String userCode) {
        String merAccountTypeCode = WelfareConstant.MerAccountTypeCode.MALL_POINT.code();
        return accountAmountTypeGroupService.removeByAccountCode(merCode, Long.parseLong(userCode), merAccountTypeCode);
    }

    @Override
    public NhcFamilyMemberDTO getFamilyInfo(String userCode) {
        Account account = accountService.getByAccountCode(Long.parseLong(userCode));
        BizAssert.notNull(account, ExceptionCode.ILLEGALITY_ARGURMENTS, "员工不存在");
        Merchant merchant = merchantService.getMerchantByMerCode(account.getMerCode());
        BizAssert.notNull(merchant, ExceptionCode.ILLEGALITY_ARGURMENTS, "商户不存在");
        AccountAmountType accountAmountType = accountAmountTypeDao.queryByAccountCodeAndAmountType(account.getAccountCode(),
                WelfareConstant.MerAccountTypeCode.MALL_POINT.code());
        BizAssert.notNull(accountAmountType, ExceptionCode.ILLEGALITY_ARGURMENTS, "积分福利账户为空");
        NhcFamilyMemberDTO dto = new NhcFamilyMemberDTO();
        if (Objects.nonNull(accountAmountType.getAccountAmountTypeGroupId())
                && accountAmountType.getJoinedGroup() != null && accountAmountType.getJoinedGroup()) {
            AccountAmountTypeGroup group = accountAmountTypeGroupDao.getById(accountAmountType.getAccountAmountTypeGroupId());
            BizAssert.notNull(group, ExceptionCode.ILLEGALITY_ARGURMENTS, "员工福利账号组为空");
            dto.setFamilyCode(group.getGroupCode());
            dto.setFamilyBalance(String.valueOf(group.getBalance()));
            QueryWrapper<AccountAmountType> accountAmountTypeQueryWrapper = new QueryWrapper<>();
            accountAmountTypeQueryWrapper
                    .eq(AccountAmountType.ACCOUNT_AMOUNT_TYPE_GROUP_ID, group.getId())
                    .eq(AccountAmountType.JOINED_GROUP, true);
            List<AccountAmountType> accountAmountTypes = accountAmountTypeDao.list(accountAmountTypeQueryWrapper);
            Map<Long, AccountAmountType> accountAmountTypeMap = accountAmountTypes.stream().collect(Collectors.toMap(AccountAmountType::getAccountCode, type->type));
            List<Long> accountCodes = accountAmountTypes.stream().map(AccountAmountType::getAccountCode).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(accountCodes)) {
                QueryWrapper<Account> accountQueryWrapper = new QueryWrapper<>();
                accountQueryWrapper.in(Account.ACCOUNT_CODE, accountCodes);
                Map<Long, Account> accountMap = accountDao.list(accountQueryWrapper).stream().collect(Collectors.toMap(Account::getAccountCode, a->a));
                dto.setMembers(NhcUserInfoDTO.of(accountMap, accountAmountTypeMap, merchant));
            }
        }
        return dto;
    }
}
