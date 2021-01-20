package com.welfare.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author yaoxiao
 * @version 0.0.1
 * @date 2021/1/7 18:02
 */
@Data
@ApiModel("用户流水汇总")
public class AccountBillDTO implements Serializable {

  /**
   * 账单总金额
   */
  @ApiModelProperty("账单总金额")
  private BigDecimal accountTotalAmount;
  /**
   * 交易总笔数
   */
  @ApiModelProperty("交易总笔数")
  private int tradeNum;
  /**
   * 目前余额
   */
  @ApiModelProperty("目前余额")
  private BigDecimal accountBalance;

}
