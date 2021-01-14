package com.welfare.service.dto;

import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class AccountDepositReq {

    @ApiModelProperty(value = "账号")
    private Long accountCode;

    @ApiModelProperty(value = "商户号")
    private String merCode;

    @ApiModelProperty("充值金额")
    private BigDecimal rechargeAmount;

    @ApiModelProperty("付款方式，微信传wechat")
    private String payType;
}
