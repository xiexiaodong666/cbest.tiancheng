package com.welfare.service.dto;

import com.welfare.common.annotation.Query;
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
  @Query(type = Query.Type.INNER_LIKE)
  private String applyUser;

  /**
   * 审批状态
   */
  @ApiModelProperty("审批状态(通过：AUDIT_SUCCESS， 不通过：AUDIT_FAILED, 待审核：AUDITING)")
  @Query(type = Query.Type.EQUAL)
  private String approvalStatus;

  /**
   * 审批人
   */
  @ApiModelProperty("审批人")
  @Query(type = Query.Type.INNER_LIKE)
  private String approvalUser;

  /**
   * 申请开始时间
   */
  @ApiModelProperty("申请开始时间")
  @Query(type = Query.Type.GREATER_THAN)
  private Date applyTimeStart;

  /**
   * 申请结束时间
   */
  @ApiModelProperty("申请结束时间")
  @Query(type = Query.Type.LESS_THAN)
  private Date applyTimeEnd;

  /**
   * 审批开始时间
   */
  @ApiModelProperty("审批开始时间")
  @Query(type = Query.Type.GREATER_THAN)
  private Date approvalTimeStart;

  /**
   * 审批结束时间
   */
  @ApiModelProperty("审批结束时间")
  @Query(type = Query.Type.LESS_THAN)
  private Date approvalTimeEnd;
}