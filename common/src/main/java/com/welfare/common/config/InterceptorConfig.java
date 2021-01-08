package com.welfare.common.config;

import com.welfare.common.interceptor.HeaderVerificationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 1/7/2021
 */
@Configuration
public class InterceptorConfig {
    @Bean
    public HeaderVerificationInterceptor headerVerificationInterceptor(){
        return new HeaderVerificationInterceptor();
    }
}
