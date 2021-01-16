package com.welfare.persist.dto.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author qiang.deng
 * @version 1.0.0
 * @date 2021/1/7 4:47 下午
 * @desc 账单列表请求dto
 */
@Data
public class WelfareSettleQuery {

    @ApiModelProperty(value = "商户代码")
    private String merCode;

    @ApiModelProperty(value = "商户名称")
    private String merName;

    @ApiModelProperty(value = "合作方式")
    private String merCooperationMode;
}
