package com.welfare.common.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 1/9/2021
 */
@Configuration
public class SpringBeanUtils implements ApplicationContextAware {
    private static  ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext context){
        applicationContext = context;
    }

    public static <T> T getBean(Class<T> requiredType){
        return applicationContext.getBean(requiredType);
    }

    public static Object getBean(String beanName){
        return applicationContext.getBean(beanName);
    }

    public static ApplicationContext getApplicationContext(){
        return applicationContext;
    }
}
