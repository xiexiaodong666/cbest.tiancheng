package com.welfare.service;

import com.welfare.service.dto.payment.PaymentRequest;

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
     * @return
     */
    PaymentRequest paymentRequest(PaymentRequest paymentRequest);

    /**
     * 查询支付结果
     * @param transNo
     * @return
     */
    PaymentRequest queryResult(String transNo);
}
