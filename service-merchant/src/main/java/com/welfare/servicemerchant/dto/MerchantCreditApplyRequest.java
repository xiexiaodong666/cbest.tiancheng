package com.welfare.servicemerchant.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author duanhy
 * @version 1.0.0
 * @description
 * @date 2021/1/7  6:57 PM
 */
@Data
@NoArgsConstructor
@ApiModel("商户额度申请请求")
public class MerchantCreditApplyRequest {

  /**
   * 商户编码
   */
  @ApiModelProperty("商户编码")
  private String merCode;

  /**
   * 申请类型
   */
  @ApiModelProperty("申请类型")
  private String applyType;

  /**
   * 金额
   */
  @ApiModelProperty("金额")
  private BigDecimal balance;

  /**
   * 申请人
   */
  @ApiModelProperty("申请人")
  private String applyUser;

  /**
   * 备注
   */
  @ApiModelProperty("备注")
  private String remark;

  /**
   * 附件
   */
  @ApiModelProperty("附件")
  private String enclosure;
}