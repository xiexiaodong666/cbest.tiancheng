package com.welfare.service.dto.paymentChannel;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/3/25 4:13 下午
 */
@Data
public class PaymentChannelMerchantResp {

  @ApiModelProperty("商户编码")
  private String merchantCode;

  @ApiModelProperty("商户名称")
  private String merchantName;

  @ApiModelProperty("支付渠道")
  private List<PaymentChannelResp> paymentChannels;
}
