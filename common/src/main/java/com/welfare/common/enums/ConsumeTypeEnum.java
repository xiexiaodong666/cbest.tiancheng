package com.welfare.common.enums;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/1/10 8:07 PM
 */
public enum ConsumeTypeEnum {
  O2O("O2O", "O2O（线上下单，到店提货"),
  ONLINE_MALL("ONLINE_MALL", "线上商城"),
  SHOP_SHOPPING("SHOP_CONSUMPTION", "到店消费");


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

  ConsumeTypeEnum(String code, String desc) {
    this.code = code;
    this.desc = desc;
  }
}
