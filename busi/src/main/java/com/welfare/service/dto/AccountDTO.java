package com.welfare.service.dto;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
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
@ExcelIgnoreUnannotated
public class AccountDTO implements Serializable {






  /**
   * id
   */
  @ApiModelProperty("id")  @JsonSerialize(using = ToStringSerializer.class)
  @TableId
  @ExcelProperty(value = "id")
  private Long id;

  /**
   * 商户名称
   */
  @ApiModelProperty("商户名称")
  @ExcelProperty(value = "商户名称")
  private String merName;
  /**
   * 商户编码
   */
  @ApiModelProperty("商户编码")
  @ExcelProperty(value = "商户编码")
  private String merCode;

  /**
   * 员工名称
   */
  @ApiModelProperty("员工名称")
  @ExcelProperty(value = "员工名称")
  private String accountName;
  /**
   * 员工账号
   */
  @ApiModelProperty("员工账号")
  @ExcelProperty(value = "员工账号")
  private String accountCode;

  /**
   * 账号状态
   */
  @ApiModelProperty("账号状态")
  private String accountStatus;
  /**
   * 账号状态文字
   */
  @ExcelProperty(value = "账号状态(1正常2禁用)")
  private String accountStatusString;
  /**
   * 所属部门
   */
  @ApiModelProperty("所属部门Code")
  @ExcelProperty(value = "所属部门Code")
  private String departmentCode;
  /**
   * 所属部门
   */
  @ApiModelProperty("所属部门")
  @ExcelProperty(value = "所属部门")
  private String departmentName;
  /**
   * 员工类型编码名称
   */
  @ApiModelProperty("员工类型编码名称")
  @ExcelProperty(value = "员工类型编码名称")
  private String accountTypeName;

  /**
   * 最大授权额度
   */
  @ApiModelProperty("最大授权额度")
  @ExcelProperty(value = "最大授权额度")
  private BigDecimal maxQuota;
  /**
   * 剩余授权额度
   */
  @ApiModelProperty("剩余授权额度")
  @ExcelProperty(value = "剩余授权额度")
  private BigDecimal surplusQuota;
  /**
   * 员工卡号
   */
  @ApiModelProperty("员工卡号(如果存在多个逗号分隔)")
  @ExcelProperty(value = "员工卡号(如果存在多个逗号分隔)")
  private String cardId;

  /**
   * 账户余额
   */
  @ApiModelProperty("账户余额")
  @ExcelProperty(value = "账户余额")
  private BigDecimal accountBalance;

  /**
   * 是否绑卡(1绑定0未绑定)
   */
  @ApiModelProperty("是否绑卡(1绑定0未绑定)")
  private Integer binding;

}
