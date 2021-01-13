package com.welfare.persist.dto.query;

import lombok.Data;

import java.util.Date;

/**
 * @author duanhy
 * @version 1.0.0
 * @description
 * @date 2021/1/13  10:45 AM
 */
@Data
public class MerchantCreditApplyQueryReq {

  /**
   * 商户名称
   */
  private String merName;

  /**
   * 申请类型
   */
  private String applyType;

  /**
   * 审批状态
   */
  private String approvalStatus;

  /**
   * 审批人
   */
  private String applyUser;

  /**
   * 审批人
   */
  private String approvalUser;

  /**
   * 申请开始时间
   */
  private Date applyTimeStart;

  /**
   * 申请结束时间
   */
  private Date applyTimeEnd;

  /**
   * 审批开始时间
   */
  private Date approvalTimeStart;

  /**
   * 审批结束时间
   */
  private Date approvalTimeEnd;
}