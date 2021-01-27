package com.welfare.service.remote.fallback;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.welfare.common.exception.BusiException;
import com.welfare.common.exception.ExceptionCode;
import com.welfare.service.remote.MerchantCreditFeign;
import com.welfare.service.remote.NotificationFeign;
import com.welfare.service.remote.entity.NotificationReq;
import com.welfare.service.remote.entity.NotificationResp;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 1/27/2021
 */
@Slf4j
@Component
public class NotificationFallback implements FallbackFactory<NotificationFeign> {
    @Override
    public NotificationFeign create(Throwable throwable) {
        return notificationReq -> {
            log.error(StrUtil.format("调用通知系统出错, req: {}", JSON.toJSONString(notificationReq)), throwable);
            throw new BusiException(ExceptionCode.UNKNOWON_EXCEPTION, "系统异常", null);
        };
    }
}
