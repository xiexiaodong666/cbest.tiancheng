package com.welfare.service.enums;

/**
 * 员工充值状态
 * @author duanhy
 * @version 1.0.0
 * @description
 * @date 2021/1/8  3:14 PM
 */
public enum RechargeStatus {

  /**
   *
   */
  SUCCESS("SUCCESS","充值成功"),
  NO("NO","审批未通过不充值"),
  INIT("INIT","还未充值");

  private final String code;
  private final String value;

  RechargeStatus(String code, String value){
    this.code=code;
    this.value=value;
  }
  public static RechargeStatus getByCode(String code){
    for(RechargeStatus genderEnum: RechargeStatus.values()){
      if(code.equals(genderEnum.code)){
        return genderEnum;
      }
    }
    throw new RuntimeException("RechargeStatus不存在");
  };
  public String getCode() {
    return code;
  }

  public String getValue() {
    return value;
  }
}