package com.welfare.persist.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("员工卡首页支付渠道")
public class AccountPaymentChannelDTO {

    @ApiModelProperty("账号支付渠道")
    private String paymentChannel;

    @ApiModelProperty("账号支付渠道名称")
    private String paymentChannelDesc;
}
