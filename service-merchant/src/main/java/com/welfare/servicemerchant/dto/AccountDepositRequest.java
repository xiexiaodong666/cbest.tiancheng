package com.welfare.servicemerchant.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@NoArgsConstructor
public class AccountDepositRequest {

  /**
   * 员工账号
   */
  @ApiModelProperty(name = "员工账号", required = true)
  private String accountCode;

  /**
   * 申请充值总额
   */
  @ApiModelProperty("申请充值金额")
  private BigDecimal rechargeAmount;
}