package com.welfare.service.dto.paymentChannel;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/3/25 4:38 下午
 */
@Data
public class PayChannelConfigReq {

  @ApiModelProperty(value = "商户编码", required = true)
  @NotEmpty(message = "商户编码, 不能为空")
  private String merchantCode;

  @ApiModelProperty("供应商编码")
  private String supplierCode;

  @ApiModelProperty("消费场景")
  private List<String> consumeTypes;

  @ApiModelProperty("支付渠道编码")
  private List<String> paymentChannelCode;

  @ApiModelProperty("门店名称")
  private String storeName;
}
