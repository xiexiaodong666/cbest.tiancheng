package com.welfare.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ThirdPartyBarcodePaymentDTO {

    @ApiModelProperty("账户编码")
    private Long accountCode;

    @ApiModelProperty(value = "免密支付签名")
    private String passwordFreeSignature;
}
