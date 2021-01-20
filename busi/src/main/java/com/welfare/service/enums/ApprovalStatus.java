package com.welfare.service.enums;

import com.welfare.common.exception.BusiException;

/**
 *
 * @author duanhy
 * @version 1.0.0
 * @description
 * @date 2021/1/8  3:21 PM
 */
public enum  ApprovalStatus {
  /**
   * 申请状态
   */
  AUDIT_SUCCESS("AUDIT_SUCCESS","通过"),
  AUDIT_FAILED("AUDIT_FAILED","不通过"),
  AUDITING("AUDITING","待审核");

  private final String code;
  private final String value;

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
    throw new RuntimeException("ApprovalStatus不存在");
  };
  public String getCode() {
    return code;
  }

  public String getValue() {
    return value;
  }

}