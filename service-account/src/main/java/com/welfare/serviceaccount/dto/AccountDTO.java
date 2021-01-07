package com.welfare.serviceaccount.dto;

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
   * 所属商户
   */
  @ApiModelProperty("所属商户")
  private String merCode;
  /**
   * 商户名称
   */
  @ApiModelProperty("商户名称")
  private String merName;
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
   * 员工状态
   */
  @ApiModelProperty("员工状态")
  private String staffStatus;
  /**
   * 账号状态
   */
  @ApiModelProperty("账号状态")
  private String accountStatus;
  /**
   * 所属部门
   */
  @ApiModelProperty("所属部门")
  private String storeCode;
  /**
   * 员工类型编码
   */
  @ApiModelProperty("员工类型编码")
  private String accountTypeCode;
  /**
   * 员工类型编码名称
   */
  @ApiModelProperty("员工类型编码名称")
  private String accountTypeName;
  /**
   * 是否绑卡
   */
  @ApiModelProperty("是否绑卡")
  private Integer binding;
  /**
   * 员工卡号
   */
  @ApiModelProperty("员工卡号(如果存在多个逗号分隔)")
  private String cardInfo;
  /**
   * 账户余额
   */
  @ApiModelProperty("账户余额")
  private BigDecimal accountBalance;
  /**
   * 手机号
   */
  @ApiModelProperty("手机号")
  private String phone;
  /**
   * 是否激活
   */
  @ApiModelProperty("是否激活")
  private Integer active;

}
