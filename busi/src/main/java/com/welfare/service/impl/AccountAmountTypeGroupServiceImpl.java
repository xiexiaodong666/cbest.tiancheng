package com.welfare.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.welfare.common.constants.WelfareConstant;
import com.welfare.common.exception.BizAssert;
import com.welfare.common.exception.ExceptionCode;
import com.welfare.common.util.DistributedLockUtil;
import com.welfare.persist.dao.AccountAmountTypeDao;
import com.welfare.persist.dao.AccountAmountTypeGroupDao;
import com.welfare.persist.dao.AccountBillDetailDao;
import com.welfare.persist.dao.AccountDeductionDetailDao;
import com.welfare.persist.entity.*;
import com.welfare.persist.dao.AccountDao;
import com.welfare.persist.entity.Account;
import com.welfare.persist.entity.AccountAmountType;
import com.welfare.persist.entity.AccountAmountTypeGroup;
import com.welfare.persist.mapper.AccountAmountTypeMapper;
import com.welfare.service.*;
import com.welfare.service.dto.BatchSequence;
import com.welfare.service.dto.account.AccountAmountTypeGroupDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import java.util.Objects;
import java.util.stream.Collectors;

import static com.welfare.common.constants.CacheConstant.TOTAL_ACCOUNT_AMOUNT_TYPE_GROUP_COUNT;
import static com.welfare.common.constants.RedisKeyConstant.ACCOUNT_AMOUNT_TYPE_GROUP_OPERATE;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/4/13 2:46 下午
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AccountAmountTypeGroupServiceImpl implements AccountAmountTypeGroupService {

    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountAmountTypeService accountAmountTypeService;
    @Autowired
    private AccountAmountTypeDao accountAmountTypeDao;
    @Autowired
    private AccountAmountTypeGroupDao accountAmountTypeGroupDao;
    @Autowired
    private SequenceService sequenceService;
    @Autowired
    private AccountBillDetailDao accountBillDetailDao;
    @Autowired
    private AccountDeductionDetailDao accountDeductionDetailDao;
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private AccountChangeEventRecordService accountChangeEventRecordService;
    private final AccountDao accountDao;
    private final AccountAmountTypeMapper accountAmountTypeMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = TOTAL_ACCOUNT_AMOUNT_TYPE_GROUP_COUNT,key = "#merCode")
    public boolean removeByAccountCode(String merCode, Long accountCode, String merAccountTypeCode) {
        Account account = accountService.getByAccountCode(accountCode);
        BizAssert.notNull(account, ExceptionCode.ILLEGALITY_ARGURMENTS, "员工不存在");
        AccountAmountType accountAmountType = accountAmountTypeService.queryOne(accountCode, merAccountTypeCode);
        BizAssert.notNull(accountAmountType, ExceptionCode.ILLEGALITY_ARGURMENTS, "福利类型为空");
        if (accountAmountType.getJoinedGroup() == null || !accountAmountType.getJoinedGroup()
                || Objects.isNull(accountAmountType.getAccountAmountTypeGroupId())) {
            return true;
        }
        long groupId = accountAmountType.getAccountAmountTypeGroupId();
        AccountAmountTypeGroup group = accountAmountTypeGroupDao.getById(groupId);
        BizAssert.notNull(group, ExceptionCode.ILLEGALITY_ARGURMENTS, "员工福利账户组不存在");
        List<AccountAmountType> accountAmountTypes = accountAmountTypeDao.getByTypeAndAccountAmountTypeGroupId(WelfareConstant.MerAccountTypeCode.MALL_POINT.code(), groupId);
        BizAssert.notEmpty(accountAmountTypes, ExceptionCode.ILLEGALITY_ARGURMENTS, "员工福利账户组没有任何员工");

        accountAmountType.setJoinedGroup(Boolean.FALSE);
        accountAmountType.setAccountAmountTypeGroupId(null);

        Map<Long, Account> removeAccountMap = new HashMap<>();
        removeAccountMap.put(account.getAccountCode(), account);
        List<AccountAmountType> removeAccountAmountTypes = new ArrayList<>();
        removeAccountAmountTypes.add(accountAmountType);
        if (accountAmountTypes.size() == 2) {
            Map<Long, AccountAmountType> accountAmountTypeMap = accountAmountTypes.stream().collect(Collectors.toMap(AccountAmountType::getAccountCode, a->a));
            BizAssert.isTrue(accountAmountTypeMap.containsKey(accountAmountType.getAccountCode()), ExceptionCode.ILLEGALITY_ARGURMENTS, "员工不在该组内");
            // 解散组，将组的金额继承到组内最后一人
            accountAmountTypeMap.forEach((code, amountType) -> {
                if (!code.equals(accountAmountType.getAccountCode())) {
                    amountType.setJoinedGroup(Boolean.FALSE);
                    amountType.setAccountAmountTypeGroupId(null);
                    amountType.setAccountBalance(group.getBalance());
                    removeAccountAmountTypes.add(amountType);
                    removeAccountMap.put(code, accountService.getByAccountCode(accountCode));
                }
            });
        }
        // 记录流水
        BatchSequence batchSequence = sequenceService.batchGenerate(WelfareConstant.SequenceType.DEPOSIT.code(), removeAccountAmountTypes.size());
        AtomicInteger index = new AtomicInteger();
        List<AccountBillDetail> accountBillDetails = new ArrayList<>();
        List<AccountDeductionDetail> accountDeductionDetails = new ArrayList<>();
        removeAccountAmountTypes.forEach(removeAccountAmountType -> {
            String transNo = "" + batchSequence.getSequences().get(index.getAndIncrement()).getSequenceNo();
            AccountBillDetail accountBillDetail = assemblyAccountBillDetail(
                    removeAccountMap.get(removeAccountAmountType.getAccountCode()),
                    removeAccountAmountType.getAccountBalance(),
                    removeAccountAmountType,
                    transNo,
                    WelfareConstant.TransType.LEAVE_GROUP,
                    groupId);
            AccountDeductionDetail accountDeductionDetail = assemblyAccountDeductionDetail(
                    removeAccountMap.get(removeAccountAmountType.getAccountCode()),
                    transNo,
                    WelfareConstant.TransType.LEAVE_GROUP,
                    removeAccountAmountType,
                    groupId,
                    accountCode.equals(removeAccountAmountType.getAccountCode()) ? BigDecimal.ZERO : group.getBalance(),
                    removeAccountAmountType.getAccountBalance());
            accountBillDetails.add(accountBillDetail);
            accountDeductionDetails.add(accountDeductionDetail);
        });
        accountBillDetailDao.saveBatch(accountBillDetails);
        accountDeductionDetailDao.saveBatch(accountDeductionDetails);
        return accountAmountTypeDao.updateBatchById(removeAccountAmountTypes);
    }

    @Override
    public AccountAmountTypeGroup queryByAccountCode(Long accountCode) {
        AccountAmountType accountAmountType = accountAmountTypeDao.queryByAccountCodeAndAmountType(
                accountCode,
                WelfareConstant.MerAccountTypeCode.MALL_POINT.code()
        );
        if (Objects.nonNull(accountAmountType) && accountAmountType.getJoinedGroup()) {
            return accountAmountTypeGroupDao.getById(accountAmountType.getAccountAmountTypeGroupId());
        }else{
            return null;
        }
    }

    @Override
    public AccountAmountTypeGroupDTO queryDO(Long accountCode) {
        AccountAmountTypeGroup accountAmountTypeGroup = queryByAccountCode(accountCode);
        List<AccountAmountType> accountAmountTypes = accountAmountTypeDao.queryByGroupId(accountAmountTypeGroup.getId());
        List<Long> accountCodes = accountAmountTypes.stream().map(AccountAmountType::getAccountCode).collect(Collectors.toList());
        List<Account> accounts = accountDao.listByAccountCodes(accountCodes);
        return AccountAmountTypeGroupDTO.of(accountAmountTypeGroup,accounts);
    }

    @Override
    @Cacheable(value = TOTAL_ACCOUNT_AMOUNT_TYPE_GROUP_COUNT, key = "#merCode")
    public Long countGroups(String merCode, String merAccountTypeCode) {
        return accountAmountTypeMapper.countByMerCodeAndMerAccountType(merCode,merAccountTypeCode);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean batchUpdateGroupAmount(List<GroupDeposit> deposits) {
        BizAssert.notEmpty(deposits, ExceptionCode.ILLEGALITY_ARGURMENTS, "参数为空");
        Set<Long> groupIds = deposits.stream().map(GroupDeposit::getGroupId).collect(Collectors.toSet());
        List<RLock> locks = new ArrayList<>();
        RLock multiLock = null;
        try {
            Set<Long> accountCodes = new HashSet<>();
            deposits.forEach(deposit -> {
                BizAssert.notEmpty(deposit.getDeposits(), ExceptionCode.ILLEGALITY_ARGURMENTS, "充值明细为空");
                accountCodes.addAll(deposit.getDeposits().stream().map(GroupDeposit.DepositItem::getAccountCode).collect(Collectors.toSet()));
                locks.add(redissonClient.getFairLock(ACCOUNT_AMOUNT_TYPE_GROUP_OPERATE + deposit.getGroupId()));
            });
            multiLock = redissonClient.getMultiLock(locks.toArray(new RLock[]{}));
            multiLock.lock(-1, TimeUnit.SECONDS);
            Map<Long, AccountAmountTypeGroup> groupMap = accountAmountTypeGroupDao.listByIds(groupIds).stream().collect(Collectors.toMap(AccountAmountTypeGroup::getId, a -> a,(k1, k2)->k1));
            Map<Long, Account> accountMap = accountDao.mapByAccountCodes(Lists.newArrayList(accountCodes));
            Map<Long, AccountAmountType> accountAmountTypeMap = accountAmountTypeDao.mapByAccountCodes(Lists.newArrayList(accountCodes),
                    WelfareConstant.MerAccountTypeCode.MALL_POINT.code());
            validationUpdateAmountParams(deposits, groupMap, accountMap, accountAmountTypeMap);
            List<AccountBillDetail> accountBillDetails = new ArrayList<>();
            List<AccountDeductionDetail> accountDeductionDetails = new ArrayList<>();
            deposits.forEach(groupDeposit -> {
                AccountAmountTypeGroup group = groupMap.get(groupDeposit.getGroupId());
                groupDeposit.getDeposits().forEach(depositItem -> {
                    group.setBalance(group.getBalance().add(depositItem.getAmount()));
                    Account account = accountMap.get(depositItem.getAccountCode());
                    AccountAmountType accountAmountType = accountAmountTypeMap.get(depositItem.getAccountCode());
                    AccountBillDetail accountBillDetail = assemblyAccountBillDetail(
                            account,
                            depositItem.getAmount(),
                            accountAmountType,
                            depositItem.getTransNo(),
                            WelfareConstant.TransType.DEPOSIT_INCR,
                            group.getId());
                    accountBillDetails.add(accountBillDetail);
                    AccountDeductionDetail accountDeductionDetail = assemblyAccountDeductionDetail(
                            account,
                            depositItem.getTransNo(),
                            WelfareConstant.TransType.DEPOSIT_INCR,
                            accountAmountType,
                            group.getId(),
                            depositItem.getAmount(),
                            group.getBalance());
                    accountDeductionDetails.add(accountDeductionDetail);
                });
            });
            accountBillDetailDao.saveBatch(accountBillDetails);
            accountDeductionDetailDao.saveBatch(accountDeductionDetails);
            return accountAmountTypeGroupDao.updateBatchById(groupMap.values());
        } finally {
            DistributedLockUtil.unlock(multiLock);
        }
    }

    @Override
    public List<AccountAmountTypeGroup> listById(List<Long> groupIds) {
        if (CollectionUtils.isNotEmpty(groupIds)) {
            QueryWrapper<AccountAmountTypeGroup> queryWrapper = new QueryWrapper<>();
            queryWrapper.in(AccountAmountTypeGroup.ID, groupIds);
            return accountAmountTypeGroupDao.list(queryWrapper);
        }
        return new ArrayList<>();
    }

    private void validationUpdateAmountParams(List<GroupDeposit> deposits,  Map<Long, AccountAmountTypeGroup> groupMap, Map<Long, Account> accountMap,
                                              Map<Long, AccountAmountType> accountAmountTypeMap) {
        deposits.forEach(groupDeposit -> {
            BizAssert.isTrue(groupMap.containsKey(groupDeposit.getGroupId()), ExceptionCode.ILLEGALITY_ARGURMENTS,
                    "员工福利账号组不存在");
            groupDeposit.getDeposits().forEach(depositItem -> {
                BizAssert.isTrue(accountMap.containsKey(depositItem.getAccountCode()), ExceptionCode.ILLEGALITY_ARGURMENTS,
                        "员工不存在");
                BizAssert.isTrue(accountAmountTypeMap.containsKey(depositItem.getAccountCode()), ExceptionCode.ILLEGALITY_ARGURMENTS,
                        "员工福利类型不存在");
                AccountAmountType accountAmountType = accountAmountTypeMap.get(depositItem.getAccountCode());
                Boolean joinedGroup = accountAmountType.getJoinedGroup();
                Long joinedGroupId = accountAmountType.getAccountAmountTypeGroupId();
                BizAssert.isTrue(Objects.nonNull(joinedGroup) && joinedGroup && Objects.nonNull(joinedGroupId),
                        ExceptionCode.ILLEGALITY_ARGURMENTS,
                        "员工没有加入任何组");
                BizAssert.isTrue(joinedGroupId.equals(groupDeposit.getGroupId()), ExceptionCode.ILLEGALITY_ARGURMENTS,
                        "员工不属于该组");
            });
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = TOTAL_ACCOUNT_AMOUNT_TYPE_GROUP_COUNT,key = "#merCode")
    public boolean addByAccountCodeAndMerAccountTypeCode(String merCode, Long joinAccountCode, Long groupAccountCode, String merAccountTypeCode) {
        Account joinAccount = accountService.getByAccountCode(joinAccountCode);
        BizAssert.notNull(joinAccount, ExceptionCode.ILLEGALITY_ARGURMENTS, "员工不存在");
        Account groupAccount = accountService.getByAccountCode(groupAccountCode);
        BizAssert.notNull(groupAccount, ExceptionCode.ILLEGALITY_ARGURMENTS, "组员工不存在");
        BizAssert.isTrue(!joinAccount.getAccountCode().equals(groupAccount.getAccountCode()),
                ExceptionCode.NO_AND_OWN_GROUP, "不能和自己一个组");
        Map<Long, Account> accountMap = new HashMap<>();
        accountMap.put(joinAccount.getAccountCode(), joinAccount);
        accountMap.put(groupAccount.getAccountCode(), groupAccount);
        BizAssert.isTrue(joinAccount.getMerCode().equals(groupAccount.getMerCode()), ExceptionCode.ILLEGALITY_ARGURMENTS, "家庭员工不存在");
        AccountAmountType joinAccountAmountType = accountAmountTypeService.queryOne(joinAccountCode, merAccountTypeCode);
        AccountAmountType groupAccountAmountType = accountAmountTypeService.queryOne(groupAccountCode, merAccountTypeCode);
        BizAssert.isTrue(Objects.nonNull(joinAccountAmountType) && Objects.nonNull(groupAccountAmountType),
                ExceptionCode.ILLEGALITY_ARGURMENTS, "福利账户不存在");
        AccountAmountTypeGroup amountTypeGroup;
        List<AccountAmountType> updateAccountAmountTypes = new ArrayList<>();

        // 如果已属于某个组，先退组
        removeByAccountCode(merCode, joinAccountCode, merAccountTypeCode);

        if (groupAccountAmountType.getJoinedGroup() && Objects.nonNull(groupAccountAmountType.getAccountAmountTypeGroupId())) {
            amountTypeGroup = accountAmountTypeGroupDao.getById(groupAccountAmountType.getAccountAmountTypeGroupId());
            BizAssert.notNull(amountTypeGroup, ExceptionCode.ILLEGALITY_ARGURMENTS, "用户组不存在");
            updateAccountAmountTypes.add(joinAccountAmountType);
        } else {
            amountTypeGroup = new AccountAmountTypeGroup();
            amountTypeGroup.setMerAccountTypeCode(merAccountTypeCode);
            amountTypeGroup.setBalance(BigDecimal.ZERO);
            amountTypeGroup.setGroupCode("" + sequenceService.nextNo(WelfareConstant.SequenceType.ACCOUNT_AMOUNT_TYPE_GROUP_CODE.code()));
            updateAccountAmountTypes.add(joinAccountAmountType);
            updateAccountAmountTypes.add(groupAccountAmountType);
        }
        // 创建或更新组
        boolean flag1 = accountAmountTypeGroupDao.saveOrUpdate(amountTypeGroup);
        // 记录流水
        List<AccountBillDetail> accountBillDetails = new ArrayList<>();
        List<AccountDeductionDetail> accountDeductionDetails = new ArrayList<>();
        BatchSequence batchSequence = sequenceService.batchGenerate(WelfareConstant.SequenceType.DEPOSIT.code(), updateAccountAmountTypes.size());
        AtomicInteger index = new AtomicInteger();
        updateAccountAmountTypes.forEach(accountAmountType -> {
            amountTypeGroup.setBalance(amountTypeGroup.getBalance().add(accountAmountType.getAccountBalance()));
            String transNo = batchSequence.getSequences().get(index.getAndIncrement()).getSequenceNo() + "";
            AccountBillDetail accountBillDetail = assemblyAccountBillDetail(
                    accountMap.get(accountAmountType.getAccountCode()),
                    accountAmountType.getAccountBalance(),
                    accountAmountType,
                    transNo,
                    WelfareConstant.TransType.JOINED_GROUP,
                    amountTypeGroup.getId());
            accountBillDetails.add(accountBillDetail);
            AccountDeductionDetail accountDeductionDetail = assemblyAccountDeductionDetail(
                    accountMap.get(accountAmountType.getAccountCode()),
                    transNo,
                    WelfareConstant.TransType.JOINED_GROUP,
                    accountAmountType,
                    amountTypeGroup.getId(),
                    accountAmountType.getAccountBalance(),
                    amountTypeGroup.getBalance());
            accountDeductionDetails.add(accountDeductionDetail);
        });
        accountBillDetailDao.saveBatch(accountBillDetails);
        accountDeductionDetailDao.saveBatch(accountDeductionDetails);
        accountAmountTypeGroupDao.updateById(amountTypeGroup);
        updateAccountAmountTypes.forEach(accountAmountType -> {
            accountAmountType.setJoinedGroup(Boolean.TRUE);
            accountAmountType.setAccountAmountTypeGroupId(amountTypeGroup.getId());
            accountAmountType.setAccountBalance(BigDecimal.ZERO);
        });
        // 组成员福利账户金额清零
        boolean flag2 = accountAmountTypeDao.updateBatchById(updateAccountAmountTypes);
        return flag1 && flag2;
    }

    private AccountBillDetail assemblyAccountBillDetail(Account account, BigDecimal amount, AccountAmountType accountAmountType, String transNo, WelfareConstant.TransType transType,
                                                        Long accountAmountTypeGroupId) {
        AccountBillDetail accountBillDetail = new AccountBillDetail();
        accountBillDetail.setAccountCode(account.getAccountCode());
        accountBillDetail.setAccountBalance(account.getAccountBalance());
        accountBillDetail.setChannel(WelfareConstant.Channel.PLATFORM.code());
        accountBillDetail.setTransNo(transNo);
        accountBillDetail.setTransAmount(amount.abs());
        accountBillDetail.setTransTime(Calendar.getInstance().getTime());
        accountBillDetail.setSurplusQuota(account.getSurplusQuota());
        accountBillDetail.setSurplusQuotaOverpay(account.getSurplusQuotaOverpay());
        accountBillDetail.setTransType(transType.code());
        accountBillDetail.setAccountAmountTypeGroupId(accountAmountTypeGroupId);
        return accountBillDetail;
    }

    private AccountDeductionDetail assemblyAccountDeductionDetail(Account account, String transNo,
                                                                  WelfareConstant.TransType transType,
                                                                  AccountAmountType accountAmountType,
                                                                  Long accountAmountTypeGroupId,
                                                                  BigDecimal updateAmountTypeBalance,
                                                                  BigDecimal remainingAmount) {
        AccountDeductionDetail accountDeductionDetail = new AccountDeductionDetail();
        accountDeductionDetail.setAccountCode(account.getAccountCode());
        accountDeductionDetail.setAccountDeductionAmount(updateAmountTypeBalance.abs());
        accountDeductionDetail.setAccountAmountTypeBalance(remainingAmount);
        accountDeductionDetail.setMerAccountType(accountAmountType.getMerAccountTypeCode());
        accountDeductionDetail.setTransNo(transNo);
        accountDeductionDetail.setTransType(transType.code());
        accountDeductionDetail.setTransAmount(updateAmountTypeBalance.abs());
        accountDeductionDetail.setReversedAmount(BigDecimal.ZERO);
        accountDeductionDetail.setTransTime(Calendar.getInstance().getTime());
        accountDeductionDetail.setMerDeductionCreditAmount(BigDecimal.ZERO);
        accountDeductionDetail.setMerDeductionAmount(BigDecimal.ZERO);
        accountDeductionDetail.setChanel(WelfareConstant.Channel.PLATFORM.code());
        accountDeductionDetail.setSelfDeductionAmount(BigDecimal.ZERO);
        accountDeductionDetail.setAccountAmountTypeGroupId(accountAmountTypeGroupId);
        return accountDeductionDetail;
    }
}
