package com.welfare.service.dto.merchantapply;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author duanhy
 * @version 1.0.0
 * @description
 * @date 2021/1/12  2:01 PM
 */
@Data
@ExcelIgnoreUnannotated
public class MerchantCreditApplyExcelInfo {

  /**
   * 申请id
   */
  @ApiModelProperty("申请id")
  @ExcelProperty(value = "编号")
  private String id;

  /**
   * 申请类型
   */
  @ApiModelProperty("商户额度申请类型（充值额度：rechargeLimit，余额：currentBalance, 剩余信用额度:remainingLimit，信用额度:creditLimit，消耗返点:rebateLimit, 批发采购信用额度:wholesaleCreditLimit）")
  @ExcelProperty("申请类型")
  private String applyType;

  /**
   * 商户名称
   */
  @ApiModelProperty("商户名称")
  @ExcelProperty("商户")
  private String merName;

  /**
   * 商户编码
   */
  @ApiModelProperty("商户编码")
  private String merCode;

  /**
   * 合作方式
   */
  @ApiModelProperty("合作方式（待定）")
  @ExcelProperty("合作方式")
  private String merCooperationMode;

  /**
   * 金额
   */
  @ApiModelProperty("金额")
  @ExcelProperty("金额")
  private BigDecimal balance;

  /**
   * 申请人
   */
  @ApiModelProperty("申请人")
  @ExcelProperty("申请人")
  private String applyUser;

  /**
   * 申请时间
   */
  @ApiModelProperty("申请时间(yyyy-MM-dd HH:mm:ss)")
  @ExcelProperty("申请时间")
  private Date applyTime;

  /**
   * 申请备注
   */
  @ApiModelProperty("申请备注")
  @ExcelProperty("备注")
  private String remark;

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
  @ApiModelProperty("审批时间(yyyy-MM-dd HH:mm:ss)")
  @ExcelProperty("审批时间")
  private Date approvalTime;

  /**
   * 审批备注
   */
  @ApiModelProperty("审批备注")
  @ExcelProperty("审批备注")
  private String approvalRemark;

  /**
   * 附件
   */
  @ApiModelProperty("附件")
  private String enclosure;
}