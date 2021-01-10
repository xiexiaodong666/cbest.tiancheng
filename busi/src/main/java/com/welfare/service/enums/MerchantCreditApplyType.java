package com.welfare.service.enums;

/**
 * @author duanhy
 * @version 1.0.0
 * @description
 * @date 2021/1/10  2:43 PM
 */
public enum MerchantCreditApplyType {

  RECHARGE_LIMIT("RECHARGE_LIMIT","添加充值额度"),
  BALANCE("BALANCE","添加余额"),
  REMAINING_LIMIT("REMAINING_LIMIT","添加剩余信用额度"),
  REBATE_LIMIT("REBATE_LIMIT","消耗返点"),
  CREDIT_LIMIT("CREDIT_LIMIT","设置信用额度");

  private String code;
  private String value;

  private MerchantCreditApplyType(String code, String value){
    this.code=code;
    this.value=value;
  }
  public static MerchantCreditApplyType getByCode(String code){
    for(MerchantCreditApplyType genderEnum: MerchantCreditApplyType.values()){
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