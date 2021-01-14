package com.welfare.common.config;

import com.welfare.common.interceptor.HeaderVerificationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 1/7/2021
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Autowired
    private  HeaderVerificationInterceptor headerVerificationInterceptor;
    @Value("${exclude-header-interceptor:/swagger*/**,/doc.html*,***/error,/index,/**/*.css,/**/*.js,/**/*.png,/**/*.jpg,/**/*.jpeg,/**/*.ico}")
    private List<String> excludeHeaderInterceptor;

    @Bean
    public HeaderVerificationInterceptor headerVerificationInterceptor(){
        return new HeaderVerificationInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // TODO add Interceptor path
         registry.addInterceptor(headerVerificationInterceptor)
                 .addPathPatterns("/**")
                 .excludePathPatterns(excludeHeaderInterceptor);
    }
}
