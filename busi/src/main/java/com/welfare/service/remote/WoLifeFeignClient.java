package com.welfare.service.remote;

import com.welfare.common.annotation.ConditionalOnHavingProperty;
import com.welfare.service.remote.config.FeignConfiguration;
import com.welfare.service.remote.entity.request.WoLifeAccountDeductionRequest;
import com.welfare.service.remote.entity.request.WoLifeRefundWriteOffRequest;
import com.welfare.service.remote.entity.response.WoLifeAccountDeductionResponse;
import com.welfare.service.remote.entity.response.WoLifeBasicResponse;
import com.welfare.service.remote.entity.response.WoLifeGetUserMoneyResponse;
import com.welfare.service.remote.fallback.WoLifeFeignClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/1/11 9:56 AM
 */
@FeignClient(value = "wo_life", url = "${wo_life.url:http://113.204.98.245:9081}", fallbackFactory = WoLifeFeignClientFallback.class, configuration = FeignConfiguration.class)
@ConditionalOnHavingProperty("wo_life.url")
public interface WoLifeFeignClient {

  /**
   * 账户余额查询
   */
  @RequestMapping(value = "/webrdp-web/_saas/_app/lifehouse.app/service/jsonService.db/getUserMoney.jssp", method = RequestMethod.POST, consumes = "application/json")
  WoLifeBasicResponse<WoLifeGetUserMoneyResponse> getUserMoney(
      @RequestParam(value = "phone", required = true) String phone
  );

  /**
   * 账户扣款
   */
  @RequestMapping(value = "/webrdp-web/_saas/_app/lifehouse.app/service/jsonService.db/getSubmitForTC.jssp", method = RequestMethod.POST, consumes = "application/json")
  WoLifeBasicResponse<WoLifeAccountDeductionResponse> accountDeduction(
      @RequestBody WoLifeAccountDeductionRequest request);

  /**
   * 退款销账
   */
  @RequestMapping(value = "/webrdp-web/_saas/_app/lifehouse.app/service/jsonService.db/refundForTC.jssp", method = RequestMethod.POST, consumes = "application/json")
  WoLifeBasicResponse refundWriteOff(
      @RequestBody WoLifeRefundWriteOffRequest request);

  /**
   * 扣款查询
   */
  /*@RequestMapping(value = "/", method = RequestMethod.GET)
  WoLifeBasicResponse getAccountDeduction(
      @RequestBody WoLifeGetAccountDeductionRequest request);*/

  /**
   * 销账查询
   */
/*  @RequestMapping(value = "/", method = RequestMethod.GET)
  WoLifeBasicResponse getAccountWriteOff(
      @RequestBody WoLifeGetAccountWriteOffRequest request);*/
}
