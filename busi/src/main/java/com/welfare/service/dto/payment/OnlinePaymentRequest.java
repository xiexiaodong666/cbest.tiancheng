package com.welfare.service.dto.payment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 1/6/2021
 */
@ApiModel("线上支付请求")
@Data
public class OnlinePaymentRequest extends PaymentRequest {

    @ApiModelProperty("支付渠道")
    private String paymentChannel;

    @Override
    public Long calculateAccountCode(){
        return super.getAccountCode();
    }
}
