package com.welfare.common.enums;

/**
 * @author hao.yin
 * @version 1.0.0
 * @date 2021/1/12 8:40 PM
 */
public enum MerchantAccountTypeShowStatusEnum {
  /**
   *
   */
  SHOW(1, "展示"),
  UNSHOW(0, "不展示");


  public Integer getCode() {
    return code;
  }

  public String getDesc() {
    return desc;
  }

  private final Integer code;

  private final String desc;

  MerchantAccountTypeShowStatusEnum(Integer code, String desc) {
    this.code = code;
    this.desc = desc;
  }
}
