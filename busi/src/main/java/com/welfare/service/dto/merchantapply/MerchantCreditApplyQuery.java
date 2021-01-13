package com.welfare.service.dto.merchantapply;

import com.welfare.common.annotation.Query;
import com.welfare.common.constants.WelfareConstant;
import com.welfare.service.enums.ApprovalStatus;
import com.welfare.service.utils.PageReq;
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
public class MerchantCreditApplyQuery extends PageReq {

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
  private String applyType;

  /**
   * 审批状态
   */
  @ApiModelProperty("审批状态(通过：AUDIT_SUCCESS， 不通过：AUDIT_FAILED, 待审核：AUDITING)")
  @Query(type = Query.Type.EQUAL)
  private String approvalStatus;

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
  @ApiModelProperty("申请开始时间(yyyy-MM-dd HH:mm:ss)")
  @Query(type = Query.Type.GREATER_THAN, propName = "applyTime")
  private Date applyTimeStart;

  /**
   * 申请结束时间
   */
  @ApiModelProperty("申请结束时间(yyyy-MM-dd HH:mm:ss)")
  @Query(type = Query.Type.LESS_THAN, propName = "applyTime")
  private Date applyTimeEnd;

  /**
   * 审批开始时间
   */
  @ApiModelProperty("审批开始时间(yyyy-MM-dd HH:mm:ss)")
  @Query(type = Query.Type.GREATER_THAN, propName = "approvalTime")
  private Date approvalTimeStart;

  /**
   * 审批结束时间
   */
  @ApiModelProperty("审批结束时间(yyyy-MM-dd HH:mm:ss)")
  @Query(type = Query.Type.LESS_THAN, propName = "approvalTime")
  private Date approvalTimeEnd;
}