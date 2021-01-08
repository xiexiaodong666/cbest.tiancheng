package com.welfare.serviceaccount.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 1/8/2021
 */
@Data
@ApiModel("条码加盐参数")
public class BarcodeSalt {
    @ApiModelProperty("有效期间")
    private String validPeriod;
    @ApiModelProperty("加盐参数")
    private Long saltValue;
}
