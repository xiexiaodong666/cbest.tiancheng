package com.welfare.service.dto.payment;

import io.swagger.annotations.ApiModel;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 3/1/2021
 */
@ApiModel("批发支付请求")
public class WholesalePaymentRequest extends PaymentRequest{
    @Override
    public Long calculateAccountCode(){
        return super.getAccountCode();
    }
}
