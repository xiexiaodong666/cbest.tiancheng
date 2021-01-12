package com.welfare.service.dto.accountapply;

import com.welfare.service.dto.AccountDepositRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
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


  @ApiModelProperty("请求id（用于幂等处理，UUID即可）")
  @NotEmpty(message = "requestId为空")
  private String requestId;

  /**
   * 申请备注
   */
  @ApiModelProperty("申请备注")
  private String applyRemark;

  /**
   * 福利类型
   */
  @ApiModelProperty("福利类型")
  @NotEmpty(message = "余额类型为空")
  private String merAccountTypeCode;

  /**
   * 福利类型名称
   */
  @ApiModelProperty("福利类型名称")
  @NotEmpty(message = "余额类型名称为空")
  private String merAccountTypeName;

  /**
   * 审批类型
   */
  @ApiModelProperty("审批类型（单个：SINGLE，批量：BATCH）")
  @NotEmpty(message = "审批类型为空")
  private String approvalType;

  @ApiModelProperty("申请员工额度信息")
  @NotNull(message = "申请员工额度信息为空")
  @Valid
  private AccountDepositRequest info;
}