package com.welfare.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author qiang.deng
 * @version 1.0.0
 * @date 2021/1/7 4:46 下午
 * @desc 账单列表响应dto
 */
@Data
public class WelfareSettleResp {

    @ApiModelProperty(value = "商户编码")
    private String merCode;

    @ApiModelProperty(value = "商户名称")
    private String merName;

    @ApiModelProperty(value = "待结算额度金额")
    private String unSettleAmount;

    @ApiModelProperty(value = "待结算员工充值金额")
    private String unSettleSelfAmount;

    @ApiModelProperty(value = "交易笔数")
    private String orderNum;

    @ApiModelProperty(value = "合作方式")
    private String merCooperationMode;

    @ApiModelProperty(value = "合作方式名称")
    private String merCooperationModeName;

    @ApiModelProperty(value = "返利金额")
    private String rebateAmount;
}
