package com.welfare.service.payment;

import com.welfare.persist.entity.Account;
import com.welfare.persist.entity.AccountDeductionDetail;
import com.welfare.persist.entity.MerchantCredit;
import com.welfare.persist.entity.SupplierStore;
import com.welfare.service.dto.RefundRequest;
import com.welfare.service.dto.payment.PaymentRequest;
import com.welfare.service.operator.merchant.domain.MerchantAccountOperation;
import com.welfare.service.operator.payment.domain.AccountAmountDO;
import com.welfare.service.operator.payment.domain.PaymentOperation;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 3/26/2021
 */
@Component
public class AliPayPaymentOperator implements IPaymentOperator,IRefundOperator{
    @Override
    public List<PaymentOperation> pay(PaymentRequest paymentRequest, Account account, List<AccountAmountDO> usableAccountAmountDOList, List<AccountAmountDO> allAccountAmountDOList, SupplierStore supplierStore, MerchantCredit merchantCredit) {
        PaymentOperation paymentOperation = doPay(paymentRequest, account, usableAccountAmountDOList, allAccountAmountDOList, supplierStore, merchantCredit, null);
        return Collections.singletonList(paymentOperation);
    }


    @Override
    public void refund(RefundRequest refundRequest, List<AccountDeductionDetail> refundDeductionInDbs, List<AccountDeductionDetail> paidDeductionDetails, Long accountCode) {
        doRefund(refundRequest,paidDeductionDetails,accountCode);
    }


    @Override
    public List<MerchantAccountOperation> operateMerchantRefund(RefundRequest refundRequest, Account account){
        //支付宝支付不用退款商户,所以没有任何实现
        return null;
    }
}
