package com.welfare.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.welfare.common.constants.AccountChangeType;
import com.welfare.common.constants.WelfareConstant;
import com.welfare.common.util.DistributedLockUtil;
import com.welfare.persist.dao.*;
import com.welfare.persist.entity.*;
import com.welfare.persist.mapper.AccountAmountTypeMapper;
import com.welfare.service.*;
import com.welfare.service.dto.Deposit;
import com.welfare.service.operator.payment.domain.AccountAmountDO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.RedissonLock;
import org.redisson.RedissonMultiLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import sun.swing.BakedArrayList;

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
    private final AccountService accountService;
    private final OrderTransRelationService orderTransRelationService;
    private final AccountChangeEventRecordService accountChangeEventRecordService;
    private final AccountChangeEventRecordDao accountChangeEventRecordDao;
    private final AccountBillDetailDao accountBillDetailDao;
    private final OrderTransRelationDao orderTransRelationDao;

    /**
     * 循环依赖问题，所以未采用构造器注入
     */
    @Autowired
    private AccountBillDetailService accountBillDetailService;
    private final AccountDeductionDetailDao accountDeductionDetailDao;

    @Override
    public int batchSaveOrUpdate(List<AccountAmountType> list) {
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
        try{
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
                accountAmountTypeDao.updateById(accountAmountType);
            }
            account.setAccountBalance(oldAccountBalance.add(deposit.getAmount()));
            AccountChangeEventRecord accountChangeEventRecord = new AccountChangeEventRecord();
            accountChangeEventRecord.setAccountCode(account.getAccountCode());
            accountChangeEventRecord.setChangeType(AccountChangeType.ACCOUNT_BALANCE_CHANGE.getChangeType());
            accountChangeEventRecord.setChangeValue(AccountChangeType.ACCOUNT_BALANCE_CHANGE.getChangeValue());
            accountChangeEventRecordService.save(accountChangeEventRecord);
            accountDao.saveOrUpdate(account);
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
                    Account account = accountMap.get(deposit.getAccountCode());
                    account.setAccountBalance(account.getAccountBalance().add(deposit.getAmount()));
                    AccountChangeEventRecord accountChangeEventRecord = new AccountChangeEventRecord();
                    accountChangeEventRecord.setAccountCode(account.getAccountCode());
                    accountChangeEventRecord.setChangeType(AccountChangeType.ACCOUNT_BALANCE_CHANGE.getChangeType());
                    accountChangeEventRecord.setChangeValue(AccountChangeType.ACCOUNT_BALANCE_CHANGE.getChangeValue());
                    records.add(accountChangeEventRecord);
                    AccountDeductionDetail deductionDetail = assemblyAccountDeductionDetail(deposit, account, accountAmountType);
                    deductionDetails.add(deductionDetail);
                    details.add(assemblyAccountBillDetail(deposit, accountAmountType, account));
                    relations.add(assemblyNewTransRelation(
                            deposit.getApplyCode(),
                            deposit.getTransNo(),
                            WelfareConstant.TransType.DEPOSIT_INCR));
                }
                if (!CollectionUtils.isEmpty(newAccountAmountTypes)) {
                    accountAmountTypeDao.saveBatch(newAccountAmountTypes, newAccountAmountTypes.size());
                }
                if (!CollectionUtils.isEmpty(accountAmountTypeMap)) {
                    accountAmountTypeDao.updateBatchById(accountAmountTypeMap.values(), accountAmountTypeMap.size());
                }
                accountDao.updateBatchById(accountMap.values(), accountMap.size());
                accountDeductionDetailDao.saveBatch(deductionDetails, deductionDetails.size());
                accountChangeEventRecordDao.saveBatch(records, records.size());
                accountBillDetailDao.saveBatch(details, details.size());
                orderTransRelationDao.saveBatch(relations, relations.size());
            } finally {
                DistributedLockUtil.unlock(multiLock);
            }
        }
    }


    @Override
    public AccountAmountType querySurplusQuota(Long accountCode) {
        QueryWrapper<AccountAmountType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(AccountAmountType.ACCOUNT_CODE, accountCode)
                .eq(AccountAmountType.MER_ACCOUNT_TYPE_CODE, WelfareConstant.MerAccountTypeCode.SURPLUS_QUOTA.code());
        return accountAmountTypeDao.getOne(queryWrapper);
    }

    @Override
    public List<AccountAmountDO> queryAccountAmountDO(Account account) {
        QueryWrapper<AccountAmountType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(AccountAmountType.ACCOUNT_CODE, account.getAccountCode());
        List<AccountAmountType> accountAmountTypes = accountAmountTypeDao.list(queryWrapper);
        Assert.isTrue(!CollectionUtils.isEmpty(accountAmountTypes), "该用户没有账户余额信息");
        List<MerchantAccountType> types = merchantAccountTypeDao.queryAllByMerCode(account.getMerCode());
        Assert.isTrue(!CollectionUtils.isEmpty(types), "该商户没有配置accountType");

        Map<String, MerchantAccountType> map = types.stream()
                .collect(Collectors.toMap(MerchantAccountType::getMerAccountTypeCode, type -> type));
        return accountAmountTypes.stream()
                .map(accountAmountType ->
                        AccountAmountDO.of(accountAmountType, map.get(accountAmountType.getMerAccountTypeCode()),account))
                .collect(Collectors.toList());
    }

    @Override
    public BigDecimal sumBalanceExceptSurplusQuota(Long accountCode) {
        QueryWrapper<AccountAmountType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(AccountAmountType.ACCOUNT_CODE,accountCode)
                .ne(AccountAmountType.MER_ACCOUNT_TYPE_CODE, WelfareConstant.MerAccountTypeCode.SURPLUS_QUOTA)
                .select(AccountAmountType.MER_ACCOUNT_TYPE_CODE,AccountAmountType.ACCOUNT_BALANCE);
        List<AccountAmountType> accountAmountTypes = accountAmountTypeDao.list(queryWrapper);
        BigDecimal balanceSum = accountAmountTypes.stream()
                .map(AccountAmountType::getAccountBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return balanceSum;
    }

    private AccountDeductionDetail assemblyAccountDeductionDetail(Deposit deposit, Account account,
                                                                  AccountAmountType accountAmountType) {
        AccountDeductionDetail accountDeductionDetail = new AccountDeductionDetail();
        accountDeductionDetail.setAccountCode(account.getAccountCode());
        accountDeductionDetail.setAccountDeductionAmount(deposit.getAmount());
        accountDeductionDetail.setAccountAmountTypeBalance(accountAmountType.getAccountBalance());
        accountDeductionDetail.setMerAccountType(accountAmountType.getMerAccountTypeCode());
        accountDeductionDetail.setTransNo(deposit.getTransNo());
        accountDeductionDetail.setTransType(WelfareConstant.TransType.DEPOSIT_INCR.code());
        accountDeductionDetail.setTransAmount(deposit.getAmount());
        accountDeductionDetail.setReversedAmount(BigDecimal.ZERO);
        accountDeductionDetail.setTransTime(Calendar.getInstance().getTime());
        accountDeductionDetail.setMerDeductionCreditAmount(BigDecimal.ZERO);
        accountDeductionDetail.setMerDeductionAmount(BigDecimal.ZERO);
        accountDeductionDetail.setSelfDeductionAmount(BigDecimal.ZERO);
        accountDeductionDetail.setCardId(deposit.getCardNo());
        accountDeductionDetail.setChanel(deposit.getChannel());
        return accountDeductionDetail;
    }

    private AccountBillDetail assemblyAccountBillDetail(Deposit deposit, AccountAmountType accountAmountType,
                                                        Account account) {
        AccountBillDetail accountBillDetail = new AccountBillDetail();
        Long accountCode = deposit.getAccountCode();
        BigDecimal amount = deposit.getAmount();
        accountBillDetail.setAccountCode(accountCode);
        accountBillDetail.setAccountBalance(account.getAccountBalance());
        accountBillDetail.setChannel(deposit.getChannel());
        accountBillDetail.setTransNo(deposit.getTransNo());
        accountBillDetail.setTransAmount(amount);
        accountBillDetail.setTransTime(Calendar.getInstance().getTime());
        accountBillDetail.setSurplusQuota(account.getSurplusQuota());
        accountBillDetail.setTransType(WelfareConstant.TransType.DEPOSIT_INCR.code());
        return accountBillDetail;
    }

    private OrderTransRelation assemblyNewTransRelation(String orderId, String transNo, WelfareConstant.TransType transType) {
        OrderTransRelation orderTransRelation = new OrderTransRelation();
        orderTransRelation.setOrderId(orderId);
        orderTransRelation.setTransNo(transNo);
        orderTransRelation.setType(transType.code());
        return orderTransRelation;
    }
}