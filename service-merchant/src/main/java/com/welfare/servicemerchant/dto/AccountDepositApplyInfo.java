package com.welfare.servicemerchant.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

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
  private Integer id;

  /**
   * 员工名称
   */
  @ApiModelProperty("员工名称")
  private String accountName;

  /**
   * 充值账户个数
   */
  @ApiModelProperty("充值账户个数")
  private Integer rechargeNum;

  /**
   * 申请充值总额
   */
  @ApiModelProperty("申请充值总额")
  private BigDecimal rechargeAmount;

  /**
   * 申请人
   */
  @ApiModelProperty("申请人")
  private String applyUser;

  /**
   * 创建日期
   */
  @ApiModelProperty("申请时间")
  private Date createTime;

  /**
   * 申请备注
   */
  @ApiModelProperty("申请备注")
  private String applyRemark;

  /**
   * 审批状态
   */
  @ApiModelProperty("审批状态")
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
   * 申请人
   */
  @ApiModelProperty("余额类型（待定义）")
  private String balanceType;
}