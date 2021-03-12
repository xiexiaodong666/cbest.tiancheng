package com.welfare.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/3/11 5:15 下午
 */
@Data
public class PaymentChannelDTO {

  @ApiModelProperty("支付渠道编码")
  private String paymentChannelCode;

  @ApiModelProperty("支付渠道名称")
  private String paymentChannelName;
}
