package com.welfare.service.impl;

import com.welfare.persist.dao.AccountAmountTypeDao;
import com.welfare.persist.entity.AccountAmountType;
import com.welfare.persist.entity.MerchantCredit;
import com.welfare.service.AccountAmountTypeService;
import com.welfare.service.DepositService;
import com.welfare.service.MerchantCreditService;
import com.welfare.service.dto.Deposit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
    private final AccountAmountTypeService accountAmountTypeService;
    private final AccountAmountTypeDao accountAmountTypeDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deposit(Deposit deposit) {
        BigDecimal amount = deposit.getAmount();

        MerchantCredit merchantCredit = merchantCreditService.getByMerCode(deposit.getMerchantCode());
        merchantCreditService.updateMerchantRechargeCredit(merchantCredit,amount);
        AccountAmountType accountAmountType = accountAmountTypeService.queryOne(deposit.getAccountCode(),
                deposit.getMerAccountTypeCode());

        if(Objects.isNull(accountAmountType)){
            accountAmountType = deposit.toNewAccountAmountType();
            accountAmountTypeDao.save(accountAmountType);
        }else{
            accountAmountType.setAccountBalance(accountAmountType.getAccountBalance().add(amount));
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
                    List<Deposit> singleMerDeposits = entry.getValue();
                    BigDecimal totalAmountToDeposit = singleMerDeposits.stream()
                            .map(Deposit::getAmount)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    String merCode = entry.getKey();

                    MerchantCredit credit = merchantCreditService.getByMerCode(merCode);

                    merchantCreditService.updateMerchantRechargeCredit(credit,totalAmountToDeposit);

                });
    }
}
