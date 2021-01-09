package com.welfare.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;

/**
 * @author yaoxiao
 * @version 0.0.1
 * @date 2021/1/7 18:09
 */
@ApiModel("用户流水明细DTO")
@Data
public class AccountBillDetailDTO implements Serializable {
  /**
   * 消费门店名称
   */
  @ApiModelProperty("消费门店名称")
  private String storeName;

  /**
   * 交易类型
   */
  @ApiModelProperty("交易类型")
  private String transType;

  /**
   * 交易总金额
   */
  @ApiModelProperty("交易总金额")
  private BigDecimal transAmount;

  /**
   * 账户余额
   */
  @ApiModelProperty("账户余额")
  private BigDecimal accountBalance;

  /**
   * 创建时间
   */
  @ApiModelProperty("创建时间")
  private String createTime;

}
