package com.welfare.common.enums;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/1/10 8:07 PM
 */
public enum ShoppingActionTypeEnum {
  /**
   *
   */
  ADD("ADD", "添加",1L),
  UPDATE("UPDATE", "修改",2L),
  DELETE("DELETE", "删除",3L),
  BATCH_ADD("ADD", "批量添加",4L),
  ACTIVATE("UPDATE", "更改激活状态",5L),
  ACCOUNT_BATCH_ADD("BATCH_ADD", "批量添加",6L),
  ACCOUNT_CONSUME_SCENE_BATCH_DELETE("ACCOUNT_CONSUME_SCENE_BATCH_DELETE","批量删除",7l);




  public Long getEvtType() {
    return evtType;
  }

  public String getCode() {
    return code;
  }

  public String getDesc() {
    return desc;
  }

  private final String code;

  private final String desc;

  private final Long evtType;

  ShoppingActionTypeEnum(String code, String desc,Long evtType) {
    this.code = code;
    this.desc = desc;
    this.evtType=evtType;
  }
}
