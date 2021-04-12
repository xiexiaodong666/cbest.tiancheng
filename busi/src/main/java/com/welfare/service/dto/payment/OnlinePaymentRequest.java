package com.welfare.service.dto.payment;

import com.welfare.common.constants.WelfareConstant;
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
    @ApiModelProperty("业务类型  default:默认，hospital-points:卫计委积分支付,wholesale:批发支付")
    private String bizType;
    @Override
    public Long calculateAccountCode(){
        return super.getAccountCode();
    }

    @Override
    public WelfareConstant.PaymentBizType bizType(){
        return WelfareConstant.PaymentBizType.fromCode(bizType);
    }
}
