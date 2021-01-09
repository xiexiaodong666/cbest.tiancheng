package com.welfare.service.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;

/**
 * @author yaoxiao
 * @version 0.0.1
 * @date 2021/1/7 16:18
 */
@Data
@ApiModel("账户信息")
public class AccountDTO implements Serializable {






  /**
   * id
   */
  @ApiModelProperty("id")  @JsonSerialize(using = ToStringSerializer.class)
  @TableId
  private Long id;

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
   * 员工类型编码名称
   */
  @ApiModelProperty("员工类型编码名称")
  private String accountTypeName;

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
   * 员工卡号
   */
  @ApiModelProperty("员工卡号(如果存在多个逗号分隔)")
  private String cardId;

  /**
   * 账户余额
   */
  @ApiModelProperty("账户余额")
  private BigDecimal accountBalance;

  /**
   * 是否激活
   */
  @ApiModelProperty("是否激活")
  private Integer active;

}
