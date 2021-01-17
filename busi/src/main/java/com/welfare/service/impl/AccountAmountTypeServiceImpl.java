package com.welfare.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.welfare.common.constants.AccountChangeType;
import com.welfare.common.constants.WelfareConstant;
import com.welfare.common.exception.BusiException;
import com.welfare.common.exception.ExceptionCode;
import com.welfare.persist.dao.AccountAmountTypeDao;
import com.welfare.persist.dao.AccountDao;
import com.welfare.persist.entity.*;
import com.welfare.persist.mapper.AccountAmountTypeMapper;
import com.welfare.service.*;
import com.welfare.service.dto.Deposit;
import com.welfare.service.operator.merchant.AbstractMerAccountTypeOperator;
import com.welfare.service.operator.payment.domain.AccountAmountDO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.welfare.common.constants.RedisKeyConstant.ACCOUNT_AMOUNT_TYPE_OPERATE;
import static com.welfare.common.constants.RedisKeyConstant.MER_ACCOUNT_TYPE_OPERATE;
import static com.welfare.common.constants.WelfareConstant.MerAccountTypeCode.SURPLUS_QUOTA;

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
    private final MerchantAccountTypeService merchantAccountTypeService;
    private final RedissonClient redissonClient;
    private final AccountDao accountDao;
    private final AccountService accountService;
    private final OrderTransRelationService orderTransRelationService;
    private final AccountChangeEventRecordService accountChangeEventRecordService;
    /**
     * 循环依赖问题，所以未采用构造器注入
     */
    @Autowired
    private AccountBillDetailService accountBillDetailService;

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
        RLock lock = redissonClient.getFairLock(ACCOUNT_AMOUNT_TYPE_OPERATE + ":" + deposit.getAccountCode());
        lock.lock();
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
            orderTransRelationService.saveNewTransRelation(deposit.getApplyCode(),deposit.getTransNo(), WelfareConstant.TransType.DEPOSIT);
        } finally {
            lock.unlock();
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
        List<MerchantAccountType> types = merchantAccountTypeService.queryByMerCode(account.getMerCode());
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


}