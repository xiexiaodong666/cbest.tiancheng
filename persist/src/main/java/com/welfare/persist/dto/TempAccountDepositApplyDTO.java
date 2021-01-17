package com.welfare.persist.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author duanhy
 * @version 1.0.0
 * @description
 * @date 2021/1/9  1:14 PM
 */
@Data
@NoArgsConstructor
public class TempAccountDepositApplyDTO {

  /**
   * id
   */
  @ApiModelProperty(name = "id")
  private String id;

  /**
   * 员工手机号
   */
  @ApiModelProperty(name = "员工（手机号）")
  private String phone;

  /**
   * 员工账号
   */
  @ApiModelProperty(name = "员工账号")
  private Long accountCode;

  /**
   * 员工姓名
   */
  @ApiModelProperty(name = "员工姓名")
  private String accountName;

  /**
   * 申请充值总额
   */
  @ApiModelProperty("申请充值金额")
  private BigDecimal rechargeAmount;

  /**
   * 所属机构编码
   */
  @ApiModelProperty("所属机构编码")
  private String departmentCode;

  /**
   * 所属机构名称
   */
  @ApiModelProperty("所属机构名称")
  private String departmentName;
}