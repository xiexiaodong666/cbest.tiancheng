package com.welfare.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class SupplierStoreActivateReq {

    @ApiModelProperty(value = "id")
    @NotNull
    private Long id;

    @ApiModelProperty(value = "状态 1激活，0未激活")
    @NotNull
    private Integer status;

}
