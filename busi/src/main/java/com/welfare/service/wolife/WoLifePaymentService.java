package com.welfare.service.wolife;

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
 * @date 3/15/2021
 */
public interface WoLifePaymentService {
    /**
     * 联通沃生活馆支付
     * @param paymentRequest
     * @param account
     * @param accountAmountDOList
     * @param merchantCredit
     * @param supplierStore
     * @return
     */
    List<PaymentOperation> pay(PaymentRequest paymentRequest, Account account, List<AccountAmountDO> accountAmountDOList, MerchantCredit merchantCredit, SupplierStore supplierStore);

    /**
     * 沃生活馆退款
     * @param refundRequest
     * @param accountDeductionDetails
     * @param accountCode
     */
    void refund(RefundRequest refundRequest, List<AccountDeductionDetail> accountDeductionDetails, Long accountCode);
}
