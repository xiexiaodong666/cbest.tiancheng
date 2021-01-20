package com.welfare.common.constants;

/**
 * @author yaoxiao
 * @version 0.0.1
 * @date 2021/1/20 13:56
 */
public enum  AccountConsumeSceneStatus {
  ENABLE(1 ,"正常"),
  DISABLE(2,"禁用");
  private final Integer code;
  private final String desc;

  AccountConsumeSceneStatus(Integer code, String desc) {
    this.code = code;
    this.desc = desc;
  }

  public Integer getCode() {
    return code;
  }

  public String getDesc() {
    return desc;
  }
}
