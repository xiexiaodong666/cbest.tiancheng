package com.welfare.persist.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author yaoxiao
 * @version 0.0.1
 * @date 2021/1/14 15:16
 */
@Data
public class AccountIncrementDTO implements Serializable {

  /**
   * 员工账号
   */
  private Long accountCode;
  /**
   * 员工账号变更记录ID
   */
  private Long changeEventId;
  /**
   * 员工账号绑定的磁条号，如果多个逗号分割。
   */
  private String magneticStripe;
  /**
   * 员工账号余额
   */
  private BigDecimal accountBalance;
  /**
   * 员工账号配置的消费场景是否在该门店支持到店消费
   */
  private Boolean canUse;
}
