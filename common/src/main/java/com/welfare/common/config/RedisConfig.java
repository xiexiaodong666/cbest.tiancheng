package com.welfare.common.config;

import com.welfare.common.util.RedisDistributeLock;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 1/8/2021
 */
@Configuration
public class RedisConfig {

    @Bean
    @ConditionalOnClass(RedisTemplate.class)
    @ConditionalOnProperty(prefix = "e-welfare.distributed-lock",name = "enabled",havingValue = "true")
    public RedisDistributeLock redisDistributeLock(RedisTemplate redisTemplate){
        RedisDistributeLock redisDistributeLock = new RedisDistributeLock(redisTemplate);
        return redisDistributeLock;
    }

}
