package com.welfare.service.impl;

import com.welfare.persist.dto.ThirdPartyPaymentRequestDao;
import com.welfare.persist.entity.ThirdPartyPaymentRequest;
import com.welfare.service.ThirdPartyPaymentRequestService;
import com.welfare.service.dto.payment.BarcodePaymentRequest;
import com.welfare.service.dto.payment.PaymentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 3/17/2021
 */
@RequiredArgsConstructor
@Service
public class ThirdPartyPaymentRequestServiceImpl implements ThirdPartyPaymentRequestService {
    private final ThirdPartyPaymentRequestDao thirdPartyPaymentRequestDao;
    @Override
    public boolean chargeWhetherHandled(PaymentRequest paymentRequest) {
        String transNo = paymentRequest.getTransNo();
        ThirdPartyPaymentRequest thirdPartyPaymentRequestInDb = null;
        thirdPartyPaymentRequestInDb = thirdPartyPaymentRequestDao.queryByTransNo(paymentRequest.getTransNo());
        if(paymentRequest instanceof BarcodePaymentRequest){
            thirdPartyPaymentRequestInDb = thirdPartyPaymentRequestDao.queryByBarcode(((BarcodePaymentRequest) paymentRequest).getBarcode());
        }
        if(Objects.isNull(thirdPartyPaymentRequestInDb)){
            return false;
        }

        return true;
    }
}
