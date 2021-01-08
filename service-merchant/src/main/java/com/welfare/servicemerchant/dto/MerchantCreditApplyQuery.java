package com.welfare.servicemerchant.dto;

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
  private String merName;

  /**
   * 申请类型
   */
  @ApiModelProperty("商户额度申请类型（充值额度：RECHARGE_LIMIT，余额：BALANCE, 剩余信用额度:REMAINING_LIMIT，信用额度S:CREDIT_LIMIT，消耗返点:REBATE_LIMIT）")
  private String applyType;

  /**
   * 申请人
   */
  @ApiModelProperty("申请人")
  private String createUser;

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