package com.welfare.service.dto.paymentChannel;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/3/25 4:35 下午
 */
@Data
public class PayChannelConfigSimpleDTO {

  @ApiModelProperty("商户主键Id")
  private String merchantId;

  @ApiModelProperty("商户编码")
  private String merchantCode;

  @ApiModelProperty("商户编码")
  private String merchantName;

  @ApiModelProperty("关联消费门店数")
  private Integer consumeStoreCount;

  @ApiModelProperty("支付渠道(xx/xxx/xxxx)")
  private String paymentChannels;
}
