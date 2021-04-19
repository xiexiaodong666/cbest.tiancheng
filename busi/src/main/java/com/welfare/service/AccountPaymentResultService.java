package com.welfare.service;

import com.welfare.service.dto.BarcodePaymentNotifyReq;
import com.welfare.service.dto.BarcodePaymentResultDTO;
import com.welfare.service.dto.BarcodePaymentResultReq;
import com.welfare.service.dto.CreateThirdPartyPaymentDTO;
import com.welfare.service.dto.CreateThirdPartyPaymentReq;
import com.welfare.service.remote.entity.AlipayUserAgreementQueryResp;
import com.welfare.service.remote.entity.CbestPayBaseResp;

public interface AccountPaymentResultService {

    void barcodePaymentNotify(BarcodePaymentNotifyReq req);

    BarcodePaymentResultDTO queryBarcodePaymentResult(BarcodePaymentResultReq req);

    /**
     * 第三方支付结果通知
     * @param resp
     */
    void thirdPartyPaymentResultNotify(CbestPayBaseResp resp);

    /**
     * 第三方交易创建通知
     * @param resp
     */
    void createThirdPartyPaymentNotify(CbestPayBaseResp resp);

    /**
     * 第三方交易创建
     * @param req
     */
    CreateThirdPartyPaymentDTO createThirdPartyPayment(CreateThirdPartyPaymentReq req);

    /**
     * 签约或解约结果通知
     * @param resp
     */
    void thirdPartySignResultNotify(CbestPayBaseResp resp);
}
