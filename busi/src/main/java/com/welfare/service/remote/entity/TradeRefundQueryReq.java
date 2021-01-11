package com.welfare.service.remote.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class TradeRefundQueryReq {

    @JSONField(name = "trade_no")
    private String tradeNo;

    @JSONField(name = "gateway_trade_no")
    private String gatewayTradeNo;

    @JSONField(name = "out_refund_no")
    private String outRefundNo;

    private String userdata;
}
