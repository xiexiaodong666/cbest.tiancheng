package com.welfare.common.config;

import com.welfare.common.aop.SignatureAspect;
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
public class AspectConfig {
    @Bean
    public SignatureAspect signatureAspect(){
        return new SignatureAspect();
    }
}
