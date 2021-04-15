package com.welfare.service.remote.fallback;

import com.welfare.common.exception.BusiException;
import com.welfare.common.exception.ExceptionCode;
import com.welfare.service.remote.ServiceMirrorFeignClient;
import com.welfare.service.remote.entity.request.WelfareMerChantConsumeDataRequest;
import com.welfare.service.remote.entity.response.WelfareMerChantConsumeDataResponse;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/4/14 10:40 AM
 */
@Slf4j
@Component
public class ServiceMirrorFeignClientFallback implements FallbackFactory<ServiceMirrorFeignClient> {

  @Override
  public ServiceMirrorFeignClient create(Throwable throwable) {
    return new ServiceMirrorFeignClient() {
      @Override
      public WelfareMerChantConsumeDataResponse getWelfareMerChantConsumeData(
          WelfareMerChantConsumeDataRequest request) {
        log.error("报表系统查询甜橙商户消费数据异常, 请求:{}", request, throwable);
        throw new BusiException(ExceptionCode.UNKNOWON_EXCEPTION, "报表系统查询甜橙商户消费数据异常", null);
      }
    };
  }
}
