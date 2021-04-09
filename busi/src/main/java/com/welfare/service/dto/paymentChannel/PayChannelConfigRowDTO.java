package com.welfare.service.dto.paymentChannel;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/3/25 4:36 下午
 */
@Data
public class PayChannelConfigRowDTO {

  @ApiModelProperty("门店名称")
  private String storeName;

  @ApiModelProperty("门店编码")
  private String storeCode;

  @ApiModelProperty("消费场景和支付渠道")
  private List<ConsumeTypeAndPayChannel> consumeTypeAndPayChannels;


  @Data
  public static class ConsumeTypeAndPayChannel {

    @ApiModelProperty("消费场景")
    private String consumeType;

    @ApiModelProperty("勾选的支付渠道")
    private List<PaymentChannel> paymentChannels;

  }

  @Data
  public static class PaymentChannel {

    @ApiModelProperty("主键id，不传则新增支付渠道")
    private String id;

    @ApiModelProperty("支付渠道编码")
    private String paymentChannelCode;

    @ApiModelProperty("支付渠道名称")
    private String paymentChannelName;

    @ApiModelProperty("是否勾选")
    private Boolean selected;

    @ApiModelProperty("是否可选")
    private Boolean enable;
  }
}
