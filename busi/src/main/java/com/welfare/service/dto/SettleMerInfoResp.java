package com.welfare.service.dto;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

/**
 * @author qiang.deng
 * @version 1.0.0
 * @date 2021/1/16 4:08 下午
 * @desc
 */
public class SettleMerInfoResp {

    @ApiModelProperty(value = "商户编码")
    private String merCode;

    @ApiModelProperty(value = "充值额度")
    private String rechargeLimit;

    @ApiModelProperty(value = "消费余额")
    private String transAmount;

    @ApiModelProperty(value = "信用额度")
    private BigDecimal creditLimit;

    @ApiModelProperty(value = "剩余信用额度")
    private BigDecimal remainingLimit;

    @ApiModelProperty(value = "返利余额")
    private BigDecimal rebateLimit;
}
