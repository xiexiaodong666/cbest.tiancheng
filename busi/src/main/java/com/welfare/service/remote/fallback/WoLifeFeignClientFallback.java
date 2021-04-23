package com.welfare.service.remote.fallback;


import com.welfare.common.exception.BizException;
import com.welfare.common.exception.ExceptionCode;
import com.welfare.service.remote.WoLifeFeignClient;

import com.welfare.service.remote.entity.response.WoLifeAccountDeductionResponse;
import com.welfare.service.remote.entity.response.WoLifeBasicResponse;
import com.welfare.service.remote.entity.response.WoLifeGetUserMoneyResponse;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/1/11 11:15 PM
 */
@Slf4j
@Component
public class WoLifeFeignClientFallback implements FallbackFactory<WoLifeFeignClient> {


  @Override
  public WoLifeFeignClient create(Throwable throwable) {
    return new WoLifeFeignClient() {
      @Override
      public WoLifeBasicResponse<WoLifeGetUserMoneyResponse> getUserMoney(String phone) {
        log.error("沃生活馆账户余额查询失败, 请求:{}", phone, throwable);
        throw new BizException(ExceptionCode.UNKNOWON_EXCEPTION, "沃生活馆系统异常", null);
      }

      @Override
      public WoLifeBasicResponse<WoLifeAccountDeductionResponse> accountDeduction(
          String phone, String data) {
        log.error("沃生活馆账户扣款失败, 请求:{},{}", phone, data, throwable);
        throw new BizException(ExceptionCode.UNKNOWON_EXCEPTION, "沃生活馆系统异常", null);
      }

      @Override
      public WoLifeBasicResponse refundWriteOff( String phone, String data) {
        log.error("沃生活馆退款销账失败, 请求:{},{}", phone, data, throwable);
        throw new BizException(ExceptionCode.UNKNOWON_EXCEPTION, "沃生活馆系统异常", null);
      }

  /*    @Override
      public WoLifeBasicResponse getAccountDeduction(WoLifeGetAccountDeductionRequest request) {
        log.error("沃生活馆退款销账失败, 请求:{}", JSON.toJSONString(request), throwable);
        throw new BizException(ExceptionCode.UNKNOWON_EXCEPTION, "沃生活馆系统异常", null);
      }*/

/*      @Override
      public WoLifeBasicResponse getAccountDeduction(WoLifeGetAccountDeductionRequest request) {
        log.error("沃生活馆扣款查询失败, 请求:{}", JSON.toJSONString(request), throwable);
        throw new BizException(ExceptionCode.UNKNOWON_EXCEPTION, "沃生活馆系统异常", null);
      }

      @Override
      public WoLifeBasicResponse getAccountWriteOff(WoLifeGetAccountWriteOffRequest request) {
        log.error("沃生活馆销账查询失败, 请求:{}", JSON.toJSONString(request), throwable);
        throw new BizException(ExceptionCode.UNKNOWON_EXCEPTION, "沃生活馆系统异常", null);
      }*/
    };
  }
}
