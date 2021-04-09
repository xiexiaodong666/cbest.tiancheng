package com.welfare.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CreateThirdPartyPaymentReq {

    @ApiModelProperty("条码")
    private String barcode;

    @ApiModelProperty("员工账号")
    private Long accountCode;
}
