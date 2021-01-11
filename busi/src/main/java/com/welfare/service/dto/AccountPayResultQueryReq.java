package com.welfare.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AccountPayResultQueryReq {

    @ApiModelProperty(value = "账号")
    private String accountCode;

    @ApiModelProperty("支付交易流水号")
    private String payTradeNo;
}
