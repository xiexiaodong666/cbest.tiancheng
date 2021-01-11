package com.welfare.service.remote.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class CbestPayBaseBizResp {
    @JSONField(name = "biz_status")
    private String bizStatus;
    @JSONField(name = "biz_msg")
    private String bizMsg;
    @JSONField(name = "biz_content")
    private String bizContent;
}
