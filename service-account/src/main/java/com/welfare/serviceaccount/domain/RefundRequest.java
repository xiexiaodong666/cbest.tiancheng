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
    @ApiModelProperty("重百付流水号")
    private String transNo;
    @ApiModelProperty("重百付正向支付流水号")
    private String originalTransNo;
    @ApiModelProperty(value = "退款处理状态",notes = "1:新增, 2:处理中, 3:处理成功 -1:处理失败")
    private Integer refundStatus;
}
