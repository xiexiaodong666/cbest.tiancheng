package com.welfare.servicemerchant.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author duanhy
 * @version 1.0.0
 * @description
 * @date 2021/1/7  6:58 PM
 */
@Data
@NoArgsConstructor
public class MerchantCreditApplyInfo {

  /**
   * 申请id
   */
  @ApiModelProperty("申请id")
  private Long id;

  /**
   * 商户编码
   */
  @ApiModelProperty("商户编码")
  private String merCode;

  /**
   * 商户名称
   */
  @ApiModelProperty("商户名称")
  private String merName;

  /**
   * 申请类型
   */
  @ApiModelProperty("申请类型")
  private String applyType;

  /**
   * 合作方式
   */
  @ApiModelProperty("合作方式")
  private String merCooperationMode;

  /**
   * 金额
   */
  @ApiModelProperty("金额")
  private BigDecimal balance;

  /**
   * 申请备注
   */
  @ApiModelProperty("申请备注")
  private String remark;

  /**
   * 附件
   */
  @ApiModelProperty("附件")
  private String enclosure;

  /**
   * 申请时间
   */
  @ApiModelProperty("申请时间")
  private Date applyTime;

  /**
   * 审批状态
   */
  @ApiModelProperty("审批状态(通过：AUDIT_SUCCESS， 不通过：AUDIT_FAILED)")
  private String approvalStatus;

  /**
   * 审批人
   */
  @ApiModelProperty("审批人")
  private String approvalUser;

  /**
   * 审批时间
   */
  @ApiModelProperty("审批时间")
  private Date approvalTime;
  /**
   * 审批备注
   */
  @ApiModelProperty("审批备注")
  private String approvalRemark;
}