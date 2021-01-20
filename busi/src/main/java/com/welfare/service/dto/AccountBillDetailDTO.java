package com.welfare.service.dto;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author yaoxiao
 * @version 0.0.1
 * @date 2021/1/7 18:09
 */
@ApiModel("用户流水明细DTO")
@Data
@ExcelIgnoreUnannotated
public class AccountBillDetailDTO implements Serializable {
  /**
   * 消费门店名称
   */
  @ApiModelProperty("消费门店名称(交易方)")
  @ExcelProperty(value = "消费门店名称")
  private String storeName;

  /**
   * 交易类型
   */
  @ApiModelProperty("交易类型(消费、退款、充值等)")
  @ExcelProperty(value = "交易类型(出入账)code")
  private String transType;

  /**
   * 交易类型文字
   */
  @ExcelProperty(value = "交易类型value")
  private String transTypeString;

  /**
   * 交易总金额
   */
  @ApiModelProperty("交易总金额(金额)")
  @ExcelProperty(value = "交易总金额")
  private BigDecimal transAmount;

  /**
   * 账户余额
   */
  @ApiModelProperty("账户余额(余额)")
  @ExcelProperty(value = "账户余额")
  private BigDecimal accountBalance;

  /**
   * 创建时间
   */
  @ApiModelProperty("创建时间(日期)")
  @ExcelProperty(value = "创建时间")
  private Date createTime;

}
