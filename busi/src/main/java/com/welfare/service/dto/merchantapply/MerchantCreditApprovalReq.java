package com.welfare.service.dto.merchantapply;

import com.welfare.service.enums.ApprovalStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

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
  @NotEmpty(message = "申请id为空")
  private String id;

  /**
   * 审批状态
   */
  @ApiModelProperty("审批状态(通过：AUDIT_SUCCESS， 不通过：AUDIT_FAILED)")
  @NotNull(message = "审批状态为空")
  private ApprovalStatus approvalStatus;

  /**
   * 审批备注
   */
  @ApiModelProperty("审批备注")
  private String approvalRemark;
}