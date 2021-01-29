package com.welfare.service.remote.fallback;

import com.welfare.service.remote.CbestDmallFeign;
import feign.hystrix.FallbackFactory;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/1/29 10:50 上午
 */
public class CbestDmallFeignFallback implements FallbackFactory<CbestDmallFeign> {
  @Override
  public CbestDmallFeign create(Throwable throwable) {
    return null;
  }
}
