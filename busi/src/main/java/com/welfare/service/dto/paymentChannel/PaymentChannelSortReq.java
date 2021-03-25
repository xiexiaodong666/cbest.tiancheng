package com.welfare.service.dto.paymentChannel;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/3/25 4:13 下午
 */
@Data
public class PaymentChannelSortReq {

  @ApiModelProperty("支付渠道编码")
  @NotEmpty(message = "支付渠道编码不能为空")
  private String code;

  @ApiModelProperty("商户编码")
  @NotEmpty(message = "商户编码不能为空")
  private String merchantCode;

  @ApiModelProperty(value = "排序，从 1 开始升序", required = true)
  @NotNull(message = "排序权重不能为空")
  @Min(value = 1, message = "排序权重不能小于1")
  private Integer sorted;
}
