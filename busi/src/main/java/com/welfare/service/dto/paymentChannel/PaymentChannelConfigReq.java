package com.welfare.service.dto.paymentChannel;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Set;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/4/12 1:37 下午
 */
@Data
public class PaymentChannelConfigReq {

  @ApiModelProperty(value = "商户编码", required = true)
  @NotEmpty(message = "商户编码不能为空")
  private String merCode;

  @ApiModelProperty(value = "门店编码", required = true)
  @NotEmpty(message = "门店编码不能为空")
  private String storeCode;

  @ApiModelProperty(value = "消费场景", required = true)
  @NotEmpty(message = "消费场景不能为空")
  private String consumeType;
}
