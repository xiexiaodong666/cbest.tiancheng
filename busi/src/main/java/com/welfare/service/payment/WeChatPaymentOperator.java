package com.welfare.service.payment;

import com.welfare.persist.entity.Account;
import com.welfare.persist.entity.AccountDeductionDetail;
import com.welfare.persist.entity.MerchantCredit;
import com.welfare.persist.entity.SupplierStore;
import com.welfare.service.dto.RefundRequest;
import com.welfare.service.dto.payment.PaymentRequest;
import com.welfare.service.operator.payment.domain.AccountAmountDO;
import com.welfare.service.operator.payment.domain.PaymentOperation;

import java.util.List;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 3/26/2021
 */
public class WeChatPaymentOperator implements IPaymentOperator,IRefundOperator{
    @Override
    public List<PaymentOperation> pay(PaymentRequest paymentRequest, Account account, List<AccountAmountDO> accountAmountDOList, SupplierStore supplierStore, MerchantCredit merchantCredit) {
        return null;
    }


    @Override
    public void refund(RefundRequest refundRequest, List<AccountDeductionDetail> refundDeductionInDbs, List<AccountDeductionDetail> paidDeductionDetails, Long accountCode) {

    }
}
