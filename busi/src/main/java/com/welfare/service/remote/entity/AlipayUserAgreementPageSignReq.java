package com.welfare.service.remote.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class AlipayUserAgreementPageSignReq {

    /**
     * 用户在重百体系的帐号
     */
    @JSONField(name = "external_logon_id")
    private String externalLogonId;

    /**
     * 重百签约号
     */
    @JSONField(name = "external_agreement_no")
    private String externalAgreementNo;

    /**
     * 跳转商户处理url。应用场景：商户需要在签约流程中跳转到商户自己的页面做处理的场景，如获得用户授权获取实名信息等 如何获取：商户自己提供的页面地址
     * 特殊说明：商户如果传递此参数，则会在签约流程中跳转所传递的地址，不传则不会跳转
     */
    @JSONField(name = "merchant_process_url")
    private String merchantProcessUrl;
}
