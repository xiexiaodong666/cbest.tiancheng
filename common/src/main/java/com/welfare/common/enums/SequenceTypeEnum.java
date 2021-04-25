package com.welfare.common.enums;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/1/10 8:07 PM
 */
public enum SequenceTypeEnum {
  /**
   *
   */
  CARDID("CARDID", "卡号"),
  MAGNETIC_STRIPE_CARD_ID("MAGNETIC_STRIPE_CARD_ID","磁条卡卡号");


  public String getCode() {
    return code;
  }

  public String getDesc() {
    return desc;
  }

  private final  String code;

  private final String desc;

  SequenceTypeEnum(String code, String desc) {
    this.code = code;
    this.desc = desc;
  }
}
