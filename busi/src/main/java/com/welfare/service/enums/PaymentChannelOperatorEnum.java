package com.welfare.service.enums;

import com.welfare.common.constants.WelfareConstant;
import com.welfare.service.payment.*;

import java.util.Arrays;

/**
 * Description: 支付渠道到与操作类映射
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 3/26/2021
 */
public enum PaymentChannelOperatorEnum {
    /**
     * 支付渠道与操作类映射表
     */
    WO_LIFE(WelfareConstant.PaymentChannel.WO_LIFE, WoLifePaymentOperator.class, WoLifePaymentOperator.class),
    WELFARE(WelfareConstant.PaymentChannel.WELFARE, WelfarePaymentOperator.class,WelfareRefundOperator.class),
    ALI_PAY(WelfareConstant.PaymentChannel.ALIPAY, AliPayPaymentOperator.class,AliPayPaymentOperator.class),
    WE_CHAT(WelfareConstant.PaymentChannel.WECHAT,WeChatPaymentOperator.class,WeChatPaymentOperator.class);


    private final WelfareConstant.PaymentChannel paymentChannel;
    private final Class<? extends IPaymentOperator> paymentOperator;
    private final Class<? extends IRefundOperator> refundOperator;

    PaymentChannelOperatorEnum(WelfareConstant.PaymentChannel paymentChannel,
                               Class<? extends IPaymentOperator> paymentOperator,
                               Class<? extends IRefundOperator> refundOperator){
        this.paymentChannel = paymentChannel;
        this.paymentOperator = paymentOperator;
        this.refundOperator = refundOperator;
    }

    public Class<? extends IPaymentOperator> paymentOperator(){
        return this.paymentOperator;
    }

    public Class<? extends IRefundOperator> refundOperator(){
        return this.refundOperator;
    }
    public WelfareConstant.PaymentChannel paymentChannel(){
        return this.paymentChannel;
    }

    public static PaymentChannelOperatorEnum findByPaymentChannel(WelfareConstant.PaymentChannel paymentChannel){
        PaymentChannelOperatorEnum[] values = PaymentChannelOperatorEnum.values();
        return Arrays.stream(values)
                .filter(operator -> operator.paymentChannel == paymentChannel)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("不支持的支付渠道" + paymentChannel.code()));
    }

    public static PaymentChannelOperatorEnum findByPaymentChannelStr(String paymentChannelStr){
        WelfareConstant.PaymentChannel paymentChannel = WelfareConstant.PaymentChannel.findByCode(paymentChannelStr);
        return findByPaymentChannel(paymentChannel);
    }

}
