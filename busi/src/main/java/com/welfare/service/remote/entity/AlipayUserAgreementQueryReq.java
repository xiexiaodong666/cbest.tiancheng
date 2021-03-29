package com.welfare.service.remote.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class AlipayUserAgreementQueryReq {
    /**
     * 代扣协议中标示用户的唯一签约号(确保在商户系统中唯一)。格式规则:支持大写小写字母和数字，最长 32 位
     */
    @JSONField(name = "external_agreement_no")
    private String externalAgreementNo;

    /**
     * 支付宝系统中用以唯一标识用户签约记录的编号（用户签约成功后的协议号 ），如果传了该参数，其他参数会被忽略
     */
    @JSONField(name = "agreement_no")
    private String agreementNo;

    /**
     * 用户的支付宝账号对应 的支付宝唯一用户号，以 2088 开头的 16 位纯数字 组成
     */
    @JSONField(name = "alipay_user_id")
    private String alipayUserId;
}
