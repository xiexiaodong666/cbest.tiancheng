package com.welfare.common.enums;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/2/1 2:53 PM
 */
public enum FileUniversalStorageEnum {
  /**
   *
   */
  ACCOUNT_IMG("ACCOUNT_IMG", "员工照片");



  public String getCode() {
    return code;
  }

  public String getDesc() {
    return desc;
  }

  private final String code;

  private final String desc;

  FileUniversalStorageEnum(String code, String desc) {
    this.code = code;
    this.desc = desc;
  }
}
