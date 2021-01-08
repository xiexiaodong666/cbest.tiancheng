package com.welfare.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author duanhy
 * @version 1.0.0
 * @description
 * @date 2021/1/7  5:18 PM
 */
@Data
@NoArgsConstructor
@ApiModel("账号申请请求")
public class DepositApplyRequest {


  @ApiModelProperty("请求id（用于幂等处理）")
  @NotEmpty(message = "requestId为空")
  private String requestId;

  /**
   * 申请备注
   */
  @ApiModelProperty("申请备注")
  private String applyRemark;

  /**
   * 申请人
   */
  @ApiModelProperty("申请人")
  @NotEmpty(message = "申请人为空")
  private String applyUser;

  /**
   * 余额类型
   */
  @ApiModelProperty("余额类型（待定义）")
  @NotEmpty(message = "余额类型为空")
  private String balanceType;

  /**
   * 审批类型
   */
  @ApiModelProperty("审批类型（单个：SINGLE，批量：BATCH）")
  @NotEmpty(message = "审批类型为空")
  private String approvalType;

  @ApiModelProperty("申请员工额度信息")
  @NotNull(message = "申请员工额度信息为空")
  private AccountDepositRequest info;
}