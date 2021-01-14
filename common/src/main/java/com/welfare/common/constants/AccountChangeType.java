package com.welfare.common.constants;

import lombok.Data;

/**
 * @author yaoxiao
 * @version 0.0.1
 * @date 2021/1/14 17:15
 */
public enum  AccountChangeType {
  ACCOUNT_NEW("account_new","新增账户"),
  ACCOUNT_ACTIVE("account_active","账号激活"),
  ACCOUNT_LOCK("account_lock","账号锁定"),
  ACCOUNT_DELETE("account_delete","账号删除"),
  ACCOUNT_BALANCE_CHANGE("account_balance_change","账号余额变更"),
  ACCOUNT_TYPE_DELETE("account_type_delete","账号关联员工类型删除"),
  ACCOUNT_CONSUME_SCENE_ENABLE("account_consume_scene_enable","员工类型消费配置启用"),
  ACCOUNT_CONSUME_SCENE_DISABLE("account_consume_scene_disable","员工类型消费配置禁用"),
  ACCOUNT_CONSUME_SCENE_DELETE("account_consume_scene_delete","员工类型消费删除"),
  ACCOUNT_CONSUME_SCENE_CONSUMETYPE_CHANGE("account_consume_scene_consumetype_change","员工类型消费配置门店选择的消费方式变更");
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
}
