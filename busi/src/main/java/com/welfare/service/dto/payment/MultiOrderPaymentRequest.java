package com.welfare.service.dto.payment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 4/29/2021
 */
@ApiModel("多订单联合支付请求")
@Data
public class MultiOrderPaymentRequest {

    @ApiModelProperty(value = "支付请求id",required = true)
    private String requestId;
    @ApiModelProperty(value = "重百付支付流水号",required = true)
    private String transNo;
    @ApiModelProperty(value = "账户编码",required = true)
    private Long accountCode;
    @ApiModelProperty(value = "订单总金额",required = true)
    private BigDecimal amount = BigDecimal.ZERO;
    @ApiModelProperty(value = "多订单联合支付请求明细",required = true)
    private List<MultiOrderPaymentRequestDetail> multiOrderPaymentRequestDetails;
    @ApiModelProperty(value = "支付时间",required = true)
    private Date paymentDate;
    @ApiModelProperty(value = "业务类型  default:默认，NHC:卫计委积分支付,wholesale:批发支付",required = true)
    private String bizType;
    @ApiModelProperty(value = "支付场景: O2O, ONLINE_MALL, SHOP_CONSUMPTION",required = true)
    private String paymentScene;
    @ApiModelProperty(value = "支付渠道",required = true)
    private String paymentChannel;


    public List<OnlinePaymentRequest> toOnlinePaymentRequests(){
        return multiOrderPaymentRequestDetails.stream().map(detail -> {
            OnlinePaymentRequest onlinePaymentRequest = new OnlinePaymentRequest();
            onlinePaymentRequest.setPaymentChannel(this.paymentChannel);
            onlinePaymentRequest.setAccountCode(this.accountCode);
            onlinePaymentRequest.setRequestId(this.requestId);
            onlinePaymentRequest.setTransNo(this.transNo);
            onlinePaymentRequest.setAmount(detail.getAmount());
            onlinePaymentRequest.setBizType(this.bizType);
            onlinePaymentRequest.setMachineNo(detail.getMachineNo());
            onlinePaymentRequest.setStoreNo(detail.getStoreNo());
            onlinePaymentRequest.setPaymentDate(this.paymentDate);
            onlinePaymentRequest.setSaleRows(detail.getSaleRows());
            onlinePaymentRequest.setOrderNo(detail.getOrderNo());
            return onlinePaymentRequest;
        }).collect(Collectors.toList());
    }
}
