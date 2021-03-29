package com.welfare.service.remote.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class AlipayUserAgreementUnsignReq {

    /**
     * 支付宝应用ID，默认值：2021002131699285
     */
    @JSONField(name = "alipay_app_id")
    private String alipayAppId;

    /**
     * 支付宝签约号
     */
    @JSONField(name = "agreement_no")
    private String agreementNo;
}
