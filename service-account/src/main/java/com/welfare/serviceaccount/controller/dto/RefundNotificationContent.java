package com.welfare.serviceaccount.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.welfare.service.dto.RefundRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

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
 * @date 4/2/2021
 */
@Data
public class RefundNotificationContent implements Serializable {
    @ApiModelProperty("交易单号")
    private String tradeNo;
    @ApiModelProperty("原交易单号")
    @JsonProperty("gateway_trade_no")
    private String gatewayTradeNo;
    @ApiModelProperty("终端号（收银机）")
    private String terminal;
    @ApiModelProperty("总金额")
    private BigDecimal amount;
    @ApiModelProperty("5065-甜橙生活-5")
    @JsonProperty("refund_detail")
    private String refundDetail;
    @ApiModelProperty("门店号")
    private String market;
    @ApiModelProperty("条码")
    private String barcode;
    private Date scanDate;

    @ApiModelProperty("账户编码")
    @JsonProperty("account_code")
    private String accountCode;
    @ApiModelProperty("退款单号")
    @JsonProperty("out_refund_no")
    private String outRefundNo;

    public RefundRequest toRefundRequest(){
        RefundRequest refundRequest = new RefundRequest();
        refundRequest.setAccountCode(Long.valueOf(accountCode));
        refundRequest.setRequestId(UUID.randomUUID().toString());
        refundRequest.setRefundDate(Calendar.getInstance().getTime());
        refundRequest.setOriginalTransNo(gatewayTradeNo);
        //冲正场景，交易单号为空，特殊处理下
        refundRequest.setTransNo(StringUtils.isEmpty(outRefundNo) ? gatewayTradeNo + "R1" : outRefundNo);
        refundRequest.setAmount(amount.divide(BigDecimal.valueOf(100),2, RoundingMode.HALF_UP));
        return refundRequest;
    }
}
