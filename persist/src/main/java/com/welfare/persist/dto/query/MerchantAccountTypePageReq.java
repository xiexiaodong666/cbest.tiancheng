package com.welfare.persist.dto.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Created by hao.yin on 2021/1/8.
 */
@Data
@NoArgsConstructor
public class MerchantAccountTypePageReq {
    @ApiModelProperty("商户代码")
    private String merCode;

    @ApiModelProperty("商户名称")
    private String merName;


    @ApiModelProperty("开始时间")
    private Date startTime;

    @ApiModelProperty("结束时间")
    private Date endTime;
}
