package com.welfare.service.impl;

import com.welfare.common.constants.WelfareConstant;
import com.welfare.persist.entity.Account;
import com.welfare.persist.entity.AccountBillDetail;
import com.welfare.service.*;
import com.welfare.service.dto.Deposit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.welfare.common.constants.WelfareConstant.MerCreditType.RECHARGE_LIMIT;

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
    private final AccountBillDetailService accountBillDetailService;
    private final AccountService accountService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deposit(Deposit deposit) {
        BigDecimal amount = deposit.getAmount();
        merchantCreditService.decreaseAccountType(deposit.getMerchantCode(), RECHARGE_LIMIT, amount, deposit.getTransNo(), WelfareConstant.TransType.DEPOSIT.code() );
        accountAmountTypeService.updateAccountAmountType(deposit);
        deposit.setDepositStatus(WelfareConstant.AsyncStatus.SUCCEED.code());
    }



    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deposit(List<Deposit> deposits) {
        Map<String, List<Deposit>> groupedDeposits = deposits.stream()
                .collect(Collectors.groupingBy(Deposit::getMerchantCode));
        groupedDeposits.forEach((key, singleMerDeposits) -> singleMerDeposits.forEach(this::deposit));
    }

    @Override
    public Deposit getByTransNo(String transNo) {
        AccountBillDetail accountBillDetail = accountBillDetailService.queryByTransNo(transNo);
        Account account = accountService.getByAccountCode(accountBillDetail.getAccountCode());
        return Deposit.of(accountBillDetail, account);
    }


}
