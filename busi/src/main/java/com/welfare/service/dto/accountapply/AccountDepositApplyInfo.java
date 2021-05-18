package com.welfare.service.dto.accountapply;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 员工账号额度申请详情
 * @author duanhy
 * @version 1.0.0
 * @description
 * @date 2021/1/6  5:28 PM
 */
@Data
@NoArgsConstructor
@ApiModel("员工账号充值申请信息")
@ExcelIgnoreUnannotated
public class AccountDepositApplyInfo {

  /**
   * 申请id
   */
  @ApiModelProperty("申请id")
  @ExcelProperty(value = "编号", index = 0)
  private String id;

  /**
   * 充值账户个数
   */
  @ApiModelProperty("充值账户个数")
  @ExcelProperty(value = "数量", index = 1)
  private Integer rechargeNum;

  /**
   * 申请充值总额
   */
  @ApiModelProperty("申请充值总额")
  @ExcelProperty(value = "数申请充值总额量", index = 2)
  private BigDecimal rechargeAmount;

  /**
   * 福利类型名称
   */
  @ApiModelProperty("福利类型名称")
  @NotEmpty(message = "余额类型名称为空")
  @ExcelProperty(value = "福利类型", index = 3)
  private String merAccountTypeName;

  /**
   * 申请人
   */
  @ApiModelProperty("申请人")
  @ExcelProperty(value = "申请人", index = 4)
  private String applyUser;

  /**
   * 创建日期
   */
  @ApiModelProperty("申请时间(yyyy-MM-dd HH:mm:ss)")
  @ExcelProperty(value = "申请时间", index = 5)
  private Date applyTime;

  /**
   * 申请备注
   */
  @ApiModelProperty("申请备注")
  @ExcelProperty(value = "备注", index = 6)
  private String applyRemark;

  /**
   * 审批状态
   */
  @ApiModelProperty("审批状态(通过：AUDIT_SUCCESS， 不通过：AUDIT_FAILED, 待审核：AUDITING)")
  @ExcelProperty(value = "审批状态", index = 7)
  private String approvalStatus;

  /**
   * 审批状态描述
   */
  @ApiModelProperty("审批状态描述")
  private String approvalStatusDesc;

  /**
   * 审批人
   */
  @ApiModelProperty("审批人")
  @ExcelProperty(value = "审批人", index = 8)
  private String approvalUser;

  /**
   * 审批时间
   */
  @ApiModelProperty("审批时间(yyyy-MM-dd HH:mm:ss)")
  @ExcelProperty(value = "审批时间", index = 9)
  private Date approvalTime;

  /**
   * 审批备注
   */
  @ApiModelProperty("审批备注")
  @ExcelProperty(value = "审批备注", index = 10)
  private String approvalRemark;

  /**
   * 福利类型
   */
  @ApiModelProperty("福利类型")
  @NotEmpty(message = "余额类型为空")
  private String merAccountTypeCode;


  /**
   * 审批类型
   */
  @ApiModelProperty("审批类型（单个：SINGLE，批量：BATCH）")
  private String approvalType;

  /**
   * 充值类型
   */
  @ApiModelProperty("充值类型（批发采购充值：wholesaleCreditLimitApply  福利充值：welfareApply)")
  private String applyType;

  /**
   * 充值状态
   */
  @ApiModelProperty("充值状态(成功:SUCCESS  失败:NO  待充值:INIT)")
  private String rechargeStatus;
}