package com.welfare.service.impl;

import com.alibaba.fastjson.JSON;
import com.welfare.common.constants.WelfareConstant;
import com.welfare.common.enums.PaymentTypeEnum;
import com.welfare.persist.dto.ThirdPartyPaymentRequestDao;
import com.welfare.persist.entity.ThirdPartyPaymentRequest;
import com.welfare.service.ThirdPartyPaymentRequestService;
import com.welfare.service.dto.RefundRequest;
import com.welfare.service.dto.payment.BarcodePaymentRequest;
import com.welfare.service.dto.payment.PaymentRequest;
import com.welfare.service.remote.entity.response.WoLifeAccountDeductionResponse;
import com.welfare.service.remote.entity.response.WoLifeBasicResponse;
import com.welfare.service.remote.service.WoLifeFeignService;
import com.welfare.service.wolife.WoLifePaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    //开启新事务，确保保存成功
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRES_NEW)
    /**
     *
     */
    public ThirdPartyPaymentRequest generateHandling(PaymentRequest paymentRequest) {
        ThirdPartyPaymentRequest thirdPartyPaymentRequest = generateThirdPartyPaymentRequest(paymentRequest);
        thirdPartyPaymentRequest.setTransStatus(WelfareConstant.AsyncStatus.HANDLING.code());
        thirdPartyPaymentRequestDao.save(thirdPartyPaymentRequest);
        return thirdPartyPaymentRequest;
    }

    private ThirdPartyPaymentRequest generateThirdPartyPaymentRequest(PaymentRequest paymentRequest) {
        ThirdPartyPaymentRequest thirdPartyPaymentRequest = new ThirdPartyPaymentRequest();
        thirdPartyPaymentRequest.setPaymentRequest(JSON.toJSONString(paymentRequest));
        thirdPartyPaymentRequest.setTransStatus(WelfareConstant.AsyncStatus.HANDLING.code());
        thirdPartyPaymentRequest.setPaymentType(PaymentTypeEnum.BARCODE.getCode());
        thirdPartyPaymentRequest.setAccountCode(paymentRequest.getAccountCode());
        thirdPartyPaymentRequest.setTransType(WelfareConstant.TransType.CONSUME.code());
        thirdPartyPaymentRequest.setPaymentTypeInfo(((BarcodePaymentRequest) paymentRequest).getBarcode());
        thirdPartyPaymentRequest.setTransNo(paymentRequest.getTransNo());
        thirdPartyPaymentRequest.setTransAmount(paymentRequest.getAmount());
        thirdPartyPaymentRequest.setPaymentChannel(WelfareConstant.PaymentChannel.WO_LIFE.code());
        return thirdPartyPaymentRequest;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public ThirdPartyPaymentRequest updateResult(ThirdPartyPaymentRequest thirdPartyPaymentRequest,
                                                 WelfareConstant.AsyncStatus asyncStatus,
                                                 WoLifeBasicResponse woLifeBasicResponse, String failedMsg){
        if(Objects.isNull(woLifeBasicResponse)){
            //接口没有返回，系统异常
            thirdPartyPaymentRequest.setResponse(failedMsg);
        }else{
            //接口有返回，业务返回
            thirdPartyPaymentRequest.setResponse(JSON.toJSONString(woLifeBasicResponse));
        }
        thirdPartyPaymentRequest.setTransStatus(asyncStatus.code());
        thirdPartyPaymentRequestDao.updateById(thirdPartyPaymentRequest);
        return thirdPartyPaymentRequest;
    }

    private ThirdPartyPaymentRequest refundThirdPartyPaymentRequest(RefundRequest refundRequest){
        ThirdPartyPaymentRequest thirdPartyPaymentRequest = thirdPartyPaymentRequestDao.queryByTransNo(refundRequest.getOriginalTransNo());
        ThirdPartyPaymentRequest refundThirdPartyPaymentRequest = new ThirdPartyPaymentRequest();
        BeanUtils.copyProperties(thirdPartyPaymentRequest,refundThirdPartyPaymentRequest);
        refundThirdPartyPaymentRequest.setTransStatus(WelfareConstant.AsyncStatus.HANDLING.code());
        refundThirdPartyPaymentRequest.setPaymentRequest(JSON.toJSONString(refundRequest));
        refundThirdPartyPaymentRequest.setId(null);
        refundThirdPartyPaymentRequest.setTransType(WelfareConstant.TransType.REFUND.code());
        return refundThirdPartyPaymentRequest;
    }

    @Override
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRES_NEW)
    public ThirdPartyPaymentRequest generateRefundHandling(RefundRequest refundRequest){
        ThirdPartyPaymentRequest thirdPartyPaymentRequest = refundThirdPartyPaymentRequest(refundRequest);
        thirdPartyPaymentRequestDao.save(thirdPartyPaymentRequest);
        return thirdPartyPaymentRequest;
    }
}
