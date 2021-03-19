package com.welfare.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author hao.yin
 * @version 1.0.0
 * @date 2021/1/8 2:26 PM
 */
@Data
@NoArgsConstructor
public class MerchantDetailDTO {

  @ApiModelProperty("id")
  private Long id;
  /**
   * 员工自主充值
   */
  @ApiModelProperty("员工自主充值")
  private String selfRecharge;

  @ApiModelProperty("员工自主充值(字典转义)")
  private String selfRechargeName;
  /**
   * 商户名称
   */
  @ApiModelProperty("商户名称")
  private String merName;
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
  private String merTypeName;
  /**
   * 身份属性
   */
  @ApiModelProperty("身份属性  supplier:供应商；customer:客户,多个用，号分隔")
  private String merIdentity;
  @ApiModelProperty("身份属性(字典转义)")
  private String merIdentityName;
  /**
   * 合作方式
   */
  @ApiModelProperty("合作方式")
  private String merCooperationMode;

  @ApiModelProperty("合作方式(字典转义)")
  private String merCooperationModeName;

  /**
   * 信用额度
   */
  @ApiModelProperty("信用额度")
  private BigDecimal creditLimit;

  @ApiModelProperty("充值额度")
  private BigDecimal rechargeLimit;

  /**
   * 剩余信用额度
   */
  @ApiModelProperty("剩余信用额度")
  private BigDecimal remainingLimit;

  /**
   * 目前余额
   */
  @ApiModelProperty("目前余额")
  private BigDecimal currentBalance;
  @ApiModelProperty("返利余额")
  private BigDecimal rebateLimit;
  /**
   * 创建日期
   */
  @ApiModelProperty("创建日期")
  private Date createTime;

  @ApiModelProperty("备注")
  private String remark;
  List<MerchantAddressDTO> addressList;

  @ApiModelProperty("员工卡消费明细门店显示")
  private String billDetailShowStoreName;
}
