package com.welfare.persist.dto.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AccountBillDetailSimpleReq {

    @ApiModelProperty("员工账号")
    private Long accountCode;

    @ApiModelProperty("开始时间")
    private String startTransTime;

    @ApiModelProperty("结束时间")
    private String endTransTime;
}
