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
public class MerchantPageReq {
    @ApiModelProperty("商户代码")
    private String merCode;

    @ApiModelProperty("商户名称")
    private String merName;

    @ApiModelProperty("商户类型")
    private String merType;

    @ApiModelProperty("合作方式")
    private String merCooperationMode;

    @ApiModelProperty("开始时间")
    private Date startTime;

    @ApiModelProperty("结束时间")
    private Date endTime;

    @ApiModelProperty("余额查询开始时间")
    private Date balanceStartTime;

    @ApiModelProperty("余额查询结束时间")
    private Date balanceEndTime;
}
