package com.welfare.service.enums;

/**
 * Description: 金额加减类型
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 1/11/2021
 */
public enum IncOrDecType {
    /**
     * 操作类型
     */
    INCREASE("increase","增加"),
    DECREASE("decrease","减少");

    String code;
    String desc;

    IncOrDecType(String code, String desc){
        this.code = code;
        this.desc = desc;
    }

    public String getCode(){
        return this.code;
    }
    public String getDesc(){
        return this.desc;
    }
}
