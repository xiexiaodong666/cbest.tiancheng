package com.welfare;

import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.config.MicaJacksonConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;

@Slf4j
@SpringBootApplication(exclude = {MicaJacksonConfiguration.class})
@EnableFeignClients
public class ServiceSettlementApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ServiceSettlementApplication.class);
        log.info("started !!!");
    }
}
