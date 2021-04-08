package com.welfare.service.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class ThirdPartyPaymentResultNotifyReq {
    @JSONField(name = "trade_no")
    private String tradeNo;

    @JSONField(name = "gateway_trade_no")
    private String gatewayTradeNo;

    @JSONField(name = "buyer_id")
    private String buyerId;

    /**
     * 收银机编号
     */
    private String terminal;

    @JSONField(name = "total_amount")
    private String totalAmount;

    @JSONField(name = "actual_amount")
    private String actualAmount;

    @JSONField(name = "total_discount_amount")
    private String totalDiscountAmount;

    @JSONField(name = "pay_detail")
    private String payDetail;

    private String market;

    private String barcode;
    private String accountCode;
}
