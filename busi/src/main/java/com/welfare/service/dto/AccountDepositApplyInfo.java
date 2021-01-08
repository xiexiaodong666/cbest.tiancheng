package com.welfare.service.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
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
public class AccountDepositApplyInfo {

  /**
   * 申请id
   */
  @ApiModelProperty("申请id")
  @ExcelProperty("编号")
  private Integer id;

  /**
   * 充值账户个数
   */
  @ApiModelProperty("充值账户个数")
  @ExcelProperty("数量")
  private Integer rechargeNum;

  /**
   * 申请充值总额
   */
  @ApiModelProperty("申请充值总额")
  @ExcelProperty("数申请充值总额量")
  private BigDecimal rechargeAmount;

  /**
   * 申请人
   */
  @ApiModelProperty("申请人")
  @ExcelProperty("申请人")
  private String applyUser;

  /**
   * 创建日期
   */
  @ApiModelProperty("申请时间")
  @ExcelProperty("申请时间")
  private Date applyTime;

  /**
   * 申请备注
   */
  @ApiModelProperty("申请备注")
  @ExcelProperty("备注")
  private String applyRemark;

  /**
   * 审批状态
   */
  @ApiModelProperty("审批状态(通过：AUDIT_SUCCESS， 不通过：AUDIT_FAILED, 待审核：AUDITING)")
  @ExcelProperty("审批状态")
  private String approvalStatus;

  /**
   * 审批人
   */
  @ApiModelProperty("审批人")
  @ExcelProperty("审批人")
  private String approvalUser;

  /**
   * 审批时间
   */
  @ApiModelProperty("审批时间")
  @ExcelProperty("审批时间")
  private Date approvalTime;

  /**
   * 审批备注
   */
  @ApiModelProperty("审批备注")
  @ExcelProperty("审批备注")
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
}