package com.welfare.service.remote.fallback;

import com.alibaba.fastjson.JSON;
import com.welfare.common.exception.BizException;
import com.welfare.service.remote.AccountCreditFeign;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/3/10 1:58 下午
 */
@Component
@Slf4j
public class AccountCreditFeignFallback implements FallbackFactory<AccountCreditFeign> {

  @Override
  public AccountCreditFeign create(Throwable throwable) {
    return (request, source) -> {
      log.error("查询价格模板失败, 请求:{}", JSON.toJSONString(request), throwable);
      throw new BizException("系统异常");
    };
  }
}
