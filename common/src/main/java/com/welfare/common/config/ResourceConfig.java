package com.welfare.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;

//@Order(1)
//@Configuration
public class ResourceConfig {
    @Configuration
    @PropertySource("${encrypt.file}")
    public class Config{

    }
}
