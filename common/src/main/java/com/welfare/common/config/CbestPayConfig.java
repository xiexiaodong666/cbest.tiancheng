package com.welfare.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "cbest.pay")
public class CbestPayConfig {

    private String appId;

    private String appKey;



}
