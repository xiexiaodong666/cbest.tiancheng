package com.welfare.service.operator.payment;

import com.welfare.service.dto.payment.PaymentRequest;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 1/12/2021
 */
public abstract class AbstractPaymentOperator {
    private AbstractPaymentOperator next;

    public AbstractPaymentOperator getNext(){
        return next;
    }

    public void next(AbstractPaymentOperator paymentOperator){
        this.next = paymentOperator;
    }

    /**
     * 支付
     * @param paymentRequest
     */
    public  abstract void doPay(PaymentRequest paymentRequest);


    public void pay(PaymentRequest paymentRequest){
        paymentRequest.calculateAccountCode();
        doPay(paymentRequest);
    }

}
