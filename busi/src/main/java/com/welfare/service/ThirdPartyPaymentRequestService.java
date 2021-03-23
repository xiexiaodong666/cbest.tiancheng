package com.welfare.service;

import com.welfare.service.dto.payment.PaymentRequest;

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
}
