package com.welfare.service.remote.config;

import feign.Contract;
import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author duanhy
 * @version 1.0.0
 * @description
 * @date 2021/1/12  9:49 AM
 */
@Configuration
public class FeignConfiguration {

  /**
   * 日志级别
   * @return
   */
  @Bean
  Logger.Level feignLoggerLevel() {
    return Logger.Level.FULL;
  }
}