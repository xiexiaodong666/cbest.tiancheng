package com.welfare.service.dto.payment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 1/22/2021
 */
@ApiModel("门禁支付请求(用于交投)")
public class DoorAccessPaymentRequest extends PaymentRequest{
    @ApiModelProperty("门禁信息")
    private String doorAccessInfo;

    @Override
    public Long calculateAccountCode(){
        //todo 根据门禁信息获取账户编号
        return super.getAccountCode();
    }
}
