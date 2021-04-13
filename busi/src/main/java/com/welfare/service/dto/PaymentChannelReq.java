package com.welfare.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/3/12 11:39 上午
 */
@Data
public class PaymentChannelReq {

  @ApiModelProperty("商户编码")
  private String merCode;

  @ApiModelProperty("是否过滤特定渠道(支付宝、微信)")
  private Boolean filtered;
}
