package com.welfare.common.constants;

/**
 * @author yaoxiao
 * @version 0.0.1
 * @date 2021/1/14 17:15
 */
public enum  AccountChangeType {
  ACCOUNT_NEW("account_new","新增账户"),
  ACCOUNT_UPDATE("account_update","修改账户"),
  ACCOUNT_ACTIVE("account_active","账号激活"),
  ACCOUNT_LOCK("account_lock","账号锁定"),
  ACCOUNT_DELETE("account_delete","账号删除"),
  ACCOUNT_BALANCE_CHANGE("account_balance_change","账号余额变更"),
  ACCOUNT_TYPE_DELETE("account_type_delete","账号关联员工类型删除"),
  ACCOUNT_CONSUME_SCENE_ENABLE("account_consume_scene_enable","员工类型消费配置启用"),
  ACCOUNT_CONSUME_SCENE_DISABLE("account_consume_scene_disable","员工类型消费配置禁用"),
  ACCOUNT_CONSUME_SCENE_DELETE("account_consume_scene_delete","员工类型消费删除"),
  ACCOUNT_CONSUME_SCENE_CONSUMETYPE_CHANGE("account_consume_scene_consumetype_change","员工类型消费配置门店选择的消费方式变更"),
  ACCOUNT_CONSUME_SCENE_EDIT("account_consume_scene_edit","员工类型消费配置编辑"),
  ACCOUNT_SETTLE_RESTORE("account_settle_restore","员工授信结算额度恢复");
  private String changeType;
  private String changeValue;

  AccountChangeType(String changeType, String changeValue) {
    this.changeType = changeType;
    this.changeValue = changeValue;
  }

  public String getChangeType() {
    return changeType;
  }

  public String getChangeValue() {
    return changeValue;
  }

  public static AccountChangeType getByAccountStatus(Integer accountStatus){
    if( accountStatus.intValue() == 1 ){
      return AccountChangeType.ACCOUNT_ACTIVE;
    }else{
      return AccountChangeType.ACCOUNT_LOCK;
    }
  }

  public static AccountChangeType getByAccountConsumeStatus(Integer accountConsumeStatus){
    if( accountConsumeStatus.intValue() == 1 ){
      return AccountChangeType.ACCOUNT_CONSUME_SCENE_ENABLE;
    }else{
      return AccountChangeType.ACCOUNT_CONSUME_SCENE_DISABLE;
    }
  }
}
