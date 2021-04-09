package com.welfare.service.remote.service.impl;

import com.alibaba.fastjson.JSON;
import com.welfare.service.remote.WoLifeFeignClient;
import com.welfare.service.remote.entity.request.WoLifeAccountDeductionDataRequest;
import com.welfare.service.remote.entity.request.WoLifeRefundWriteOffDataRequest;
import com.welfare.service.remote.entity.response.WoLifeAccountDeductionResponse;
import com.welfare.service.remote.entity.response.WoLifeBasicResponse;
import com.welfare.service.remote.entity.response.WoLifeGetUserMoneyResponse;
import com.welfare.service.remote.service.WoLifeFeignService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/3/16 5:20 PM
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WoLifeFeignServiceImpl implements WoLifeFeignService {

  @Autowired(required = false)
  WoLifeFeignClient woLifeFeignClient;

  @Override
  public WoLifeBasicResponse<WoLifeGetUserMoneyResponse> getUserMoney(String phone) {
    log.info("沃生活馆账户余额查询, 请求:{}", phone);
    WoLifeBasicResponse<WoLifeGetUserMoneyResponse> response = woLifeFeignClient.getUserMoney(
        phone);
    log.info("沃生活馆账户余额查询, 返回:{}", response);

    return response;
  }

  @Override
  public WoLifeBasicResponse<WoLifeAccountDeductionResponse> accountDeduction(String phone,
      WoLifeAccountDeductionDataRequest data) {
    log.info("沃生活馆账户扣款, 请求:{},{}", phone, data);

    WoLifeBasicResponse<WoLifeAccountDeductionResponse> response = woLifeFeignClient.accountDeduction(phone, JSON.toJSONString(data));
    log.info("沃生活馆账户扣款, 返回:{}", response);

    return response;
  }

  @Override
  public WoLifeBasicResponse refundWriteOff(String phone, WoLifeRefundWriteOffDataRequest data) {
    log.info("沃生活馆账户退款, 请求:{},{}", phone, data);
    WoLifeBasicResponse response = woLifeFeignClient.refundWriteOff(phone, JSON.toJSONString(data));
    log.info("沃生活馆账户退款, 返回:{}", response);

    return response;
  }

/*  @Override
  public WoLifeBasicResponse queryDeduction(PaymentRequest paymentRequest){
    return woLifeFeignClient.getAccountDeduction(WoLifeGetAccountDeductionRequest.of(paymentRequest));
  }*/
}
