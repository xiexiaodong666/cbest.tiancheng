package com.welfare.persist.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import lombok.Data;

@Data
@ApiModel("员工卡首页账号信息")
public class AccountSimpleDTO {

    @ApiModelProperty("单位")
    private String merName;

    @ApiModelProperty("账号")
    private Long accountCode;

    @ApiModelProperty("姓名")
    private String accountName;

    @ApiModelProperty("可用余额")
    private BigDecimal accountBalance;

    @ApiModelProperty("授信余额")
    private BigDecimal surplusQuota;
}
