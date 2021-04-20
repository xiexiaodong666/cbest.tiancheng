package com.welfare.common.constants;

/**
 * @author yaoxiao
 * @version 0.0.1
 * @date 2021/1/21 11:02
 */
public enum AccountStatus {
  ENABLE(1 ,"正常"),
  DISABLED(2,"禁用");
  private final Integer code;
  private final String desc;

  AccountStatus(Integer code, String desc){
    this.code = code;
    this.desc = desc;
  }

  public Integer getCode() {
    return code;
  }

  public String getDesc() {
    return desc;
  }

  public static boolean validStatus(Integer status){
    if( null == status ){
      return false;
    }
    if( status.intValue() != ENABLE.code.intValue() &&  DISABLED.getCode().intValue() != status.intValue()){
      return false;
    }
    return true;
  }

  public static AccountStatus getByCode(Integer status) {
      for(AccountStatus accountStatus: AccountStatus.values()){
        if(status.equals(accountStatus.getCode())){
          return accountStatus;
        }
      }
      throw new RuntimeException("AccountStatus不存在");
    }

}
