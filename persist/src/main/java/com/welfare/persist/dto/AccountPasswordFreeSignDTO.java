package com.welfare.persist.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("免密签约")
public class AccountPasswordFreeSignDTO {

    @ApiModelProperty(name = "完整的签约URL")
    private String signUrl;
}
