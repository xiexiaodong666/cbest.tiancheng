package com.welfare.common.enums;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/1/15 6:35 PM
 */
public enum MerchantStoreRelationRebateTypeEnum {

  EMPLOYEE_CARD_NUMBER_PAY("EMPLOYEE_CARD_NUMBER_PAY","员工卡号支付"),
  OTHER_PAY("OTHER_PAY","其他支付方式");

  public void setCode(String code) {
    this.code = code;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }

  public String getCode() {
    return code;
  }

  public String getDesc() {
    return desc;
  }

  private String code;

  private String desc;

  MerchantStoreRelationRebateTypeEnum(String code, String desc){
    this.code = code;
    this.desc = desc;
  }
}
