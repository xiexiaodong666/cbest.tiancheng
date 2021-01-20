package com.welfare;

import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.config.MicaJacksonConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;

@Slf4j
@EnableFeignClients(basePackages = "com.welfare.service.remote")
@SpringBootApplication(exclude = {MicaJacksonConfiguration.class})
public class ServiceMerchantApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ServiceMerchantApplication.class);
        log.info("started !!!");
    }
}
