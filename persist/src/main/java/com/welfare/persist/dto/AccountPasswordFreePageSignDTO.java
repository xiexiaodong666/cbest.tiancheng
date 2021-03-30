package com.welfare.persist.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("免密支付签约(页面跳转方式）")
public class AccountPasswordFreePageSignDTO {

    @ApiModelProperty("签约页面")
    private String signPage;
}
