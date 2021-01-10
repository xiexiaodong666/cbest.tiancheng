package com.welfare.service.dto;

import com.welfare.common.annotation.Query;
import com.welfare.common.constants.WelfareConstant;
import com.welfare.service.enums.ApprovalStatus;
import com.welfare.service.enums.MerchantCreditApplyType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author duanhy
 * @version 1.0.0
 * @description
 * @date 2021/1/7  7:56 PM
 */
@Data
@NoArgsConstructor
@ApiModel("商户额度申请审批查询")
public class MerchantCreditApplyQuery {

  /**
   * 商户名称
   */
  @ApiModelProperty("商户名称")
  @Query(type = Query.Type.INNER_LIKE)
  private String merName;

  /**
   * 申请类型
   */
  @ApiModelProperty("商户额度申请类型（充值额度：rechargeLimit，余额：currentBalance, 剩余信用额度:remainingLimit，信用额度:creditLimit，消耗返点:rebateLimit）")
  @Query(type = Query.Type.EQUAL)
  private WelfareConstant.MerCreditType applyType;

  /**
   * 审批状态
   */
  @ApiModelProperty("审批状态(通过：AUDIT_SUCCESS， 不通过：AUDIT_FAILED, 待审核：AUDITING)")
  @Query(type = Query.Type.EQUAL)
  private ApprovalStatus approvalStatus;

  /**
   * 审批人
   */
  @ApiModelProperty("申请人")
  @Query(type = Query.Type.INNER_LIKE)
  private String applyUser;

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