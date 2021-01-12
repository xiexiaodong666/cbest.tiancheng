package com.welfare;

import net.dreamlu.mica.config.MicaJacksonConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author Yuxiang Li
 */
@SpringBootApplication(exclude = {MicaJacksonConfiguration.class})
@EnableFeignClients
public class ServiceAccountApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceAccountApplication.class);
    }

}
