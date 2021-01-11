package com.welfare.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AccountDepositDTO {

    @ApiModelProperty("支付交易流水号")
    private String payTradeNo;

    @ApiModelProperty("h5支付链接，有效期为5分钟")
    private String h5Url;
}
