package com.welfare.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author duanhy
 * @version 1.0.0
 * @description
 * @date 2021/1/7  7:13 PM
 */
@Data
@NoArgsConstructor
@ApiModel("商户额度申请审批请求")
public class MerchantCreditApprovalReq {


  @ApiModelProperty("申请id")
  private Long id;

  /**
   * 审批状态
   */
  @ApiModelProperty("审批状态(通过：AUDIT_SUCCESS， 不通过：AUDIT_FAILED)")
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
  private String approvalUser;
}