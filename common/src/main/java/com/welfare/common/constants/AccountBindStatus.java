package com.welfare.common.constants;

public enum AccountBindStatus {
  NO_BIND(0 ,"未绑定"),
  BIND(1,"已绑定");
  private final Integer code;
  private final String desc;

  AccountBindStatus(Integer code, String desc){
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
