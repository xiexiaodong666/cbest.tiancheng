package com.welfare.serviceaccount.controller;

import com.alibaba.fastjson.JSON;
import com.welfare.service.remote.WoLifeFeignClient;
import com.welfare.service.remote.entity.request.WoLifeAccountDeductionRequest;
import com.welfare.service.remote.entity.request.WoLifeGetUserMoneyRequest;
import com.welfare.service.remote.entity.request.WoLifeRefundWriteOffRequest;
import com.welfare.service.remote.entity.response.WoLifeAccountDeductionResponse;
import com.welfare.service.remote.entity.response.WoLifeBasicResponse;
import com.welfare.service.remote.entity.response.WoLifeGetUserMoneyResponse;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.common.support.IController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/3/12 2:58 PM
 */
@Deprecated
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/WoLife/test")
@Api(tags = "沃支付联调测试, 上线后删除")
public class WoLifeTest implements IController {

  @Autowired(required = false)
  WoLifeFeignClient woLifeFeignClient;

  /**
   * 账户余额查询
   */
  @RequestMapping(value = "/getUserMoney", method = RequestMethod.POST)
  WoLifeBasicResponse<WoLifeGetUserMoneyResponse> getUserMoney(
      String phone) {

    log.info("getUserMoney", phone);
    return woLifeFeignClient.getUserMoney(phone);
  }

  /**
   * 账户扣款
   */
  @RequestMapping(value = "/accountDeduction", method = RequestMethod.POST)
  WoLifeBasicResponse<WoLifeAccountDeductionResponse> accountDeduction(
      @RequestBody WoLifeAccountDeductionRequest request) {

    log.info("accountDeduction", JSON.toJSONString(request));
    return woLifeFeignClient.accountDeduction(request);
  }

  /**
   * 退款销账
   */
  @RequestMapping(value = "/refundWriteOff", method = RequestMethod.POST)
  WoLifeBasicResponse refundWriteOff(
      @RequestBody WoLifeRefundWriteOffRequest request) {
    log.info("refundWriteOff", JSON.toJSONString(request));
    return woLifeFeignClient.refundWriteOff(request);
  }


}
