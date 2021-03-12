package com.welfare.persist.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("员工卡首页账户余额信息")
public class AccountBalanceDTO {

    @ApiModelProperty("余额编码")
    private String code;

    @ApiModelProperty("余额名称")
    private String name;

    @ApiModelProperty("余额值")
    private Object value;
}
