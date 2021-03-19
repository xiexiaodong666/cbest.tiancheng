package com.welfare.service.remote;

import com.welfare.common.annotation.ConditionalOnHavingProperty;
import com.welfare.service.remote.config.FeignConfiguration;
import com.welfare.service.remote.entity.request.WoLifeGetAccountDeductionRequest;
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
@FeignClient(value = "wo-life", url = "${wo-life.url:http://113.204.98.245:8900}", fallbackFactory = WoLifeFeignClientFallback.class, configuration = FeignConfiguration.class)
@ConditionalOnHavingProperty("wo-life.url")
public interface WoLifeFeignClient {

  /**
   * 账户余额查询
   */
  @RequestMapping(value = "/testlivingroom/_saas/_app/lifehouse.app/service/jsonService.db/getUserMoney.jssp", method = RequestMethod.POST, consumes = "application/x-www-form-urlencoded;charset=UTF-8")
  WoLifeBasicResponse<WoLifeGetUserMoneyResponse> getUserMoney(
      @RequestParam(name = "phone") String phone
  );

  /**
   * 账户扣款
   */
  @RequestMapping(value = "/testlivingroom/_saas/_app/lifehouse.app/service/jsonService.db/getSubmitOrderForTC.jssp", method = RequestMethod.POST, consumes = "application/x-www-form-urlencoded;charset=UTF-8")
  WoLifeBasicResponse<WoLifeAccountDeductionResponse> accountDeduction(
      @RequestParam(name = "phone") String phone, @RequestParam(name = "data") String data);

  /**
   * 退款销账
   */
  @RequestMapping(value = "/testlivingroom/_saas/_app/lifehouse.app/service/jsonService.db/refundForTC.jssp", method = RequestMethod.POST, consumes = "application/x-www-form-urlencoded;charset=UTF-8")
  WoLifeBasicResponse refundWriteOff(
      @RequestParam(name = "phone") String phone,
      @RequestParam(name = "data") String data);

  /**
   * 扣款查询
   */
 /* @RequestMapping(value = "/", method = RequestMethod.GET)
  WoLifeBasicResponse getAccountDeduction(
      @RequestBody WoLifeGetAccountDeductionRequest request);*/

  /**
   * 销账查询
   */
/*  @RequestMapping(value = "/", method = RequestMethod.GET)
  WoLifeBasicResponse getAccountWriteOff(
      @RequestBody WoLifeGetAccountWriteOffRequest request);*/
}
