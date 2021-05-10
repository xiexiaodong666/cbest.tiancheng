package com.welfare.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.welfare.common.constants.AccountChangeType;
import com.welfare.common.constants.WelfareConstant;
import com.welfare.common.exception.BizAssert;
import com.welfare.common.exception.ExceptionCode;
import com.welfare.common.exception.BizAssert;
import com.welfare.common.exception.BizException;
import com.welfare.common.exception.ExceptionCode;
import com.welfare.common.util.DistributedLockUtil;
import com.welfare.persist.dao.*;
import com.welfare.persist.dto.AccountDepositIncreDTO;
import com.welfare.persist.entity.*;
import com.welfare.persist.mapper.AccountAmountTypeMapper;
import com.welfare.service.*;
import com.welfare.service.dto.AccountAmountTypeResp;
import com.welfare.service.dto.Deposit;
import com.welfare.service.operator.payment.domain.AccountAmountDO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.welfare.common.constants.RedisKeyConstant.ACCOUNT_AMOUNT_TYPE_OPERATE;

/**
 * @author duanhy
 * @version 1.0.0
 * @description
 * @date 2021/1/8  9:29 PM
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AccountAmountTypeServiceImpl implements AccountAmountTypeService {
    private final AccountAmountTypeDao accountAmountTypeDao;
    private final AccountAmountTypeMapper accountAmountTypeMapper;
    private final MerchantAccountTypeDao merchantAccountTypeDao;
    private final RedissonClient redissonClient;
    private final AccountDao accountDao;
    @Autowired
    private AccountService accountService;
    private final OrderTransRelationService orderTransRelationService;
    @Autowired
    private AccountChangeEventRecordService accountChangeEventRecordService;
    private final AccountChangeEventRecordDao accountChangeEventRecordDao;
    private final AccountBillDetailDao accountBillDetailDao;
    private final OrderTransRelationDao orderTransRelationDao;
    private final AccountAmountTypeGroupDao accountAmountTypeGroupDao;
    /**
     * 循环依赖问题，所以未采用构造器注入
     */
    @Autowired
    private AccountBillDetailService accountBillDetailService;
    private final AccountDeductionDetailDao accountDeductionDetailDao;

    @Override
    public int batchSaveOrUpdate(List<AccountDepositIncreDTO> list) {
        return accountAmountTypeMapper.batchSaveOrUpdate(list);
    }

    @Override
    public AccountAmountType queryOne(Long accountCode, String merAccountTypeCode) {
        QueryWrapper<AccountAmountType> queryWrapper = new QueryWrapper();
        queryWrapper.eq(AccountAmountType.ACCOUNT_CODE, accountCode)
                .eq(AccountAmountType.MER_ACCOUNT_TYPE_CODE, merAccountTypeCode);
        return accountAmountTypeDao.getOne(queryWrapper);

    }

    @Override
    public void updateAccountAmountType(Deposit deposit) {
        RLock lock = DistributedLockUtil.lockFairly(ACCOUNT_AMOUNT_TYPE_OPERATE + deposit.getAccountCode());
        try {
            AccountAmountType accountAmountType = this.queryOne(deposit.getAccountCode(),
                    deposit.getMerAccountTypeCode());
            Account account = accountService.getByAccountCode(deposit.getAccountCode());
            BigDecimal oldAccountBalance = account.getAccountBalance() != null ? account.getAccountBalance() : BigDecimal.ZERO;
            if (Objects.isNull(accountAmountType)) {
                accountAmountType = deposit.toNewAccountAmountType();
                BigDecimal afterAddAmount = accountAmountType.getAccountBalance().add(deposit.getAmount());
                accountAmountType.setAccountBalance(afterAddAmount);
                accountAmountTypeDao.save(accountAmountType);
            } else {
                accountAmountType.setAccountBalance(accountAmountType.getAccountBalance().add(deposit.getAmount()));
                accountAmountTypeDao.getBaseMapper().updateBalance(
                        accountAmountType.getAccountCode(),
                        accountAmountType.getMerAccountTypeCode(),
                        accountAmountType.getAccountBalance(),
                        "sys");
            }
            if (!Lists.newArrayList(
                    WelfareConstant.MerAccountTypeCode.WHOLESALE.code(),
                    WelfareConstant.MerAccountTypeCode.WHOLESALE_PROCUREMENT.code(),
                    WelfareConstant.MerAccountTypeCode.SURPLUS_QUOTA.code(),
                    WelfareConstant.MerAccountTypeCode.SURPLUS_QUOTA_OVERPAY.code())
                    .contains(deposit.getMerAccountTypeCode())) {
                account.setAccountBalance(account.getAccountBalance().add(deposit.getAmount()));
            }
            AccountChangeEventRecord accountChangeEventRecord = new AccountChangeEventRecord();
            accountChangeEventRecord.setAccountCode(account.getAccountCode());
            accountChangeEventRecord.setChangeType(AccountChangeType.ACCOUNT_BALANCE_CHANGE.getChangeType());
            accountChangeEventRecord.setChangeValue(AccountChangeType.ACCOUNT_BALANCE_CHANGE.getChangeValue());
            accountChangeEventRecordService.save(accountChangeEventRecord);
            accountDao.getBaseMapper().updateAccountBalance(account.getAccountBalance(), account.getAccountCode());
            accountBillDetailService.saveNewAccountBillDetail(deposit, accountAmountType, account);
            orderTransRelationService.saveNewTransRelation(deposit.getApplyCode(),
                    deposit.getTransNo(),
                    WelfareConstant.TransType.DEPOSIT_INCR);
            AccountDeductionDetail deductionDetail = assemblyAccountDeductionDetail(deposit, account, accountAmountType);
            accountDeductionDetailDao.save(deductionDetail);
        } finally {
            DistributedLockUtil.unlock(lock);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchUpdateAccountAmountType(List<Deposit> deposits) {
        if (!CollectionUtils.isEmpty(deposits)) {
            List<RLock> locks = new ArrayList<>();
            RLock multiLock = null;
            try {
                deposits.forEach(deposit -> {
                    RLock lock = redissonClient.getFairLock(ACCOUNT_AMOUNT_TYPE_OPERATE + deposit.getAccountCode());
                    locks.add(lock);
                });
                multiLock = redissonClient.getMultiLock(locks.toArray(new RLock[]{}));
                multiLock.lock(-1, TimeUnit.SECONDS);
                String merAccountTypeCode = deposits.get(0).getMerAccountTypeCode();
                List<Long> accountCodes = deposits.stream().map(Deposit::getAccountCode).collect(Collectors.toList());
                Map<Long, Account> accountMap = accountDao.mapByAccountCodes(accountCodes);
                Map<Long, AccountAmountType> accountAmountTypeMap = accountAmountTypeDao.mapByAccountCodes(accountCodes, merAccountTypeCode);

                List<AccountDeductionDetail> deductionDetails = new ArrayList<>();
                List<AccountChangeEventRecord> records = new ArrayList<>();
                List<AccountBillDetail> details = new ArrayList<>();
                List<OrderTransRelation> relations = new ArrayList<>();
                List<AccountAmountType> newAccountAmountTypes = new ArrayList<>();

                List<Long> errorMsg = new ArrayList<>();
                for (Deposit deposit : deposits) {
                    AccountAmountType accountAmountType = accountAmountTypeMap.get(deposit.getAccountCode());
                    if (Objects.isNull(accountAmountType)) {
                        accountAmountType = deposit.toNewAccountAmountType();
                        BigDecimal afterAddAmount = accountAmountType.getAccountBalance().add(deposit.getAmount());
                        accountAmountType.setAccountBalance(afterAddAmount);
                        newAccountAmountTypes.add(accountAmountType);
                    } else {
                        accountAmountType.setAccountBalance(accountAmountType.getAccountBalance().add(deposit.getAmount()));
                    }

                    if (accountAmountType.getAccountBalance().compareTo(BigDecimal.ZERO) < 0) {
                        errorMsg.add(accountAmountType.getAccountCode());
                    }
                    Account account = accountMap.get(deposit.getAccountCode());
                    if (!Lists.newArrayList(
                            WelfareConstant.MerAccountTypeCode.WHOLESALE.code(),
                            WelfareConstant.MerAccountTypeCode.WHOLESALE_PROCUREMENT.code(),
                            WelfareConstant.MerAccountTypeCode.SURPLUS_QUOTA.code(),
                            WelfareConstant.MerAccountTypeCode.SURPLUS_QUOTA_OVERPAY.code())
                            .contains(deposit.getMerAccountTypeCode())) {
                        account.setAccountBalance(account.getAccountBalance().add(deposit.getAmount()));
                    }
                    AccountChangeEventRecord accountChangeEventRecord = new AccountChangeEventRecord();
                    accountChangeEventRecord.setAccountCode(account.getAccountCode());
                    accountChangeEventRecord.setChangeType(AccountChangeType.ACCOUNT_BALANCE_CHANGE.getChangeType());
                    accountChangeEventRecord.setChangeValue(AccountChangeType.ACCOUNT_BALANCE_CHANGE.getChangeValue());
                    records.add(accountChangeEventRecord);
                    AccountDeductionDetail deductionDetail = assemblyAccountDeductionDetail(deposit, account, accountAmountType);
                    deductionDetails.add(deductionDetail);
                    details.add(Deposit.assemblyAccountBillDetail(deposit, accountAmountType, account));
                    relations.add(assemblyNewTransRelation(
                            deposit.getApplyCode(),
                            deposit.getTransNo(),
                            WelfareConstant.TransType.DEPOSIT_INCR));
                }
                BizAssert.isTrue(CollectionUtils.isEmpty(errorMsg), ExceptionCode.ILLEGALITY_ARGUMENTS, "员工批发采购账户不可充为负\n" + Joiner.on(",").join(errorMsg));
                accountChangeEventRecordDao.getBaseMapper().batchInsert(records);
                accountAmountTypeDao.saveBatch(newAccountAmountTypes);
                Map<Long, BigDecimal> amountMap = deposits.stream().collect(Collectors.toMap(Deposit::getAccountCode, Deposit::getAmount));
                if (!CollectionUtils.isEmpty(accountAmountTypeMap)) {
                    List<AccountAmountType> amountTypes = Lists.newArrayList(accountAmountTypeMap.values());
                    accountAmountTypeMapper.batchSaveOrUpdate(AccountDepositIncreDTO.of(amountTypes, amountMap));
                }
                List<Account> accounts = Lists.newArrayList(accountMap.values());
                Map<Long, Long> changeEventIdMap = records.stream().collect(Collectors.toMap(AccountChangeEventRecord::getAccountCode,
                        AccountChangeEventRecord::getId));
                accountDao.getBaseMapper().batchUpdateAccountBalance(AccountDepositIncreDTO.of(accounts, amountMap, changeEventIdMap));

                accountDeductionDetailDao.saveBatch(deductionDetails, deductionDetails.size());
                accountBillDetailDao.saveBatch(details, details.size());
                orderTransRelationDao.saveBatch(relations, relations.size());
            } finally {
                DistributedLockUtil.unlock(multiLock);
            }
        }
    }

    @Override
    public List<AccountAmountDO> queryAccountAmountDO(Account account) {
        QueryWrapper<AccountAmountType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(AccountAmountType.ACCOUNT_CODE, account.getAccountCode());
        List<AccountAmountType> accountAmountTypes = accountAmountTypeDao.list(queryWrapper);
        List<MerchantAccountType> types = merchantAccountTypeDao.queryAllByMerCode(account.getMerCode());
        if (CollectionUtils.isEmpty(accountAmountTypes) || CollectionUtils.isEmpty(types)) {
            return Collections.emptyList();
        }
        Map<String, MerchantAccountType> map = types.stream()
                .collect(Collectors.toMap(MerchantAccountType::getMerAccountTypeCode, type -> type));
        return accountAmountTypes.stream()
                .map(accountAmountType -> {
                            if (accountAmountType.getJoinedGroup()) {
                                AccountAmountTypeGroup accountAmountTypeGroup = accountAmountTypeGroupDao.getById(accountAmountType.getAccountAmountTypeGroupId());
                                return AccountAmountDO.of(accountAmountType, map.get(accountAmountType.getMerAccountTypeCode()), account, accountAmountTypeGroup);
                            } else {
                                return AccountAmountDO.of(accountAmountType, map.get(accountAmountType.getMerAccountTypeCode()), account, null);
                            }
                        }
                ).collect(Collectors.toList());
    }

    @Override
    public List<AccountAmountType> batchQueryByAccount(List<Long> accountCodes, String merAccountTypeCode) {
        List<AccountAmountType> accountAmountTypes = new ArrayList<>();
        if (!CollectionUtils.isEmpty(accountCodes)) {
            accountAmountTypes = accountAmountTypeDao.listByAccountCodes(accountCodes, merAccountTypeCode);
        }
        return accountAmountTypes;
    }


    @Override
    public List<AccountAmountTypeResp> list(Long accountCode) {
        Account account = accountService.getByAccountCode(accountCode);
        List<AccountAmountType> accountAmountTypes = accountAmountTypeDao.queryByAccountCode(accountCode);
        Map<String, AccountAmountType> accountTypeMap = accountAmountTypes.stream().collect(Collectors.toMap(AccountAmountType::getMerAccountTypeCode, type -> type));
          List<AccountAmountTypeResp> resps = new ArrayList<>();
        if (Objects.nonNull(account)) {
            List<MerchantAccountType> merchantAccountTypes = merchantAccountTypeDao.queryAllByMerCode(account.getMerCode());
            merchantAccountTypes.forEach(merchantAccountType -> {
                AccountAmountTypeResp resp = new AccountAmountTypeResp();
                resp.setAccountCode(accountCode);
                resp.setShowOrder(merchantAccountType.getDeductionOrder());
                resp.setMerAccountTypeCode(merchantAccountType.getMerAccountTypeCode());
                resp.setMerAccountTypeName(merchantAccountType.getMerAccountTypeName());
                AccountAmountType accountAmountType = accountTypeMap.get(merchantAccountType.getMerAccountTypeCode());
                if (Objects.nonNull(accountAmountType)) {
                    if (merchantAccountType.getMerAccountTypeCode().equals(WelfareConstant.MerAccountTypeCode.SURPLUS_QUOTA.code())) {
                        resp.setBalance(accountAmountType.getAccountBalance() + "/" + account.getMaxQuota());
                    } else {
                        resp.setBalance(accountAmountType.getAccountBalance() + "");
                    }
                } else {
                    if (merchantAccountType.getMerAccountTypeCode().equals(WelfareConstant.MerAccountTypeCode.SURPLUS_QUOTA.code())) {
                        resp.setBalance(BigDecimal.ZERO.toString() + "/" + account.getMaxQuota());
                    } else {
                        resp.setBalance(BigDecimal.ZERO.toString());
                    }
                }
                resps.add(resp);
            });
        }
        return resps;
    }


    private AccountDeductionDetail assemblyAccountDeductionDetail(Deposit deposit, Account account,
                                                                  AccountAmountType accountAmountType) {
        AccountDeductionDetail accountDeductionDetail = new AccountDeductionDetail();
        accountDeductionDetail.setAccountCode(account.getAccountCode());
        accountDeductionDetail.setAccountDeductionAmount(deposit.getAmount().abs());
        accountDeductionDetail.setAccountAmountTypeBalance(accountAmountType.getAccountBalance());
        accountDeductionDetail.setMerAccountType(accountAmountType.getMerAccountTypeCode());
        accountDeductionDetail.setTransNo(deposit.getTransNo());
        accountDeductionDetail.setTransType(deposit.getAmount().compareTo(BigDecimal.ZERO) < 0 ?
                WelfareConstant.TransType.DEPOSIT_BACK.code() :  WelfareConstant.TransType.DEPOSIT_INCR.code());
        accountDeductionDetail.setTransAmount(deposit.getAmount().abs());
        accountDeductionDetail.setReversedAmount(BigDecimal.ZERO);
        accountDeductionDetail.setTransTime(Calendar.getInstance().getTime());
        accountDeductionDetail.setMerDeductionCreditAmount(BigDecimal.ZERO);
        accountDeductionDetail.setMerDeductionAmount(BigDecimal.ZERO);
        accountDeductionDetail.setSelfDeductionAmount(BigDecimal.ZERO);
        accountDeductionDetail.setCardId(deposit.getCardNo());
        accountDeductionDetail.setChanel(deposit.getChannel());
        accountDeductionDetail.setPaymentChannel(deposit.getPaymentChannel());
        return accountDeductionDetail;
    }

    private OrderTransRelation assemblyNewTransRelation(String orderId, String transNo, WelfareConstant.TransType transType) {
        OrderTransRelation orderTransRelation = new OrderTransRelation();
        orderTransRelation.setOrderId(orderId);
        orderTransRelation.setTransNo(transNo);
        orderTransRelation.setType(transType.code());
        return orderTransRelation;
    }
}