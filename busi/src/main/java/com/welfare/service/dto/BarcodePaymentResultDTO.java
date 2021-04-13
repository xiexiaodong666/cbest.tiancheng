package com.welfare.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BarcodePaymentResultDTO {

    @ApiModelProperty("商户交易号")
    private String tradeNo;

    @ApiModelProperty("重百付交易号")
    private String gatewayTradeNo;

    @ApiModelProperty("付款码")
    private String barcode;

    @ApiModelProperty("帐户号")
    private String accountCode;

    @ApiModelProperty("帐户姓名")
    private String accountName;

    @ApiModelProperty("帐户余额")
    private String accountBalance;

    @ApiModelProperty("帐户信用额度")
    private String accountCreedit;

    @ApiModelProperty("交易金额")
    private String totalAmount;

    @ApiModelProperty("划帐金额")
    private String receiptAmount;

    @ApiModelProperty("实际支付金额")
    private String actualAmount;

    @ApiModelProperty("总优惠金额")
    private String totalDiscountAmount;

    @ApiModelProperty("渠道优惠金额")
    private String channelDiscountAmount;

    @ApiModelProperty("商户优惠金额")
    private String merchantDiscountAmount;

    @ApiModelProperty("支付明细")
    private String payDetail;

    @ApiModelProperty("消费金额")
    private BigDecimal amount;
}
