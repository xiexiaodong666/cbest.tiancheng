package com.welfare.service.dto.merchantapply;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.util.List;

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
  @NotEmpty(message = "申请id为空")
  private String id;

  /**
  /**
   * 申请类型
   */
  @ApiModelProperty("商户额度申请类型（充值额度：rechargeLimit，余额：currentBalance, 剩余信用额度:remainingLimit，信用额度:creditLimit，消耗返点:rebateLimit）")
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
  @ApiModelProperty("附件（文件地址）")
  private List<String> enclosures;
}

