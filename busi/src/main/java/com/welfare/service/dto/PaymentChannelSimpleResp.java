package com.welfare.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/4/26 6:05 下午
 */
@Data
public class PaymentChannelSimpleResp {

    @ApiModelProperty("支付渠道编码")
    private String paymentChannelCode;

    @ApiModelProperty("支付渠道名称")
    private String paymentChannelName;
}
