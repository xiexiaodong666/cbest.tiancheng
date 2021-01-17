package com.welfare.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
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
  @ApiModelProperty(name = "员工(手机号)账号", required = true)
  private String phone;

  /**
   * 申请充值总额
   */
  @ApiModelProperty(value = "申请充值金额", required = true)
  @DecimalMin(value = "0", message = "金额不能小于0")
  @NotNull(message = "申请充值金额不能为空")
  private BigDecimal rechargeAmount;
}