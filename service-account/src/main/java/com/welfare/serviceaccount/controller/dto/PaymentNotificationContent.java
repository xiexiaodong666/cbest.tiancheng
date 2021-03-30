package com.welfare.serviceaccount.controller.dto;

import com.welfare.service.dto.payment.BarcodePaymentRequest;
import com.welfare.service.dto.payment.PaymentRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
    private String gatewayTradeNo;
    @ApiModelProperty("买家id")
    private String buyerId;
    @ApiModelProperty("终端号（收银机）")
    private String terminal;
    @ApiModelProperty("总金额")
    private BigDecimal totalAmount;
    @ApiModelProperty("实际金额")
    private BigDecimal actualAmount;
    @ApiModelProperty("总优惠金额")
    private BigDecimal totalDiscountAmount;
    @ApiModelProperty("5011-支付宝-1|5012-支付宝优惠-1|5013-支付宝优惠2-1")
    private String payDetail;
    @ApiModelProperty("门店号")
    private String market;
    @ApiModelProperty("条码")
    private String barcode;
    private Date scanDate;

    public PaymentRequest toPaymentRequest(){
        BarcodePaymentRequest barcodePaymentRequest = new BarcodePaymentRequest();
        String paymentChannel = BarcodePaymentRequest.calculatePaymentChannelByBarcode(barcode);
        barcodePaymentRequest.setBarcode(barcode);
        barcodePaymentRequest.setPaymentChannel(paymentChannel);
        barcodePaymentRequest.setRequestId(UUID.randomUUID().toString());
        barcodePaymentRequest.setScanDate(scanDate);
        //重百付调用时候金额为分，甜橙处理逻辑是按照元处理
        barcodePaymentRequest.setAmount(totalAmount.divide(BigDecimal.valueOf(100L),2, RoundingMode.HALF_UP));
        barcodePaymentRequest.setMachineNo(terminal);
        barcodePaymentRequest.setStoreNo(market);
        barcodePaymentRequest.setOffline(false);
        barcodePaymentRequest.setTransNo(gatewayTradeNo);
        return barcodePaymentRequest;
    }
}
