package com.welfare.servicesettlement.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author qiang.deng
 * @version 1.0.0
 * @date 2021/1/7 4:46 下午
 * @desc 账单列表响应dto
 */
@Data
public class BillRespDto {

    @ApiModelProperty(value = "账单编号")
    private String settleNo;

    @ApiModelProperty(value = "账单月")
    private String settleMonth;

    @ApiModelProperty(value = "商户编码")
    private String merCode;

    @ApiModelProperty(value = "商户名称")
    private String merName;

    @ApiModelProperty(value = "结算金额")
    private String amount;

    @ApiModelProperty(value = "交易笔数")
    private String orderNum;

    @ApiModelProperty(value = "合作方式")
    private String merCooperationMode;

    @ApiModelProperty(value = "对账状态")
    private String recStatus;

    @ApiModelProperty(value = "结算状态")
    private String settleStatus;

    @ApiModelProperty(value = "账单发送时间")
    private String sendTime;

    @ApiModelProperty(value = "账单确认时间")
    private String confirmTime;

    @ApiModelProperty(value = "账单返利金额")
    private String rebateAmount;

    @ApiModelProperty(value = "账单返利比例")
    private String rebate;

    @ApiModelProperty(value = "账单实际金额")
    private String realAmount;
}
