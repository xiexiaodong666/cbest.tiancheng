package com.welfare.service.payment;

import com.welfare.persist.entity.Account;
import com.welfare.persist.entity.MerchantCredit;
import com.welfare.persist.entity.SupplierStore;
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
}
