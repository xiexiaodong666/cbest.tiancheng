package com.welfare.service.remote;

import com.welfare.common.annotation.ConditionalOnHavingProperty;
import com.welfare.service.remote.config.FeignConfiguration;
import com.welfare.service.remote.entity.request.WelfareMerChantConsumeDataRequest;
import com.welfare.service.remote.entity.response.WelfareMerChantConsumeDataResponse;
import com.welfare.service.remote.fallback.ServiceMirrorFeignClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/1/11 9:56 AM
 */
@FeignClient(value = "service-mirror", url = "${service-mirror.url:http://192.1.30.96:8889}", fallbackFactory = ServiceMirrorFeignClientFallback.class, configuration = FeignConfiguration.class)
@ConditionalOnHavingProperty("service-mirror.url")
public interface ServiceMirrorFeignClient {

  /**
   * 获取甜橙生活商户消费数据
   */
  @RequestMapping(value = "/magneto/api/831586026496593920", method = RequestMethod.POST, consumes = "application/json")
  WelfareMerChantConsumeDataResponse getWelfareMerChantConsumeData(@RequestBody
      WelfareMerChantConsumeDataRequest request);
}
