package com.welfare.service.dto.accountapply;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

/**
 * @author duanhy
 * @version 1.0.0
 * @description
 * @date 2021/1/7  5:25 PM
 */
@Data
@NoArgsConstructor
@ApiModel("账号额度审批请求")
public class AccountDepositApprovalRequest {


  @ApiModelProperty("申请id")
  @NotEmpty(message = "id不能为空")
  private String id;

  /**
   * 审批状态
   */
  @ApiModelProperty("审批状态(通过：AUDIT_SUCCESS， 不通过：AUDIT_FAILED)")
  @NotEmpty(message = "审批状态不能为空")
  private String approvalStatus;

  /**
   * 申请备注
   */
  @ApiModelProperty("申请备注")
  private String applyRemark;

  /**
   * 审批人
   */
  @ApiModelProperty("审批人")
  @NotEmpty(message = "审批人不能为空")
  private String approvalUser;

  /**
   * 审批意见
   */
  @ApiModelProperty("审批意见")
  private String approvalOpinion;
}