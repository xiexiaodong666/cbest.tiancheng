package com.welfare.servicesettlement.config.datasource;


import com.alibaba.druid.pool.DruidDataSource;
import com.welfare.common.enums.DataBaseTypeEnum;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;


@Configuration
@MapperScan(basePackages = "com.welfare.persist.mapper")
public class DataBaseConfig {

    @Bean(name = "mysqlDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.mysql")
    public DataSource setMysqlDataSource() {
        return new DruidDataSource();
    }
    

    @Bean(name = "prestoDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.presto")
    public DataSource setPrestoDataSource() {
        return new DruidDataSource();
    }

    @Bean(name = "dynamicDataSource")
    @Primary
    public DynamicDataSource dynamicDataSource(@Qualifier("mysqlDataSource") DataSource mysqlDataSource,
            @Qualifier("prestoDataSource") DataSource prestoDataSource) {
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(DataBaseTypeEnum.MYSQL_DB, mysqlDataSource);
        targetDataSources.put(DataBaseTypeEnum.PRESTO_DB, prestoDataSource);

        DynamicDataSource dataSource = new DynamicDataSource();
        dataSource.setTargetDataSources(targetDataSources);
        dataSource.setDefaultTargetDataSource(mysqlDataSource);

        return dataSource;
    }

    /**
     * 根据数据源创建SqlSessionFactory
     */
    @Bean
    public SqlSessionFactory sqlSessionFactory(@Qualifier("mysqlDataSource") DataSource primaryDataSource,
            @Qualifier("prestoDataSource") DataSource minorDataSource) throws Exception {
        SqlSessionFactoryBean fb = new SqlSessionFactoryBean();
        fb.setDataSource(this.dynamicDataSource(primaryDataSource, minorDataSource));
        fb.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:com/welfare/mapper/*.xml"));
        return fb.getObject();
    }
}
