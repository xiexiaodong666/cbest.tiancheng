package com.welfare.service.enums;

/**
 * 员工充值状态
 * @author duanhy
 * @version 1.0.0
 * @description
 * @date 2021/1/8  3:14 PM
 */
public enum RechargeStatus {

  SUCCESS("SUCCESS","充值成功"),
  FAIL("FAIL","充值失败"),
  NO("NO","还未充值");

  private String code;
  private String value;

  private RechargeStatus(String code, String value){
    this.code=code;
    this.value=value;
  }
  public static RechargeStatus getByCode(String code){
    for(RechargeStatus genderEnum: RechargeStatus.values()){
      if(code.equals(genderEnum.code)){
        return genderEnum;
      }
    }
    return null;
  };
  public String getCode() {
    return code;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

}