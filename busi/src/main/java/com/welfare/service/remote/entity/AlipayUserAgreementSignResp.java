package com.welfare.service.remote.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class AlipayUserAgreementSignResp {

    /**
     * 支付宝签约参数。小程序或JSAPI签约需要使用此参数
     */
    @JSONField(name = "sign_params")
    private String signParams;

    /**
     * 完整的签约URL，建议通过APP签约时使用此参数
     */
    @JSONField(name = "sign_url")
    private String signUrl;
}
