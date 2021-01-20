package com.welfare.common.enums;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/1/10 8:07 PM
 */
public enum MoveDirectionEnum {
  /**
   *
   */
  UP("up", "上移"),
  DOWN("down", "下移");


  public String getCode() {
    return code;
  }

  public String getDesc() {
    return desc;
  }

  private final String code;

  private final String desc;

  MoveDirectionEnum(String code, String desc) {
    this.code = code;
    this.desc = desc;
  }
}
