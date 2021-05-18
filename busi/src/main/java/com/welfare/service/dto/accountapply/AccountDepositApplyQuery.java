package com.welfare.service.dto.accountapply;

import com.welfare.common.annotation.Query;
import com.welfare.service.utils.PageReq;
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
public class AccountDepositApplyQuery extends PageReq{

  /**
   * 申请人
   */
  @ApiModelProperty("申请人")
  @Query(type = Query.Type.RIGHT_LIKE)
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
  @Query(type = Query.Type.RIGHT_LIKE)
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

  /**
   * 福利类型code
   */
  @ApiModelProperty("福利类型code")
  @Query(type = Query.Type.EQUAL)
  private String merAccountTypeCode;

  /**
   * 充值类型
   */
  @ApiModelProperty("充值类型（批发采购充值：wholesaleCreditLimitApply  福利充值：welfareApply)")
  @Query(type = Query.Type.EQUAL, propName = "applyType")
  private String applyType;

  /**
   * 充值状态
   */
  @ApiModelProperty("充值状态(成功:SUCCESS  失败:NO  待充值:INIT)")
  @Query(type = Query.Type.EQUAL)
  private String rechargeStatus;
}