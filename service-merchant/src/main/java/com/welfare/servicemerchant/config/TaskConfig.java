package com.welfare.servicemerchant.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author duanhy
 * @version 1.0.0
 * @description
 * @date 2021/1/10  7:07 PM
 */
@Configuration
public class TaskConfig {


  @Bean(name = "accountApplySaveExecutor")
  public ThreadPoolExecutor taskCustomerSaveExecutor(){
    ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5,
            5, 60,
            TimeUnit.SECONDS,
            new LinkedBlockingDeque<>());
    return threadPoolExecutor;
  }
}