package com.welfare.persist.dto.query;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/4/27 9:35 下午
 */
@Data
public class WholesalePayableSettleBillQuery extends PageReq{

    @ApiModelProperty(value = "商户代码")
    private String merCode;

    @ApiModelProperty(value = "起始时间 yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date startDay;

    @ApiModelProperty(value = "结束时间 yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date endDay;

    @ApiModelProperty(value = "合作方式 联营:joint_venture  经销:distribution")
    private String merCooperationMode;

    @ApiModelProperty(value = "结算状态:待结算-unsettled 已结算-settled")
    private String settleStatus;

    @ApiModelProperty(value = "对账状态:待确认-unconfirmed 已确认-confirmed")
    private String recStatus;

    @ApiModelProperty(value = "商户名称")
    private String merName;

    private Long id;
}
