package com.welfare.common.config;

import com.welfare.common.constants.FrameworkConstant;
import com.welfare.common.constants.WelfareConstant;
import com.welfare.common.domain.MerchantUserInfo;
import com.welfare.common.util.MerchantUserHolder;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;

import java.util.Objects;
import java.util.UUID;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 1/12/2021
 */
@Slf4j
public class FeignInterceptor implements RequestInterceptor {
    @Value("${e-welfare.header.source:to_be_set_in_yml}")
    private String headerSource;
    @Override
    public void apply(RequestTemplate template) {
        addTraceId(template);
        addDmallHeaderParam(template);
        template.header(WelfareConstant.Header.SOURCE.code(),headerSource);
    }

    private void addTraceId(RequestTemplate template) {
        String traceId = MDC.get(FrameworkConstant.TRACE_ID);
        if(Strings.isEmpty(traceId)){
            traceId = UUID.randomUUID().toString();
        }
        template.header(FrameworkConstant.TRACE_ID,traceId);
    }

    private void addDmallHeaderParam(RequestTemplate template){
        MerchantUserInfo merchantUserInfo = MerchantUserHolder.getMerchantUser();
        if (Objects.nonNull(merchantUserInfo)) {
            template.header(FrameworkConstant.USER_ID, merchantUserInfo.getUserCode());
            template.header(FrameworkConstant.USER_NAME, merchantUserInfo.getUsername());
            template.header(FrameworkConstant.TIMETAMP, String.valueOf(System.currentTimeMillis()));
        }
    }
}
