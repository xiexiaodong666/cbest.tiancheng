package com.welfare.common.enums;

/**
 * Description: 启用，禁用enum
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 1/25/2021
 */
public enum EnableEnum {
    /**
     *
     */
    ENABLE(1, "启用"),
    DISABLE(0, "禁用");


    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    private final Integer code;

    private final String desc;

    EnableEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
