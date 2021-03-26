package com.welfare.service.payment;

import com.welfare.persist.entity.*;
import com.welfare.service.dto.payment.PaymentRequest;
import com.welfare.service.operator.merchant.CurrentBalanceOperator;
import com.welfare.service.operator.payment.domain.AccountAmountDO;
import com.welfare.service.operator.payment.domain.PaymentOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 3/26/2021
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class WelfarePaymentOperator implements IPaymentOperator{
    private final CurrentBalanceOperator currentBalanceOperator;
    @Override
    public List<PaymentOperation> pay(PaymentRequest paymentRequest, Account account, List<AccountAmountDO> accountAmountDOList, SupplierStore supplierStore, MerchantCredit merchantCredit) {
        BigDecimal usableAmount = account.getAccountBalance()
                .add(account.getSurplusQuota())
                .add(account.getSurplusQuotaOverpay()==null?BigDecimal.ZERO:account.getSurplusQuotaOverpay());
        BigDecimal amount = paymentRequest.getAmount();
        boolean enough = usableAmount.subtract(amount).compareTo(BigDecimal.ZERO) >= 0;
        Assert.isTrue(enough, "用户账户总余额不足");
        Assert.notEmpty(accountAmountDOList,"用户没有可用的福利类型");
        BigDecimal allTypeBalance = accountAmountDOList.stream()
                .map(accountAmountDO -> accountAmountDO.getAccountAmountType().getAccountBalance())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        //判断子账户之和
        boolean accountTypesEnough = allTypeBalance.subtract(amount).compareTo(BigDecimal.ZERO) >= 0;
        Assert.isTrue(accountTypesEnough, "用户子账户余额总和不足,请确认员工账户总账和子账是否对应");
        accountAmountDOList.sort(Comparator.comparing(x -> x.getMerchantAccountType().getDeductionOrder()));
        List<PaymentOperation> paymentOperations = new ArrayList<>(4);
        List<AccountAmountType> accountAmountTypes = accountAmountDOList.stream().map(AccountAmountDO::getAccountAmountType)
                .collect(Collectors.toList());
        for (AccountAmountDO accountAmountDO : accountAmountDOList) {
            if (BigDecimal.ZERO.compareTo(accountAmountDO.getAccountAmountType().getAccountBalance()) == 0) {
                //当前的accountType没钱，则继续下一个账户
                continue;
            }
            PaymentOperation currentOperation = decrease(accountAmountDO, amount, paymentRequest, accountAmountTypes, supplierStore, merchantCredit);
            amount = amount.subtract(currentOperation.getOperateAmount());
            paymentOperations.add(currentOperation);
            if (currentOperation.isEnough()) {
                break;
            }
        }
        return paymentOperations;
    }


    private PaymentOperation decrease(AccountAmountDO accountAmountDO,
                                      BigDecimal toOperateAmount,
                                      PaymentRequest paymentRequest,
                                      List<AccountAmountType> accountAmountTypes, SupplierStore supplierStore, MerchantCredit merchantCredit) {
        AccountAmountType accountAmountType = accountAmountDO.getAccountAmountType();
        MerchantAccountType merchantAccountType = accountAmountDO.getMerchantAccountType();
        BigDecimal accountBalance = accountAmountType.getAccountBalance();
        BigDecimal subtract = accountBalance.subtract(toOperateAmount);

        BigDecimal operatedAmount;
        /**
         * 扣减个人账户
         */
        boolean isCurrentEnough = subtract.compareTo(BigDecimal.ZERO) >= 0;
        if (isCurrentEnough) {
            operatedAmount = toOperateAmount;
            accountAmountType.setAccountBalance(subtract);
        } else {
            operatedAmount = accountBalance;
            accountAmountType.setAccountBalance(BigDecimal.ZERO);
        }
        PaymentOperation paymentOperation = new PaymentOperation();
        paymentOperation.setOperateAmount(operatedAmount);
        paymentOperation.setAccountAmountType(accountAmountType);
        paymentOperation.setMerchantAccountType(merchantAccountType);
        paymentOperation.setTransNo(paymentRequest.getTransNo());
        AccountBillDetail accountBillDetail = AccountAmountDO.generateAccountBillDetail(paymentRequest, operatedAmount, accountAmountTypes);
        paymentOperation.setAccountBillDetail(accountBillDetail);
        paymentOperation.setEnough(isCurrentEnough);
        /**
         * 扣减商户账户
         */
        AccountDeductionDetail accountDeductionDetail = AccountAmountDO.decreaseMerchant(
                paymentRequest,
                accountAmountType,
                operatedAmount,
                paymentOperation,
                accountAmountDO.getAccount(),
                supplierStore,
                merchantCredit,
                currentBalanceOperator
        );
        paymentOperation.setAccountDeductionDetail(accountDeductionDetail);
        return paymentOperation;

    }


}
