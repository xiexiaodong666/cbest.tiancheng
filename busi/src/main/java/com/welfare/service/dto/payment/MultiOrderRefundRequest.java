package com.welfare.service.dto.payment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 4/30/2021
 */
@ApiModel("多订单联合退款")
@Data
public class MultiOrderRefundRequest {
    @ApiModelProperty("请求id")
    private String requestId;
    @ApiModelProperty("重百付流水号")
    private String transNo;
    @ApiModelProperty("重百付正向支付流水号")
    private String originalTransNo;
    @ApiModelProperty("退款总金额")
    private BigDecimal amount;

    private List<MultiOrderRefundRequestDetail> multiOrderRefundRequestDetails;
}
