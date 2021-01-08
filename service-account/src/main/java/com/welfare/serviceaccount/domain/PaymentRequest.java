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
    @ApiModelProperty("重百付支付流水号")
    private String transNo;
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
    @ApiModelProperty(value = "支付状态",notes = "1:新增, 2:处理中, 3:处理成功 -1:处理失败")
    private Integer paymentStatus;

    public String chargePaymentScene(){
        if (!StringUtil.startsWithNumber(storeNumber)) {
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
