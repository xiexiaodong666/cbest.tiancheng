package com.welfare.serviceaccount.domain;

import com.alibaba.druid.util.StringUtils;
import com.welfare.common.constants.WelfaleConstant;
import com.welfare.common.util.StringUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 1/6/2021
 */
@ApiModel("支付请求")
@Data
public class PaymentRequest {
    @ApiModelProperty("支付请求id")
    private String requestId;
    @ApiModelProperty("重百付支付流水id")
    private String paymentId;
    @ApiModelProperty("门店号")
    private String storeNumber;
    @ApiModelProperty("支付机器号")
    private String machineNumber;
    @ApiModelProperty("账户编码")
    private String accountCode;
    @ApiModelProperty("支付场景")
    private String paymentScene;
    @ApiModelProperty("金额")
    private BigDecimal amount = BigDecimal.ZERO;

    public String chargePaymentScene(){
        if (!StringUtil.isStartWithNumber(storeNumber)) {
            //非数字开头的门店，供应商线下消费
            this.paymentScene = WelfaleConstant.PaymentScene.OFFLINE_SUPPLIER.code();
        } else if(StringUtils.equals(machineNumber,"")){
            //特殊支付机器号，重百线上
            this.paymentScene = WelfaleConstant.PaymentScene.ONLINE_STORE.code();
        } else {
            //其余情况，重百线下
            this.paymentScene = WelfaleConstant.PaymentScene.OFFLINE_CBEST.code();
        }
        return this.paymentScene;
    }
}
