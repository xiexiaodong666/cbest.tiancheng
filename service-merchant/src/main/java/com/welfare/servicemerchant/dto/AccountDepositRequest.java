package com.welfare.servicemerchant.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 账号余额申请请求类
 * @author duanhy
 * @version 1.0.0
 * @description
 * @date 2021/1/7  2:14 PM
 */
@Data
@ApiModel("账号余额申请")
public class AccountDepositRequest {

  /**
   * 员工账号
   */
  @ApiModelProperty("员工账号")
  private String accountCode;

  /**
   * 商户代码
   */
  @ApiModelProperty("商户代码")
  private String merCode;

  /**
   * 申请充值总额
   */
  @ApiModelProperty("申请充值金额")
  private BigDecimal rechargeAmount;

  /**
   * 申请备注
   */
  @ApiModelProperty("申请备注")
  private String applyRemark;

  /**
   * 创建人
   */
  @ApiModelProperty("创建人")
  private String createUser;
}