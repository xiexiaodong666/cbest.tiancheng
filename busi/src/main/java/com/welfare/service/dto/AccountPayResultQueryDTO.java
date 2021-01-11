package com.welfare.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AccountPayResultQueryDTO {

    @ApiModelProperty(value = "是否已支付")
    private Boolean paid;
}
