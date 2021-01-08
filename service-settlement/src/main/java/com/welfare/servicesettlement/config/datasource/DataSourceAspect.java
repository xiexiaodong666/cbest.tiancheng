package com.welfare.servicesettlement.config.datasource;

import com.welfare.common.enums.DataBaseTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;


@Aspect
@Component
@Slf4j
public class DataSourceAspect {
    @Around("execution(* com.welfare.persist.mapper.*.*(..))")
    public Object setDataSourceKey(ProceedingJoinPoint point){
    	Object proceed = null;

/*        if(point.getTarget() instanceof PrestoDao){
        	DataBaseContextHolder.setDatabaseType(DataBaseTypeEnum.PRESTO_DB);
        }else{
        	DataBaseContextHolder.setDatabaseType(DataBaseTypeEnum.MYSQL_DB);
        }*/

        DataBaseContextHolder.setDatabaseType(DataBaseTypeEnum.MYSQL_DB);
        try {
		    proceed = point.proceed();
		} catch (Throwable e) {
			e.printStackTrace();
		}finally {
            DataBaseContextHolder.clearDbType();
        }
		return proceed;
    }
}