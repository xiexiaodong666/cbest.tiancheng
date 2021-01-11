package com.welfare;

import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.config.MicaJacksonConfiguration;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.SimpleCommandLinePropertySource;

@Slf4j
@SpringBootApplication(exclude = {MicaJacksonConfiguration.class})
public class ServiceMerchantApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ServiceMerchantApplication.class);
        log.info("started !!!");
    }
}
