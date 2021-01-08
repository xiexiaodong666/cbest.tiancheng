package com.welfare.service.enums;

/**
 *
 * @author duanhy
 * @version 1.0.0
 * @description
 * @date 2021/1/8  3:21 PM
 */
public enum  ApprovalStatus {

  AUDIT_SUCCESS("AUDIT_SUCCESS","通过"),
  AUDIT_FAILED("AUDIT_FAILED","不通过"),
  AUDITING("AUDIT_FAILED","待审核");

  private String code;
  private String value;

  private ApprovalStatus(String code, String value){
    this.code=code;
    this.value=value;
  }
  public static ApprovalStatus getByCode(String code){
    for(ApprovalStatus genderEnum: ApprovalStatus.values()){
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