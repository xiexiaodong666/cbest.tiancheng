package com.welfare.common.enums;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/1/15 4:48 PM
 */
public enum CardApplyTypeEnum {
  /**
   *
   */
  STORED_VALUE_CARD("STORED_VALUE_CARD", "储值卡"),
  VOUCHER("VOUCHER", "代金券"),
  DISCOUNT_STORED_VALUE_CARD("DISCOUNT_STORED_VALUE_CARD", "折扣储值卡"),
  DISCOUNT_CARD("DISCOUNT_CARD", "折扣卡");




  public String getCode() {
    return code;
  }

  public String getDesc() {
    return desc;
  }

  private final String code;

  private final String desc;

  CardApplyTypeEnum(String code, String desc) {
    this.code = code;
    this.desc = desc;
  }
}
