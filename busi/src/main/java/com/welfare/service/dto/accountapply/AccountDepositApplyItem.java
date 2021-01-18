package com.welfare.service.dto.accountapply;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author duanhy
 * @version 1.0.0
 * @description
 * @date 2021/1/7  4:41 PM
 */
@Data
@NoArgsConstructor
@ApiModel("账号余额申请明细")
public class AccountDepositApplyItem {

  /**
   * 员工名称
   */
  @ApiModelProperty("员工名称")
  private String accountName;

  /**
   * 员工账户
   */
  @ApiModelProperty("员工账户")
  private Long accountCode;

  /**
   * 手机号
   */
  @ApiModelProperty("手机号")
  private String phone;

  /**
   * 充值金额
   */
  @ApiModelProperty("充值金额")
  private BigDecimal rechargeAmount;

  /**
   * 机构编码
   */
  @ApiModelProperty("机构编码")
  private String departmentCode;

  /**
   * 机构名称
   */
  @ApiModelProperty("机构名称")
  private String departmentName;
}