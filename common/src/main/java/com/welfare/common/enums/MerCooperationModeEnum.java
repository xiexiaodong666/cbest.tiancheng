package com.welfare.common.enums;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/1/10 8:07 PM
 */
public enum MerCooperationModeEnum {
  payFirt("payFirt", "先付费"),
  payed("payed", "后付费");


  public void setCode(String code) {
    this.code = code;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }

  public String getCode() {
    return code;
  }

  public String getDesc() {
    return desc;
  }

  private String code;

  private String desc;

  MerCooperationModeEnum(String code, String desc) {
    this.code = code;
    this.desc = desc;
  }
}
