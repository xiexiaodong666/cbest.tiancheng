package com.welfare.service.remote.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class CreateWXH5TradeNotifyResp {

    @JSONField(name = "trade_no")
    private String tradeNo;
    @JSONField(name = "gateway_trade_no")
    private String gatewayTradeNo;

    @JSONField(name = "total_amount")
    private String totalAmount;

    @JSONField(name = "receipt_amount")
    private String receiptAmount;

    @JSONField(name = "actual_amount")
    private String actualAmount;

    @JSONField(name = "total_discount_amount")
    private String totalDiscountAmount;

    @JSONField(name = "channel_discount_amount")
    private String channelDiscountAmount;

    @JSONField(name = "merchant_discount_amount")
    private String merchantDiscountAmount;

    @JSONField(name = "pay_detail")
    private String payDetail;
}
