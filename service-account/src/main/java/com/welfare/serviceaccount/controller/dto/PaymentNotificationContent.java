package com.welfare.serviceaccount.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.welfare.service.dto.payment.BarcodePaymentRequest;
import com.welfare.service.dto.payment.PaymentRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 3/30/2021
 */
@Data
public class PaymentNotificationContent implements Serializable {
    @ApiModelProperty("交易单号")
    private String tradeNo;
    @ApiModelProperty("网关交易单号")
    @JsonProperty("gateway_trade_no")
    private String gatewayTradeNo;
    @ApiModelProperty("终端号（收银机）")
    private String terminal;
    @ApiModelProperty("总金额")
    private BigDecimal amount;
    @ApiModelProperty("5065-甜橙生活-5")
    @JsonProperty("pay_detail")
    private String payDetail;
    @ApiModelProperty("门店号")
    private String market;
    @ApiModelProperty("条码")
    private String barcode;
    private Date scanDate;

    @ApiModelProperty("账户编码")
    @JsonProperty("account_code")
    private String accountCode;

    public PaymentRequest toPaymentRequest(){
        BarcodePaymentRequest barcodePaymentRequest = new BarcodePaymentRequest();
        String paymentChannel = BarcodePaymentRequest.calculatePaymentChannelByBarcode(barcode);
        barcodePaymentRequest.setAccountCode(Long.valueOf(accountCode));
        barcodePaymentRequest.setBarcode(barcode);
        barcodePaymentRequest.setPaymentChannel(paymentChannel);
        barcodePaymentRequest.setRequestId(UUID.randomUUID().toString());
        barcodePaymentRequest.setScanDate(Calendar.getInstance().getTime());
        //重百付调用时候金额为分，甜橙处理逻辑是按照元处理
        barcodePaymentRequest.setAmount(amount.divide(BigDecimal.valueOf(100L),2, RoundingMode.HALF_UP));
        barcodePaymentRequest.setMachineNo(terminal);
        barcodePaymentRequest.setStoreNo(market);
        barcodePaymentRequest.setOffline(false);
        barcodePaymentRequest.setNotification(true);
        barcodePaymentRequest.setTransNo(gatewayTradeNo);
        return barcodePaymentRequest;
    }
}
