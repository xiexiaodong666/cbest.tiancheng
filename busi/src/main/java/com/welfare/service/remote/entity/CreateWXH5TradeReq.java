package com.welfare.service.remote.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class CreateWXH5TradeReq {

    @JSONField(name = "trade_no")
    private String tradeNo;

    private Integer amount;

    @JSONField(name = "notify_url")
    private String notifyUrl;

    private String appid;
}
