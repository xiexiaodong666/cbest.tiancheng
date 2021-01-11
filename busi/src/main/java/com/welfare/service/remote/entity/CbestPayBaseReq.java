package com.welfare.service.remote.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class CbestPayBaseReq {
    @JSONField(name = "app_id")
    private String appId;
    private String market;
    private String method;
    private String version;
    private String format;
    private String charset;
    @JSONField(name = "sign_type")
    private String signType;
    private String timestamp;
    @JSONField(name = "biz_content")
    private String bizContent;
    private String sign;
}
