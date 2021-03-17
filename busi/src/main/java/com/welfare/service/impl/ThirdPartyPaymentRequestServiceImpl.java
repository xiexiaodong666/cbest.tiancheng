package com.welfare.service.impl;

import com.welfare.common.constants.WelfareConstant;
import com.welfare.persist.dto.ThirdPartyPaymentRequestDao;
import com.welfare.persist.entity.ThirdPartyPaymentRequest;
import com.welfare.service.ThirdPartyPaymentRequestService;
import com.welfare.service.dto.payment.BarcodePaymentRequest;
import com.welfare.service.dto.payment.PaymentRequest;
import com.welfare.service.remote.entity.response.WoLifeBasicResponse;
import com.welfare.service.remote.service.WoLifeFeignService;
import com.welfare.service.wolife.WoLifePaymentService;
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
    private final WoLifeFeignService woLifeFeignService;
    @Override
    @Deprecated
    public boolean chargeWhetherHandled(PaymentRequest paymentRequest) {
        ThirdPartyPaymentRequest thirdPartyPaymentRequestInDb = null;
        thirdPartyPaymentRequestInDb = thirdPartyPaymentRequestDao.queryByTransNo(paymentRequest.getTransNo());
        if(paymentRequest instanceof BarcodePaymentRequest){
            thirdPartyPaymentRequestInDb = thirdPartyPaymentRequestDao.queryByBarcode(((BarcodePaymentRequest) paymentRequest).getBarcode());
        }
        if(Objects.isNull(thirdPartyPaymentRequestInDb)){
            return false;
        }

        paymentRequest.setPaymentStatus(WelfareConstant.AsyncStatus.SUCCEED.code());
        thirdPartyPaymentRequestInDb.setTransStatus(WelfareConstant.AsyncStatus.SUCCEED.code());

        thirdPartyPaymentRequestDao.updateById(thirdPartyPaymentRequestInDb);
        return true;
    }
}
