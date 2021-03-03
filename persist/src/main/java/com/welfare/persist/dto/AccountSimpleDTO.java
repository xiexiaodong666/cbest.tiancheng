package com.welfare.persist.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("员工卡首页账号信息")
public class AccountSimpleDTO {

    @ApiModelProperty("单位")
    private String merName;

    @ApiModelProperty("账号")
    private String phone;

    @ApiModelProperty("姓名")
    private String accountName;

    @ApiModelProperty("可用余额")
    private BigDecimal accountBalance;

    @ApiModelProperty("最大授权额度")
    private BigDecimal maxQuota;

    @ApiModelProperty("授信余额")
    private BigDecimal surplusQuota;

    @ApiModelProperty("是否授信")
    private Boolean credit;
}
