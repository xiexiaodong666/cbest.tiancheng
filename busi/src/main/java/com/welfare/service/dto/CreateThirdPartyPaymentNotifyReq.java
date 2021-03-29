package com.welfare.service.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class CreateThirdPartyPaymentNotifyReq {

    private String barcode;

    @JSONField(name = "trade_no")
    private String tradeNo;

    @JSONField(name = "gateway_trade_no")
    private String gatewayTradeNo;

    private String market;

    @JSONField(name = "order_string")
    private String orderString;
}
