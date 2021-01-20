package com.welfare.common.enums;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/1/10 8:07 PM
 */
public enum MerIdentityEnum {
  /**
   *
   */
  customer("PARTER", "客户"),
  supplier("SUPPLIER", "供应商");



  public String getCode() {
    return code;
  }

  public String getDesc() {
    return desc;
  }

  private final String code;

  private final String desc;

  MerIdentityEnum(String code, String desc) {
    this.code = code;
    this.desc = desc;
  }
}
