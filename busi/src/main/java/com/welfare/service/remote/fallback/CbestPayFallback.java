package com.welfare.service.remote.fallback;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.welfare.common.exception.BizException;
import com.welfare.common.exception.ExceptionCode;
import com.welfare.service.remote.CbestPayFeign;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CbestPayFallback implements FallbackFactory<CbestPayFeign> {

    @Override
    public CbestPayFeign create(Throwable throwable) {
        return req -> {
            log.error(StrUtil.format("调用重百付接口异常, req: {}", JSON.toJSONString(req)), throwable);
            throw new BizException(ExceptionCode.UNKNOWN_EXCEPTION, "系统异常", null);
        };
    }
}
