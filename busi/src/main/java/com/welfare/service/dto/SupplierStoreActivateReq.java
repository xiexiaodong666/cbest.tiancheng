package com.welfare.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SupplierStoreActivateReq {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "状态 1激活，0未激活")
    private Integer status;

}
