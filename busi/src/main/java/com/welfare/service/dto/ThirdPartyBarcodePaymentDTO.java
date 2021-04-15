package com.welfare.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ThirdPartyBarcodePaymentDTO {

    @ApiModelProperty("账户编码")
    private Long accountCode;
    @ApiModelProperty("账户名称")
    private String accountName;
    @ApiModelProperty("电话")
    private String phone;
    @ApiModelProperty("账户余额")
    private BigDecimal accountBalance;
    @ApiModelProperty("授信余额")
    private BigDecimal surplusQuota;

    @ApiModelProperty(value = "免密支付签名")
    private String passwordFreeSignature;
}
