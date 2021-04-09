package com.welfare.persist.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("免密签约(APP、小程序或JSAPI")
public class AccountPasswordFreeSignDTO {

    @ApiModelProperty("支付宝签约参数。小程序或JSAPI签约需要使用此参数")
    private String signParams;

    @ApiModelProperty("完整的签约URL，建议通过APP签约时使用此参数")
    private String signUrl;
}
