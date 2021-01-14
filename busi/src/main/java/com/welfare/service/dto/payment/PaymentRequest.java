package com.welfare.service.dto.payment;

import com.welfare.common.constants.WelfareConstant;
import com.welfare.common.util.StringUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 1/11/2021
 */
@Data
public abstract class PaymentRequest {
    private static transient final String ONLINE_MACHINE_NO = "9002";


    @ApiModelProperty("支付请求id")
    private String requestId;
    @ApiModelProperty("重百付支付流水号")
    private String transNo;
    @ApiModelProperty("门店号")
    private String storeNo;
    @ApiModelProperty("支付机器号")
    private String machineNo;
    @ApiModelProperty("金额")
    private BigDecimal amount = BigDecimal.ZERO;
    @ApiModelProperty("是否离线，用于区分是离线支付还是在线支付")
    private Boolean offline;
    @ApiModelProperty("商户编码")
    private String merCode;
    @ApiModelProperty("支付状态，0：新增，1：处理中，2：处理成功，-1：处理失败")
    private Integer paymentStatus;
    @ApiModelProperty("账户号")
    private Long accountCode;
    @ApiModelProperty("支付时间")
    private Date paymentDate;

    public String chargePaymentScene(){
        if (!StringUtil.startsWithNumber(storeNo)) {
            //非数字开头的门店，供应商线下消费
            return WelfareConstant.PaymentScene.OFFLINE_SUPPLIER.code();
        } else if(ONLINE_MACHINE_NO.equals(machineNo)){
            //特殊支付机器号，重百线上
            return WelfareConstant.PaymentScene.ONLINE_STORE.code();
        } else {
            //其余情况，重百线下
            return WelfareConstant.PaymentScene.OFFLINE_CBEST.code();
        }
    }



    public Long calculateAccountCode(){
        return getAccountCode();
    }
}
