package com.welfare.service.remote.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class AlipayUserAgreementPageSignResp {

    @JSONField(name = "sign_page")
    private String signPage;
}
