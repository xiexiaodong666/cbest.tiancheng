package com.welfare.common.enums;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/1/10 8:07 PM
 */
public enum ConsumeTypeEnum {
  O2O("O2O", "O2O（线上下单，到店提货","1"),
  ONLINE_MALL("ONLINE_MALL", "线上商城","2"),
  SHOP_SHOPPING("SHOP_CONSUMPTION", "到店消费","3");


  public void setCode(String code) {
    this.code = code;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }

  public String getExcelType() {
    return excelType;
  }

  public void setExcelType(String excelType) {
    this.excelType = excelType;
  }

  public String getCode() {
    return code;
  }

  public String getDesc() {
    return desc;
  }

  private String code;

  private String desc;

  //导入excel的时候传的code
  private String excelType;

  ConsumeTypeEnum(String code, String desc,String excelType) {
    this.code = code;
    this.desc = desc;
    this.excelType=excelType;
  }

  private final static Map<String, ConsumeTypeEnum> ENUM_MAP = Stream
          .of(ConsumeTypeEnum.values()).collect(Collectors
                  .toMap(ConsumeTypeEnum::getCode,
                          e -> e));
  private final static Map<String, String> TYPE_MAP = Stream
          .of(ConsumeTypeEnum.values()).collect(Collectors
                  .toMap(ConsumeTypeEnum::getExcelType,
                          ConsumeTypeEnum::getCode));

  public static ConsumeTypeEnum getByType(String type) {
    return ENUM_MAP.get(type);
  }

  public static String getTypeByExcelType(String excelType) {
    return TYPE_MAP.get(excelType);
  }
}
