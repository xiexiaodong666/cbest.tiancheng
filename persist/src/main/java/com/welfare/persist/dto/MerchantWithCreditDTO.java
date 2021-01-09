package com.welfare.persist.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/1/8 2:26 PM
 */
@Data
@NoArgsConstructor
public class MerchantWithCreditDTO {

  @ApiModelProperty("id")
  private Long id;
  /**
   * 商户名称
   */
  @ApiModelProperty("商户名称")
  private String merName;
  /**
   * 商户代码
   */
  @ApiModelProperty("商户代码")
  private String merCode;
  /**
   * 商户类型
   */
  @ApiModelProperty("商户类型")
  private String merType;
  /**
   * 身份属性
   */
  @ApiModelProperty("身份属性")
  private String merIdentity;
  /**
   * 合作方式
   */
  @ApiModelProperty("合作方式")
  private String merCooperationMode;

  /**
   * 信用额度
   */
  @ApiModelProperty("信用额度")
  private BigDecimal creditLimit;

  /**
   * 剩余信用额度
   */
  @ApiModelProperty("剩余信用额度")
  private BigDecimal remainingLimit;

  /**
   * 目前余额
   */
  @ApiModelProperty("目前余额")
  private BigDecimal currentBalance;
  /**
   * 创建日期
   */
  @ApiModelProperty("创建日期")
  private Date createTime;

}
