package com.welfare.common.enums;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 2/3/2021
 */
public enum PaymentTypeEnum {
    /**
     *
     */
    ONLINE("online", "线上"),
    BARCODE("barcode","条码"),
    CARD("card","刷卡"),
    DOOR_ACCESS("door_access","门禁");


    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    private final  String code;

    private final String desc;

    PaymentTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
