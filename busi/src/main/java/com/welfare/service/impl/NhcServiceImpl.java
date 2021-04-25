package com.welfare.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.welfare.common.constants.*;
import com.welfare.common.enums.ShoppingActionTypeEnum;
import com.welfare.common.exception.BizAssert;
import com.welfare.common.exception.BizException;
import com.welfare.common.exception.ExceptionCode;
import com.welfare.common.util.AccountUtil;
import com.welfare.common.util.DistributedLockUtil;
import com.welfare.common.util.MerchantUserHolder;
import com.welfare.persist.dao.*;
import com.welfare.persist.dto.AccountBillDetailMapperDTO;
import com.welfare.persist.entity.*;
import com.welfare.persist.mapper.AccountCustomizeMapper;
import com.welfare.service.*;
import com.welfare.service.dto.AccountBillDetailDTO;
import com.welfare.service.dto.BatchSequence;
import com.welfare.service.dto.DepartmentTree;
import com.welfare.service.dto.Deposit;
import com.welfare.service.dto.nhc.*;
import com.welfare.service.sync.event.AccountEvt;
import com.welfare.service.utils.AccountUtils;
import com.welfare.service.utils.PageUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
    private TempAccountDepositApplyDao tempAccountDepositApplyDao;
    @Autowired
    private AccountAmountTypeService accountAmountTypeService;
    @Autowired
    private AccountCustomizeMapper accountCustomizeMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveOrUpdateUser(NhcUserReq userReq) {
        Account account;
        Merchant merchant = merchantService.getMerchantByMerCode(userReq.getMerCode());
        BizAssert.notNull(merchant,
                ExceptionCode.ILLEGALITY_ARGURMENTS, "商户不存在");
        if (StringUtils.isNoneBlank(userReq.getAccountCode())) {
            // 修改
            account = accountService.getByAccountCode(Long.parseLong(userReq.getAccountCode()));
            BizAssert.notNull(account, ExceptionCode.ILLEGALITY_ARGURMENTS, String.format("用户不存在[%s]", userReq.getAccountCode()));
            BizAssert.isTrue(userReq.getMerCode().equals(account.getMerCode()), ExceptionCode.ILLEGALITY_ARGURMENTS, "无权限操作！");
            account.setAccountName(userReq.getUserName());
            if (Objects.nonNull(userReq.getAccountStatus())) {
                account.setAccountStatus(AccountStatus.getByCode(userReq.getAccountStatus()).getCode());
            }
            if (StringUtils.isNoneBlank(userReq.getPhone())) {
                BizAssert.isTrue(userReq.getPhone().length() == 11 && AccountUtil.isNumeric(userReq.getPhone()),
                        ExceptionCode.ILLEGALITY_ARGURMENTS, "手机号不合法");
                account.setPhone(userReq.getPhone());
            } else {
                account.setPhone(DEFAULT_PHONE_PREFIX + sequenceService.nextNo(WelfareConstant.SequenceType.DEFAULT_PHONE.code()));
            }
        } else {
            // 新增
            account = assemblyUser(userReq, merchant);
        }
        BizAssert.isTrue(accountDao.saveOrUpdate(account));
        // 加入家庭
        if (StringUtils.isNoneBlank(userReq.getFamilyUserCode())) {
            boolean success = accountAmountTypeGroupService.addByAccountCodeAndMerAccountTypeCode(
                    account.getMerCode(),
                    account.getAccountCode(),
                    Long.parseLong(userReq.getFamilyUserCode()),
                    WelfareConstant.MerAccountTypeCode.MALL_POINT.code());
            BizAssert.isTrue(success);
        }
        if (StringUtils.isNoneBlank(account.getPhone()) && account.getPhone().startsWith(NhcService.DEFAULT_PHONE_PREFIX)) {
            account.setPhone(null);
        }
        // 同步员工
        applicationContext.publishEvent(AccountEvt.builder()
                .typeEnum(StringUtils.isNoneBlank(userReq.getAccountCode()) ? ShoppingActionTypeEnum.UPDATE : ShoppingActionTypeEnum.ADD)
                .accountList(Collections.singletonList(account)).build());
        return String.valueOf(account.getAccountCode());
    }

    private Account assemblyUser(NhcUserReq userReq, Merchant merchant) {
        Account account = new Account();
        Long accountCode = sequenceService.nextNo(WelfareConstant.SequenceType.ACCOUNT_CODE.code());
        account.setCreateUser(MerchantUserHolder.getMerchantUser().getUsername());
        account.setAccountCode(accountCode);
        account.setAccountName(userReq.getUserName());
        account.setMerCode(userReq.getMerCode());
        if (StringUtils.isBlank(userReq.getPhone())) {
            account.setPhone(DEFAULT_PHONE_PREFIX + sequenceService.nextNo(WelfareConstant.SequenceType.DEFAULT_PHONE.code()));
        } else {
            BizAssert.isTrue(userReq.getPhone().length() == 11 && AccountUtil.isNumeric(userReq.getPhone()), ExceptionCode.ILLEGALITY_ARGURMENTS, "手机号不合法");
            Account oldAccount= accountService.findByPhoneAndMerCode(userReq.getPhone(), userReq.getMerCode());
            BizAssert.isTrue(Objects.isNull(oldAccount), ExceptionCode.ACCOUNT_ALREADY_EXIST, "员工已存在");
            account.setPhone(userReq.getPhone());
        }
        if (userReq.getAccountStatus() != null) {
            account.setAccountStatus(AccountStatus.getByCode(userReq.getAccountStatus()).getCode());
        } else {
            account.setAccountStatus(AccountStatus.ENABLE.getCode());
        }
        account.setOfflineLock(WelfareConstant.AccountOfflineFlag.ENABLE.code());
        account.setBinding(AccountBindStatus.NO_BIND.getCode());
        account.setAccountTypeCode(WelfareConstant.AccountType.PATIENT.code());
        List<DepartmentTree> departmentTrees = departmentService.tree(userReq.getMerCode());
        BizAssert.notEmpty(departmentTrees.get(0).getChildren(),ExceptionCode.ILLEGALITY_ARGURMENTS, "商户部门不存在");
        DepartmentTree department = (DepartmentTree)departmentTrees.get(0).getChildren().get(0);
        account.setDepartment(department.getDepartmentCode());
        // 创建甜橙卡子账户
        SubAccount subAccount = new SubAccount();
        subAccount.setSubAccountType(WelfareConstant.PaymentChannel.WELFARE.code());
        subAccount.setAccountCode(account.getAccountCode());
        BizAssert.isTrue(subAccountDao.save(subAccount));
        // 创建积分福利账户
        AccountAmountType accountAmountType1 = new AccountAmountType();
        accountAmountType1.setAccountCode(accountCode);
        accountAmountType1.setMerAccountTypeCode(WelfareConstant.MerAccountTypeCode.MALL_POINT.code());
        accountAmountType1.setAccountBalance(BigDecimal.ZERO);
        accountAmountType1.setJoinedGroup(false);
        accountAmountTypeDao.save(accountAmountType1);
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
        BizAssert.notNull(account, ExceptionCode.ILLEGALITY_ARGURMENTS, "用户不存在");
        Merchant merchant = merchantService.getMerchantByMerCode(account.getMerCode());
        BizAssert.notNull(merchant, ExceptionCode.ILLEGALITY_ARGURMENTS, "商户不存在");
        AccountAmountType accountAmountType = accountAmountTypeDao.queryByAccountCodeAndAmountType(account.getAccountCode(),
                WelfareConstant.MerAccountTypeCode.MALL_POINT.code());
        AccountAmountTypeGroup group = null;
        if (Objects.nonNull(accountAmountType.getAccountAmountTypeGroupId())
                && accountAmountType.getJoinedGroup() != null && accountAmountType.getJoinedGroup()) {
            group = accountAmountTypeGroupDao.getById(accountAmountType.getAccountAmountTypeGroupId());
        }

        return NhcUserInfoDTO.of(group, account, accountAmountType, merchant);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void rechargeMallPoint(NhcUserPointRechargeReq req) {
        String lockKey = RedisKeyConstant.buidKey(RedisKeyConstant.ACCOUNT_DEPOSIT_APPLY__ID, req.getRequestId());
        RLock lock = DistributedLockUtil.lockFairly(lockKey);
        try {
            List<TempAccountDepositApply> applyList = tempAccountDepositApplyDao.listByRequestId(req.getRequestId());
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
            List<AccountAmountType> groupAccountAmountTypes = accountAmountTypes.stream().filter(accountAmountType ->
                    Objects.nonNull(accountAmountType.getAccountAmountTypeGroupId()) && Objects.nonNull(accountAmountType.getJoinedGroup()) && accountAmountType.getJoinedGroup())
                    .collect(Collectors.toList());
            Map<Long, List<AccountAmountType>> groupAccountAmountTypeMap = groupAccountAmountTypes.stream() .collect(Collectors.groupingBy(AccountAmountType::getAccountAmountTypeGroupId));
            BatchSequence sequences = sequenceService.batchGenerate(WelfareConstant.SequenceType.DEPOSIT.code(), accountAmountTypes.size());
            // 员工组充值
            if (CollectionUtils.isNotEmpty(groupAccountAmountTypes)) {
                List<Sequence> groupSequences = sequences.getSequences().subList(0, groupAccountAmountTypes.size());
                accountAmountTypeGroupService.batchUpdateGroupAmount(GroupDeposit.of(req.getAmount(), groupSequences, groupAccountAmountTypeMap));
            }
            // 员工充值
            if (CollectionUtils.isNotEmpty(noGroupAccountAmountTypes)) {
                List<Sequence> noGroupSequences = sequences.getSequences().subList(groupAccountAmountTypes.size(), sequences.getSequences().size());
                accountAmountTypeService.batchUpdateAccountAmountType(Deposit.of(req.getAmount(), noGroupSequences, noGroupAccountAmountTypes));
            }
            TempAccountDepositApply accountDepositApply = assemblyUserTempAccountDepositApply(req.getRequestId());
            tempAccountDepositApplyDao.save(accountDepositApply);
        } finally {
            DistributedLockUtil.unlock(lock);
        }
    }

    private TempAccountDepositApply assemblyUserTempAccountDepositApply(String requestId) {
        TempAccountDepositApply depositApply = new TempAccountDepositApply();
        depositApply.setAccountCode(9999999999L);
        depositApply.setPhone("00000000000");
        depositApply.setFileId(requestId);
        depositApply.setRequestId(requestId);
        return depositApply;
    }

    @Override
    public Page<NhcAccountBillDetailDTO> getUserBillPage(NhcUserPageReq userPageReq) {
        Page<AccountBillDetailMapperDTO> page = new Page<>(userPageReq.getCurrent(), userPageReq.getSize());
        accountCustomizeMapper.queryAccountBillDetail(page, userPageReq.getAccountCode(), null, null);
        List<NhcAccountBillDetailDTO> list = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(page.getRecords())) {
            page.getRecords().forEach(billDetail -> {
                NhcAccountBillDetailDTO dto = new NhcAccountBillDetailDTO();
                dto.setTransNo(billDetail.getTransNo());
                dto.setTransTime(billDetail.getCreateTime());
                dto.setTransTypeName(billDetail.getTransTypeString());
                dto.setMerAccountTypeName(billDetail.getAccountTypeName());
                if (WelfareConstant.TransType.DEPOSIT_INCR.code().equals(billDetail.getTransType())) {
                    dto.setEvent("积分充值");
                    dto.setTransAmount("+" + billDetail.getTransAmount().abs());
                } else if (WelfareConstant.TransType.DEPOSIT_BACK.code().equals(billDetail.getTransType())) {
                    dto.setEvent("积分回冲");
                    dto.setTransAmount("-" + billDetail.getTransAmount().abs());
                } else if (WelfareConstant.TransType.CONSUME.code().equals(billDetail.getTransType())) {
                    dto.setEvent("积分商品兑换");
                    dto.setTransAmount("-" + billDetail.getTransAmount().abs());
                } else if (WelfareConstant.TransType.REFUND.code().equals(billDetail.getTransType())) {
                    dto.setEvent("积分商品退款");
                    dto.setTransAmount("+" + billDetail.getTransAmount().abs());
                }
                list.add(dto);
            });
        }
        return PageUtils.toPage(page, list);
    }

    @Override
    public Boolean leaveFamily(String merCode, String userCode) {
        String merAccountTypeCode = WelfareConstant.MerAccountTypeCode.MALL_POINT.code();
        return accountAmountTypeGroupService.removeByAccountCode(merCode, Long.parseLong(userCode), merAccountTypeCode);
    }

    @Override
    public NhcFamilyMemberDTO getFamilyInfo(String userCode) {
        Account account = accountService.getByAccountCode(Long.parseLong(userCode));
        BizAssert.notNull(account, ExceptionCode.ILLEGALITY_ARGURMENTS, "用户不存在");
        Merchant merchant = merchantService.getMerchantByMerCode(account.getMerCode());
        BizAssert.notNull(merchant, ExceptionCode.ILLEGALITY_ARGURMENTS, "商户不存在");
        AccountAmountType accountAmountType = accountAmountTypeDao.queryByAccountCodeAndAmountType(account.getAccountCode(),
                WelfareConstant.MerAccountTypeCode.MALL_POINT.code());
        BizAssert.notNull(accountAmountType, ExceptionCode.ILLEGALITY_ARGURMENTS, "积分福利账户为空");
        NhcFamilyMemberDTO dto = new NhcFamilyMemberDTO();
        dto.setMembers(new ArrayList<>());
        dto.setFamilyCode("0001");
        if (Objects.nonNull(accountAmountType.getAccountAmountTypeGroupId())
                && accountAmountType.getJoinedGroup() != null && accountAmountType.getJoinedGroup()) {
            AccountAmountTypeGroup group = accountAmountTypeGroupDao.getById(accountAmountType.getAccountAmountTypeGroupId());
            BizAssert.notNull(group, ExceptionCode.ILLEGALITY_ARGURMENTS, "员工福利账号组为空");
            dto.setFamilyCode(group.getGroupCode());
            dto.setFamilyBalance(String.valueOf(group.getBalance()));
            QueryWrapper<AccountAmountType> accountAmountTypeQueryWrapper = new QueryWrapper<>();
            accountAmountTypeQueryWrapper
                    .eq(AccountAmountType.ACCOUNT_AMOUNT_TYPE_GROUP_ID, group.getId())
                    .eq(AccountAmountType.JOINED_GROUP, true)
                    .ne(AccountAmountType.ACCOUNT_CODE, userCode);
            List<AccountAmountType> accountAmountTypes = accountAmountTypeDao.list(accountAmountTypeQueryWrapper);
            Map<Long, AccountAmountType> accountAmountTypeMap = accountAmountTypes.stream().collect(Collectors.toMap(AccountAmountType::getAccountCode, type->type));
            List<Long> accountCodes = accountAmountTypes.stream().map(AccountAmountType::getAccountCode).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(accountCodes)) {
                QueryWrapper<Account> accountQueryWrapper = new QueryWrapper<>();
                accountQueryWrapper.in(Account.ACCOUNT_CODE, accountCodes);
                Map<Long, Account> accountMap = accountDao.list(accountQueryWrapper).stream().collect(Collectors.toMap(Account::getAccountCode, a->a));
                dto.setMembers(NhcUserInfoDTO.of(group, accountMap, accountAmountTypeMap, merchant));
            }
        }
        return dto;
    }
}
