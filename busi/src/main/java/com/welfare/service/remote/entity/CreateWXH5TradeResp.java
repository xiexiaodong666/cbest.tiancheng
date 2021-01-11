package com.welfare.service.remote.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class CreateWXH5TradeResp {

    @JSONField(name = "trade_no")
    private String tradeNo;

    @JSONField(name = "gateway_trade_no")
    private String gatewayTradeNo;

    private String userdata;

    @JSONField(name = "channel_trade_no")
    private String channelTradeNo;

    @JSONField(name = "h5_url")
    private String h5Url;
}
