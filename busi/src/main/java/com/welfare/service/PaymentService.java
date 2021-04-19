package com.welfare.service;

import com.welfare.service.dto.ThirdPartyBarcodePaymentDTO;
import com.welfare.service.dto.payment.BarcodePaymentRequest;
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
     * @param paymentRequest 付款请求
     * @return 被执行的付款请求及结果填充
     */
    <T extends PaymentRequest>T paymentRequest(T paymentRequest);

    /**
     * 查询支付结果
     * @param transNo 交易单号
     * @param clazz 什么类型的PaymentRequest
     * @return 查询出的交易结果
     */
    <T extends PaymentRequest> T queryResult(String transNo,Class<T> clazz);

    /**
     * 第三方支付码（微信或支付宝）场景校验，并且返回免密支付签名
     * @param paymentRequest 条码支付请求，用于查询
     * @return 免密支付签名
     */
    ThirdPartyBarcodePaymentDTO thirdPartyBarcodePaymentSceneCheck(
        BarcodePaymentRequest paymentRequest);
}
