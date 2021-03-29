package com.welfare.service.dto.paymentChannel;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/3/25 5:44 下午
 */
@Data
public class PaymentChannelMerchantDTO {

  @ApiModelProperty("商户编码")
  private String merchantCode;

  @ApiModelProperty("商户名称")
  private String merchantName;

  @ApiModelProperty("支付渠道：xx/xxx/xxxx")
  private String paymentChannels;
}
