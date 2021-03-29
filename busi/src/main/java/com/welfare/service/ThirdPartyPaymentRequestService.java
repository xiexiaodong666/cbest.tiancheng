package com.welfare.service;

import com.welfare.common.constants.WelfareConstant;
import com.welfare.persist.entity.ThirdPartyPaymentRequest;
import com.welfare.service.dto.RefundRequest;
import com.welfare.service.dto.payment.PaymentRequest;
import com.welfare.service.remote.entity.response.WoLifeAccountDeductionResponse;
import com.welfare.service.remote.entity.response.WoLifeBasicResponse;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 3/17/2021
 */
public interface ThirdPartyPaymentRequestService {
    /**
     * 判断是否已经扣款过
     * @param paymentRequest
     * @return
     */
    boolean chargeWhetherHandled(PaymentRequest paymentRequest);

    /**
     * 生成支付中，并且保存
     * @param paymentRequest
     * @return
     */
    ThirdPartyPaymentRequest generateHandling(PaymentRequest paymentRequest);

    /**
     * 生成退款中，并且保存
     * @param refundRequest
     * @return
     */
    ThirdPartyPaymentRequest generateRefundHandling(RefundRequest refundRequest);

    /**
     * 更新结果
     * @param thirdPartyPaymentRequest
     * @param asyncStatus
     * @param woLifeBasicResponse
     * @return
     */
    ThirdPartyPaymentRequest updateResult(ThirdPartyPaymentRequest thirdPartyPaymentRequest,
                                          WelfareConstant.AsyncStatus asyncStatus,
                                          WoLifeBasicResponse woLifeBasicResponse,String failedMsg);

}
