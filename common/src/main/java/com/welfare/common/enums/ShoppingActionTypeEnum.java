package com.welfare.common.enums;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/1/10 8:07 PM
 */
public enum ShoppingActionTypeEnum {
  ADD("ADD", "添加"),
  UPDATE("UPDATE", "修改"),
  DELETE("DELETE", "删除");


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

  ShoppingActionTypeEnum(String code, String desc) {
    this.code = code;
    this.desc = desc;
  }
}
