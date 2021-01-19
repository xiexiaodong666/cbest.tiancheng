package com.welfare.service.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.welfare.common.base.RequestPage;
import com.welfare.service.utils.PageReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author qiang.deng
 * @version 1.0.0
 * @date 2021/1/7 4:47 下午
 * @desc 账单列表请求dto
 */
@Data
public class MonthSettlePageReq extends PageReq {

    @ApiModelProperty(value = "商户代码")
    private String merCode;

    @ApiModelProperty(value = "商户名称")
    private String merName;

    @ApiModelProperty(value = "起始月份 yyyy-MM")
    private String startMonthStr;

    @ApiModelProperty(value = "结束月份 yyyy-MM")
    private String endMonthStr;

    @ApiModelProperty(value = "起始时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(format = "yyyy-MM-dd")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(format = "yyyy-MM-dd")
    private Date endTime;

    @ApiModelProperty(value = "合作方式")
    private String merCooperationMode;

    @ApiModelProperty(value = "结算状态:待结算-unsettled 已结算-settled")
    private String settleStatus;

    @ApiModelProperty(value = "对账状态:待确认-unconfirmed 已确认-confirmed")
    private String recStatus;

    @ApiModelProperty(value = "发送状态:待发送-unsended 已发送-sended")
    private String sendStatus;

}
