package com.welfare.service.enums;

/**
 * 员工账号审批类型
 * @author duanhy
 * @version 1.0.0
 * @description
 * @date 2021/1/8  3:24 PM
 */
public enum ApprovalType {


  SINGLE("SINGLE","单个"),
  BATCH("BATCH","批量");

  private String code;
  private String value;

  private ApprovalType(String code, String value){
    this.code=code;
    this.value=value;
  }
  public static ApprovalType getByCode(String code){
    for(ApprovalType genderEnum: ApprovalType.values()){
      if(code.equals(genderEnum.code)){
        return genderEnum;
      }
    }
    throw new RuntimeException("ApprovalType不存在");
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