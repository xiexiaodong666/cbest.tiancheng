package com.welfare.service.enums;

/**
 * Description: 账户金额操作类型
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 1/11/2021
 */
public enum IncreaseOrDecrease {
    /**
     * 操作类型
     */
    INCREASE("increase","增加"),
    DECREASE("decrease","减少");

    String code;
    String desc;

    IncreaseOrDecrease(String code, String desc){
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
