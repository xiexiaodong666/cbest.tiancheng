package com.welfare.service.dto.merchantapply;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.util.List;

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

  @ApiModelProperty("请求id（用于幂等处理，UUID即可）")
  @NotEmpty(message = "requestId为空")
  private String requestId;

  /**
   * 商户编码
   */
  @ApiModelProperty("商户编码")
  private String merCode;

  /**
   * 申请类型
   */
  @ApiModelProperty("商户额度申请类型（充值额度：rechargeLimit，余额：currentBalance, 剩余信用额度:remainingLimit，信用额度:creditLimit，消耗返点:rebateLimit）")
  @NotEmpty(message = "商户额度申请类型为空")
  private String applyType;

  /**
   * 金额
   */
  @ApiModelProperty("金额")
  private BigDecimal balance;

  /**
   * 备注
   */
  @ApiModelProperty("备注")
  private String remark;

  /**
   * 附件
   */
  @ApiModelProperty("附件(文件地址)")
  private List<String> enclosures;
}