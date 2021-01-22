package com.welfare.common.enums;

/**
 * @author hao.yin
 * @version 1.0.0
 * @date 2021/1/12 8:40 PM
 */
public enum SupplierStoreStatusEnum {
  /**
   *
   */
  ACTIVATED(1, "激活"),
  NONACTIVATED(0, "未激活");


  public Integer getCode() {
    return code;
  }

  public String getDesc() {
    return desc;
  }

  private final Integer code;

  private final String desc;

  SupplierStoreStatusEnum(Integer code, String desc) {
    this.code = code;
    this.desc = desc;
  }
}
