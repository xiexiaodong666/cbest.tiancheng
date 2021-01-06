package com.welfare.persist.config;


import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = {"com.welfare.persist.repositories"}
)
@EntityScan("com.welfare.persist.domain")
public class EntityManagerConfig {

}
