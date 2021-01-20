package com.welfare.service.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class BarcodePaymentNotifyReq implements Serializable {

    @ApiModelProperty("商户交易号")
    @JSONField(name = "trade_no")
    @JsonProperty("trade_no")
    private String tradeNo;

    @ApiModelProperty("重百付交易号")
    @JSONField(name = "gateway_trade_no")
    @JsonProperty("gateway_trade_no")
    private String gatewayTradeNo;

    @ApiModelProperty("付款码")
    private String barcode;

    @ApiModelProperty("帐户号")
    @JSONField(name = "account_code")
    @JsonProperty("account_code")
    private String accountCode;

    @ApiModelProperty("帐户姓名")
    @JSONField(name = "account_name")
    @JsonProperty("account_name")
    private String accountName;

    @ApiModelProperty("帐户余额")
    @JSONField(name = "account_balance")
    @JsonProperty("account_balance")
    private String accountBalance;

    @ApiModelProperty("帐户信用额度")
    @JSONField(name = "account_creedit")
    @JsonProperty("account_creedit")
    private String accountCreedit;

    @ApiModelProperty("交易金额")
    @JSONField(name = "total_amount")
    @JsonProperty("total_amount")
    private String totalAmount;

    @ApiModelProperty("划帐金额")
    @JSONField(name = "receipt_amount")
    @JsonProperty("receipt_amount")
    private String receiptAmount;

    @ApiModelProperty("实际支付金额")
    @JSONField(name = "actual_amount")
    @JsonProperty("actual_amount")
    private String actualAmount;

    @ApiModelProperty("总优惠金额")
    @JSONField(name = "total_discount_amount")
    @JsonProperty("total_discount_amount")
    private String totalDiscountAmount;

    @ApiModelProperty("渠道优惠金额")
    @JSONField(name = "channel_discount_amount")
    @JsonProperty("channel_discount_amount")
    private String channelDiscountAmount;

    @ApiModelProperty("商户优惠金额")
    @JSONField(name = "merchant_discount_amount")
    @JsonProperty("merchant_discount_amount")
    private String merchantDiscountAmount;

    @ApiModelProperty("支付明细")
    @JSONField(name = "pay_detail")
    @JsonProperty("pay_detail")
    private String payDetail;
}
