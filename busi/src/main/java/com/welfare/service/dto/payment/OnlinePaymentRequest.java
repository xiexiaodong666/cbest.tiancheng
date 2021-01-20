package com.welfare.service.dto.payment;

import io.swagger.annotations.ApiModel;
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


    @Override
    public Long calculateAccountCode(){
        return super.getAccountCode();
    }
}
