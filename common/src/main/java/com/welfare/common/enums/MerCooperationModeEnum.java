package com.welfare.common.enums;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/1/10 8:07 PM
 */
public enum MerCooperationModeEnum {
  /**
   *
   */
  payFirt("PAY_FIRST", "先付费"),
  payed("PAYED", "后付费");


  public String getCode() {
    return code;
  }

  public String getDesc() {
    return desc;
  }

  private final String code;

  private final String desc;

  MerCooperationModeEnum(String code, String desc) {
    this.code = code;
    this.desc = desc;
  }

  public static MerCooperationModeEnum getByCode(String code){
    for(MerCooperationModeEnum genderEnum: MerCooperationModeEnum.values()){
      if(code.equals(genderEnum.code)){
        return genderEnum;
      }
    }
    throw new RuntimeException("MerCooperationModeEnum不存在");
  };
}
