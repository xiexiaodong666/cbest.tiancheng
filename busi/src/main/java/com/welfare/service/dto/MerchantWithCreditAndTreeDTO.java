package com.welfare.service.dto;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author hao.yin
 * @version 1.0.0
 * @date 2021/1/8 2:26 PM
 */
@Data
@NoArgsConstructor
@ExcelIgnoreUnannotated
public class MerchantWithCreditAndTreeDTO extends Tree{

  @ApiModelProperty("id")
  private Long id;
  /**
   * 商户名称
   */
  @ApiModelProperty("商户名称")
  @ExcelProperty(value = "商户名称",index = 0)
  private String merName;
  /**
   * 员工自主充值
   */
  @ApiModelProperty("员工自主充值")
  private String selfRecharge;
  @ApiModelProperty("员工自主充值(字典转义)")
  private String selfRechargeName;
  @ApiModelProperty("父级编码")
  private String departmentParent;
  /**
   * 商户代码
   */
  @ApiModelProperty("商户代码")
  @ExcelProperty(value = "商户代码",index = 1)
  private String code;
  /**
   * 商户代码
   */
  @ApiModelProperty("商户代码")
  private String merCode;
  /**
   * 商户类型
   */
  @ApiModelProperty("商户类型")
  private String merType;
  @ApiModelProperty("商户类型(字典转义)")
  @ExcelProperty(value = "商户类型",index = 2)
  private String merTypeName;
  /**
   * 身份属性
   */
  @ApiModelProperty("身份属性")
  private String merIdentity;
  @ApiModelProperty("身份属性(字典转义)")
  private String merIdentityName;
  /**
   * 合作方式
   */
  @ApiModelProperty("合作方式")
  private String merCooperationMode;

  @ApiModelProperty("合作方式(字典转义)")
  @ExcelProperty(value = "合作方式",index = 3)
  private String merCooperationModeName;

  /**
   * 信用额度
   */
  @ApiModelProperty("信用额度")
  @ExcelProperty(value = "信用额度",index = 4)
  private BigDecimal creditLimit;

  /**
   * 剩余信用额度
   */
  @ApiModelProperty("剩余信用额度")
  @ExcelProperty(value = "剩余额度",index = 5)
  private BigDecimal remainingLimit;

  /**
   * 目前余额
   */
  @ApiModelProperty("目前余额")
  @ExcelProperty(value = "余额",index = 6)
  private BigDecimal currentBalance;
  /**
   * 创建日期
   */
  @ApiModelProperty("创建日期")
  @ExcelProperty(value = "创建时间",index = 7)
  private Date createTime;

}
