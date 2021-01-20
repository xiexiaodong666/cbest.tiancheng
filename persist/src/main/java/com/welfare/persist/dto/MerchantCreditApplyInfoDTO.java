package com.welfare.persist.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author duanhy
 * @version 1.0.0
 * @description
 * @date 2021/1/13  10:42 AM
 */
@Data
public class MerchantCreditApplyInfoDTO {

  /**
   * 申请id
   */
  private Long id;

  /**
   * 申请类型
   */
  private String applyType;

  /**
   * 商户名称
   */
  private String merName;

  /**
   * 商户编码
   */
  private String merCode;

  /**
   * 合作方式
   */
  private String merCooperationMode;

  /**
   * 金额
   */
  private BigDecimal balance;

  /**
   * 申请人
   */
  private String applyUser;

  /**
   * 申请时间
   */
  private Date applyTime;

  /**
   * 申请备注
   */
  private String remark;

  /**
   * 审批状态
   */
  private String approvalStatus;

  /**
   * 审批人
   */

  private String approvalUser;

  /**
   * 审批时间
   */

  private Date approvalTime;

  /**
   * 审批备注
   */
  private String approvalRemark;

  /**
   * 附件
   */
  private String enclosure;
}