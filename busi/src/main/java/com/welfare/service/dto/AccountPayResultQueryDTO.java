package com.welfare.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AccountPayResultQueryDTO {

    @ApiModelProperty(value = "支付状态")
    private String paymentStatus;
}
