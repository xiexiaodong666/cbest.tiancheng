package com.welfare.persist.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("员工卡首页账号概览信息")
public class AccountOverviewDTO {

    @ApiModelProperty("单位")
    private String merName;

    @ApiModelProperty("账号")
    private String phone;

    @ApiModelProperty("姓名")
    private String accountName;

    @ApiModelProperty("是否授信")
    private Boolean credit;

    @ApiModelProperty("账号支付渠道")
    private String paymentChannel;

    @ApiModelProperty("账号支付渠道名称")
    private String paymentChannelDesc;

    @ApiModelProperty("账号余额列表")
    private List<AccountBalanceDTO> balanceList;

    @ApiModelProperty("账号支付渠道")
    private List<AccountPaymentChannelDTO> paymentChannelList;

    @ApiModelProperty("查询失败错误信息")
    private String queryErrorMsg;
}
