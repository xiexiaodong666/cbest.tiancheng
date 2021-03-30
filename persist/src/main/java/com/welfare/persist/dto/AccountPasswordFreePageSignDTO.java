package com.welfare.persist.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("免密签约")
public class AccountPasswordFreePageSignDTO {

    @ApiModelProperty(name = "签约页面")
    private String signPage;
}
