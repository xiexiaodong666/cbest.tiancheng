package com.welfare.service.dto;

import com.welfare.common.constants.WelfareConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author duanhy
 * @version 1.0.0
 * @description
 * @date 2021/1/7  7:29 PM
 */
@Data
@NoArgsConstructor
@ApiModel("修改商户额度申请审批请求")
public class MerchantCreditApplyUpdateReq {

  /**
   * 申请id
   */
  @ApiModelProperty("申请id")
  @NotNull(message = "申请id为空")
  private Long id;

  /**
  /**
   * 申请类型
   */
  @ApiModelProperty("商户额度申请类型（充值额度：rechargeLimit，余额：currentBalance, 剩余信用额度:remainingLimit，信用额度:creditLimit，消耗返点:rebateLimit）")
  @NotEmpty(message = "商户额度申请类型为空")
  private WelfareConstant.MerCreditType applyType;

  /**
   * 金额
   */
  @ApiModelProperty("金额")
  @DecimalMin(value = "0", message = "金额不能小于0")
  private BigDecimal balance;

  /**
   * 备注
   */
  @ApiModelProperty("备注")
  private String remark;

  /**
   * 附件
   */
  @ApiModelProperty("附件（文件地址）")
  private String enclosure;
}

