package com.welfare.common.enums;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/1/10 8:07 PM
 */
public enum ShoppingActionTypeEnum {
  ADD("ADD", "添加",1L),
  UPDATE("UPDATE", "修改",2L),
  DELETE("DELETE", "删除",3L),
  BATCH_ADD("BATCH_ADD", "批量添加",4L),
  ACTIVATE("ACTIVATE", "更改激活状态",5L);



  public void setCode(String code) {
    this.code = code;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }

  public Long getEvtType() {
    return evtType;
  }

  public void setEvtType(Long evtType) {
    this.evtType = evtType;
  }

  public String getCode() {
    return code;
  }

  public String getDesc() {
    return desc;
  }

  private String code;

  private String desc;

  private Long evtType;

  ShoppingActionTypeEnum(String code, String desc,Long evtType) {
    this.code = code;
    this.desc = desc;
    this.evtType=evtType;
  }
}
