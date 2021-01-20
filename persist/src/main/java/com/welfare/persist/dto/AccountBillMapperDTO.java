package com.welfare.persist.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author yaoxiao
 * @version 0.0.1
 * @date 2021/1/9 18:53
 */
@Data
public class AccountBillMapperDTO {

  /**
   * 账号CODE
   */
  private String accountCode;
  /**
   * 账单总金额
   */
  private BigDecimal accountTotalAmount;
  /**
   * 交易总笔数
   */
  private Integer tradeNum;

  /**
   * 账户余额
   */
  private BigDecimal accountBalance;
}
