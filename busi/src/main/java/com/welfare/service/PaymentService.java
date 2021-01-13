package com.welfare.service;

import com.welfare.service.dto.payment.OnlinePaymentRequest;
import com.welfare.service.dto.payment.PaymentRequest;
import com.welfare.service.operator.payment.domain.PaymentOperation;

import java.math.BigDecimal;
import java.util.List;

/**
 * Description: 扣款相关service
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 1/8/2021
 */
public interface PaymentService {


    /**
     * 处理支付请求
     * @param paymentRequest
     */
    List<PaymentOperation> handlePayRequest(PaymentRequest paymentRequest);
}
