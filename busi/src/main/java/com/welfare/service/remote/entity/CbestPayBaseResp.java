package com.welfare.service.remote.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CbestPayBaseResp {
    private String status;
    private String msg;
    @JsonProperty("biz_status")
    @JSONField(name = "biz_status")
    private String bizStatus;
    @JsonProperty("biz_msg")
    @JSONField(name = "biz_msg")
    private String bizMsg;
    @JsonProperty("biz_content")
    @JSONField(name = "biz_content")
    private String bizContent;
    private String sign;
}
