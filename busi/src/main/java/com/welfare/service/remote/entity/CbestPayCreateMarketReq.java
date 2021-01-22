package com.welfare.service.remote.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class CbestPayCreateMarketReq {
    //门店名称
    @JSONField(name = "market_name")
    private String marketName;
}
