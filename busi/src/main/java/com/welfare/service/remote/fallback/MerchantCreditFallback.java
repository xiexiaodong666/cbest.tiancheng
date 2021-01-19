package com.welfare.service.remote.fallback;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.welfare.common.exception.BusiException;
import com.welfare.common.exception.ExceptionCode;
import com.welfare.service.remote.MerchantCreditFeign;
import com.welfare.service.remote.ShoppingFeignClient;
import com.welfare.service.remote.entity.*;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.cert.ocsp.Req;
import org.springframework.stereotype.Component;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/1/11 11:15 PM
 */
@Slf4j
@Component
public class MerchantCreditFallback implements FallbackFactory<MerchantCreditFeign> {

  @Override
  public MerchantCreditFeign create(Throwable throwable) {
    return req -> {
      log.error(StrUtil.format("调用商户恢复商户信用额度异常, req: {}", JSON.toJSONString(req)), throwable);
      throw new BusiException(ExceptionCode.UNKNOWON_EXCEPTION, "系统异常", null);
    };
  }
}
