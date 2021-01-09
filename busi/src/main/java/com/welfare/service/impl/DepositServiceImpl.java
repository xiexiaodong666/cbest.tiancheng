package com.welfare.service.impl;

import com.welfare.common.exception.BusiException;
import com.welfare.common.exception.ExceptionCode;
import com.welfare.persist.dao.AccountAmountTypeDao;
import com.welfare.persist.dao.MerchantAccountTypeDao;
import com.welfare.persist.dao.MerchantCreditDao;
import com.welfare.persist.entity.AccountAmountType;
import com.welfare.persist.entity.MerchantCredit;
import com.welfare.service.AccountAmountTypeService;
import com.welfare.service.DepositService;
import com.welfare.service.MerchantAccountTypeService;
import com.welfare.service.MerchantCreditService;
import com.welfare.service.dto.Deposit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 1/9/2021
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class DepositServiceImpl implements DepositService {
    private final MerchantCreditService merchantCreditService;
    private final MerchantCreditDao merchantCreditDao;
    private final AccountAmountTypeService accountAmountTypeService;
    private final AccountAmountTypeDao accountAmountTypeDao;
    private final MerchantAccountTypeService merchantAccountTypeService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deposit(Deposit deposit) {
        MerchantCredit merchantCredit = merchantCreditService.getByMerCode(deposit.getMerchantCode());
        BigDecimal rechargeLimit = merchantCredit.getRechargeLimit();
        if (rechargeLimit.subtract(deposit.getAmount()).compareTo(BigDecimal.ZERO) < 0) {
            throw new BusiException(ExceptionCode.MERCHANT_RECHARGE_LIMIT_EXCEED, "充值额度不足", null);
        }
        merchantCredit.setRechargeLimit(merchantCredit.getRechargeLimit().subtract(deposit.getAmount()));
        merchantCreditDao.updateById(merchantCredit);
        AccountAmountType accountAmountType = accountAmountTypeService.queryOne(deposit.getAccountCode(),
                deposit.getMerAccountTypeCode());

        if(Objects.isNull(accountAmountType)){
            accountAmountType = deposit.toNewAccountAmountType();
            accountAmountTypeDao.save(accountAmountType);
        }else{
            accountAmountType.setAccountBalance(accountAmountType.getAccountBalance().add(deposit.getAmount()));
            accountAmountTypeDao.updateById(accountAmountType);
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deposit(List<Deposit> deposits) {
        Map<String, List<Deposit>> groupedDeposits = deposits.stream()
                .collect(Collectors.groupingBy(deposit -> deposit.getMerchantCode()));
        groupedDeposits.entrySet()
                .stream()
                .forEach(entry -> {
                    String merCode = entry.getKey();
                    MerchantCredit credit = merchantCreditService.getByMerCode(merCode);
                    BigDecimal rechargeLimit = credit.getRechargeLimit();
                    List<Deposit> singleMerDeposits = entry.getValue();
                    BigDecimal totalAmountToDeposit = singleMerDeposits.stream()
                            .map(Deposit::getAmount)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    if(rechargeLimit.compareTo(totalAmountToDeposit) < 0){
                        throw new BusiException(ExceptionCode.MERCHANT_RECHARGE_LIMIT_EXCEED, "充值额度不足", null);
                    }

                    credit.setRechargeLimit(credit.getRechargeLimit().subtract(totalAmountToDeposit));
                    merchantCreditDao.updateById(credit);

                });
    }
}
