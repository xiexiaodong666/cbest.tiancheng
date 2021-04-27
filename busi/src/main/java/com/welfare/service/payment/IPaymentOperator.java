package com.welfare.service.payment;

import com.welfare.persist.entity.*;
import com.welfare.service.dto.payment.PaymentRequest;
import com.welfare.service.operator.merchant.AbstractMerAccountTypeOperator;
import com.welfare.service.operator.payment.domain.AccountAmountDO;
import com.welfare.service.operator.payment.domain.PaymentOperation;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 3/26/2021
 */
public interface IPaymentOperator {
    /**
     * 付款
     * @param paymentRequest
     * @param account
     * @param accountAmountDOList
     * @param supplierStore
     * @param merchantCredit
     * @return
     */
    default List<PaymentOperation> pay(PaymentRequest paymentRequest,
                               Account account,
                               List<AccountAmountDO> accountAmountDOList,
                               SupplierStore supplierStore,
                               MerchantCredit merchantCredit){
        throw new RuntimeException("method not supported");
    }

    /**
     * 实际执行支付流程
     * @param paymentRequest
     * @param account
     * @param accountAmountDOList
     * @param supplierStore
     * @param merchantCredit
     * @param merAccountTypeOperator
     * @return
     */
    default PaymentOperation doPay(PaymentRequest paymentRequest,
                                     Account account,
                                     List<AccountAmountDO> accountAmountDOList,
                                     SupplierStore supplierStore,
                                     MerchantCredit merchantCredit,
                                     AbstractMerAccountTypeOperator merAccountTypeOperator) {
        paymentRequest.setPhone(account.getPhone());
        List<AccountAmountType> accountAmountTypes = accountAmountDOList.stream().map(AccountAmountDO::getAccountAmountType)
                .collect(Collectors.toList());
        PaymentOperation paymentOperation = new PaymentOperation();
        BigDecimal paymentAmount = paymentRequest.getAmount();
        AccountBillDetail accountBillDetail = AccountAmountDO.generateAccountBillDetail(paymentRequest, paymentAmount, accountAmountTypes, null);
        paymentOperation.setAccountBillDetail(accountBillDetail);
        paymentOperation.setTransNo(paymentRequest.getTransNo());
        AccountDeductionDetail accountDeductionDetail = AccountAmountDO.generateAccountDeductionDetail(paymentRequest,
                null,
                paymentAmount,
                paymentOperation,
                account,
                supplierStore,
                merchantCredit,
                merAccountTypeOperator, null);
        paymentOperation.setAccountDeductionDetail(accountDeductionDetail);
        paymentOperation.setOperateAmount(paymentAmount);
        paymentOperation.setMerchantAccountType(null);
        return paymentOperation;
    }
}
