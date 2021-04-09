package com.welfare.service.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

@Data
public class CreateThirdPartyPaymentNotifyReq implements Serializable {

    private String barcode;

    @JSONField(name = "trade_no")
    private String tradeNo;

    @JSONField(name = "gateway_trade_no")
    private String gatewayTradeNo;

    private String market;

    private String amount;

    @JSONField(name = "order_string")
    private String orderString;
}
