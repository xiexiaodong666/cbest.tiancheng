package com.welfare.service.dto.paymentChannel;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/3/25 4:12 下午
 */
@Data
public class PaymentChannelResp {

  @ApiModelProperty("主键id")
  private String id;

  @ApiModelProperty("支付渠道编码")
  private String code;

  @ApiModelProperty("支付渠道名称")
  private String name;

  @ApiModelProperty("排序：从 1 开始升序排列")
  private Integer sorted;

  @ApiModelProperty("商户编码")
  private String merchantCode;
}
