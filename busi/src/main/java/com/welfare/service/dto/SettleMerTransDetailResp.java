package com.welfare.service.dto;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author qiang.deng
 * @version 1.0.0
 * @date 2021/1/16 4:08 下午
 * @desc
 */
public class SettleMerTransDetailResp {

    @ApiModelProperty(value = "交易流水号")
    private String transNo;

    @ApiModelProperty(value = "交易日期")
    private Date transTime;

    @ApiModelProperty(value = "出入账类型 in-入账 out-出账")
    private String inOrOutType;

    @ApiModelProperty(value = "所属类型")
    private String transType;

    @ApiModelProperty(value = "出入账金额")
    private BigDecimal inOrOutAmount;

    @ApiModelProperty(value = "总金额")
    private BigDecimal allAmount;

}
