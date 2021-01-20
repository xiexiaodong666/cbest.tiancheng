package com.welfare.common.enums;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/1/12 8:40 PM
 */
public enum SupplierStoreSourceEnum {
  /**
   *
   */
  MERCHANT_STORE_RELATION("merchantStoreRelation", "供应商");


  public String getCode() {
    return code;
  }

  public String getDesc() {
    return desc;
  }

  private final String code;

  private final String desc;

  SupplierStoreSourceEnum(String code, String desc) {
    this.code = code;
    this.desc = desc;
  }
}
