package com.welfare.persist.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import lombok.Data;

@Data
@ApiModel("员工卡首页账号交易明细扣款详情")
public class AccountBillDetailSimpleDeductionDTO {

    @ApiModelProperty("账户类型名称")
    private String merAccountTypeName;

    @ApiModelProperty("子账户扣款金额")
    private BigDecimal accountDeductionAmount;
}
