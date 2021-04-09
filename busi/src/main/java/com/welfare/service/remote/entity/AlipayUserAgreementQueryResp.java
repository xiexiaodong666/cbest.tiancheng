package com.welfare.service.remote.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class AlipayUserAgreementQueryResp {

    /**
     * 用户代扣协议的实际生效时间，格式为yyyy-MM-dd HH:mm:ss
     */
    @JSONField(name = "valid_time")
    private String validTime;

    /**
     * 用户代扣协议的失效时间，格式为yyyy-MM-dd HH:mm:ss
     */
    @JSONField(name = "invalid_time")
    private String invalidTime;

    /**
     * 用户签约的支付宝账号对应的支付宝唯一用户号。以2088开头的16位纯数字组成
     */
    @JSONField(name = "alipay_user_id")
    private String alipayUserId;

    /**
     * 用户在商户网站的登录账号，如果商户接口中未传，则不会返回
     */
    @JSONField(name = "external_logon_id")
    private String externalLogonId;

    /**
     * 支付宝系统中用以唯一标识用户签约记录的编号
     */
    @JSONField(name = "agreement_no")
    private String agreementNo;

    /**
     * 代扣协议中标示用户的唯一签约号（确保在商户系统中唯一），如果商户接口中未传，则不会返回
     */
    @JSONField(name = "external_agreement_no")
    private String externalAgreementNo;

    /**
     * 协议的当前状态。1. TEMP：暂存，协议未生效过；2. NORMAL：正常；3. STOP：暂停
     */
    @JSONField(name = "status")
    private String status;

}
