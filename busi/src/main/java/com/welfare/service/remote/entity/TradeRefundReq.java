package com.welfare.service.remote.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class TradeRefundReq {

    @JSONField(name = "trade_no")
    private String tradeNo;

    @JSONField(name = "gateway_trade_no")
    private String gatewayTradeNo;

    @JSONField(name = "out_refund_no")
    private String outRefundNo;

    @JSONField(name = "gateway_trade_no")
    private String cardNo;

    @JSONField(name = "refund_amount")
    private Integer refundAmount;

    private String terminal;

    private String operator;

    private String userdata;
}
