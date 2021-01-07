package com.welfare.serviceaccount.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 1/7/2021
 */
@Data
public class RefundRequest {
    @ApiModelProperty("请求id")
    private String requestId;
    @ApiModelProperty("重百付流水id")
    private String paymentId;
    @ApiModelProperty("重百付正向支付流水id")
    private String originalPaymentId;
}
