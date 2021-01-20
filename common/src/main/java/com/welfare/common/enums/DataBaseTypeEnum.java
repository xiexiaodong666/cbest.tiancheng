package com.welfare.common.enums;

/**
 * @Author: qiang.deng01
 * @Date: 2021/01/08 9:49
 * 多数据源枚举
 */
public enum DataBaseTypeEnum {
    /**
     *
     */
    MYSQL_DB("mysql","mysql数据源"),
    PRESTO_DB("presto","presto数据源");


    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    private final String code;

    private final String desc;

    DataBaseTypeEnum(String code, String desc){
        this.code = code;
        this.desc = desc;
    }

    public static String getDescByCode(String code){
        for (DataBaseTypeEnum baseTypeEnum : DataBaseTypeEnum.values()){
            if(baseTypeEnum.code.equals(code)){
                return baseTypeEnum.desc;
            }
        }
        return null;
    }
}
