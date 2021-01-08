package com.welfare.servicesettlement.config.datasource;

import com.welfare.common.enums.DataBaseTypeEnum;
import lombok.extern.log4j.Log4j2;

/**
 * 保存一个线程安全的DatabaseType容器
 */
@Log4j2
public class DataBaseContextHolder {
	
    private static final ThreadLocal<DataBaseTypeEnum> contextHolder = new ThreadLocal<>();
    
    public static void setDatabaseType(DataBaseTypeEnum type){
    	contextHolder.set(type);
    }
    
    public static DataBaseTypeEnum getDatabaseType(){
        return contextHolder.get();
    }

	public static ThreadLocal<DataBaseTypeEnum> getContextholder() {
		return contextHolder;
	}
	
	public static void clearDbType() {
		contextHolder.remove();
	}
}