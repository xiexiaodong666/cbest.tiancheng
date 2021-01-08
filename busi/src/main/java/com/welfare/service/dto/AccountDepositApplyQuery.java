package com.welfare.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author duanhy
 * @version 1.0.0
 * @description
 * @date 2021/1/7  4:55 PM
 */
@Data
@NoArgsConstructor
public class AccountDepositApplyQuery {

  /**
   * 申请人
   */
  @ApiModelProperty("申请人")
  private String applyUser;

  /**
   * 审批状态
   */
  @ApiModelProperty("审批状态(通过：AUDIT_SUCCESS， 不通过：AUDIT_FAILED, 待审核：AUDITING)")
  private String approvalStatus;

  /**
   * 审批人
   */
  @ApiModelProperty("审批人")
  private String approvalUser;

  /**
   * 申请开始时间
   */
  @ApiModelProperty("申请开始时间")
  private Date applyTimeStart;

  /**
   * 申请结束时间
   */
  @ApiModelProperty("申请结束时间")
  private Date applyTimeEnd;

  /**
   * 审批开始时间
   */
  @ApiModelProperty("审批开始时间")
  private Date approvalTimeStart;

  /**
   * 审批结束时间
   */
  @ApiModelProperty("审批结束时间")
  private Date approvalTimeEnd;
}