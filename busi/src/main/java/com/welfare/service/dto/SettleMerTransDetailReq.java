package com.welfare.service.dto;

import com.welfare.service.utils.PageReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author qiang.deng
 * @version 1.0.0
 * @date 2021/1/16 4:08 下午
 * @desc
 */
@Data
public class SettleMerTransDetailReq extends PageReq {

    @ApiModelProperty(value = "出入账类型 in-入账 out-出账")
    private String inOrOutType;

    @ApiModelProperty(value = "所属类型")
    private String transType;

    @ApiModelProperty(value = "起始时间")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;
}
