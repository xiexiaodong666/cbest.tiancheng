package com.welfare;

import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.config.MicaJacksonConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;

@Slf4j
@EnableFeignClients(basePackages = "com.welfare.service.remote")
@SpringBootApplication(exclude = {MicaJacksonConfiguration.class})
@EnableAsync
@EnableRetry
public class ServiceMerchantApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ServiceMerchantApplication.class);
        log.info("started !!!");
    }
}
