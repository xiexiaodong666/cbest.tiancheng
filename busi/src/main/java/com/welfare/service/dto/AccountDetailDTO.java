package com.welfare.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * @author yaoxiao
 * @version 0.0.1
 * @date 2021/1/9 16:48
 */
@Data
@ApiModel("账户详情")
public class AccountDetailDTO {
  /**
   * 员工名称
   */
  @ApiModelProperty("员工名称")
  private String accountName;
  /**
   * 员工账号
   */
  @ApiModelProperty("员工账号")
  private String accountCode;

  /**
   * 账号状态
   */
  @ApiModelProperty("账号状态")
  private String accountStatus;


  /**
   * 员工类型编码名称
   */
  @ApiModelProperty("员工类型编码")
  private String accountTypeCode;
  /**
   * 员工类型编码名称
   */
  @ApiModelProperty("员工类型编码名称")
  private String accountTypeName;
  /**
   * 所属部门
   */
  @ApiModelProperty("所属部门Code")
  private String departmentCode;
  /**
   * 所属部门
   */
  @ApiModelProperty("所属部门")
  private String departmentName;

  /**
   * 商户CODE
   */
  @ApiModelProperty("商户CODE")
  private String merCode;

  /**
   * 商户名称
   */
  @ApiModelProperty("商户名称")
  private String merName;


  /**
   * 最大授权额度
   */
  @ApiModelProperty("最大授权额度")
  private BigDecimal maxQuota;
  /**
   * 剩余授权额度
   */
  @ApiModelProperty("剩余授权额度")
  private BigDecimal surplusQuota;

  /**
   * 账户余额
   */
  @ApiModelProperty("账户余额")
  private BigDecimal accountBalance;

  /**
   * 备注
   */
  @ApiModelProperty("备注")
  private String remark;
  /**
   * 手机号
   */
  @ApiModelProperty("手机号")
  private String phone;
  /**
   * 创建时间
   */
  @ApiModelProperty("创建时间")
  private Date createTime;
}
