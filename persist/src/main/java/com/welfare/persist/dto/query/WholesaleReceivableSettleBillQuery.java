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
 * @Date: 2021/4/27 8:18 下午
 */
@Data
public class WholesaleReceivableSettleBillQuery extends PageReq{

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
    private Date startDay;

    @ApiModelProperty(value = "结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(format = "yyyy-MM-dd")
    private Date endDay;

    @ApiModelProperty(value = "合作方式")
    private String merCooperationMode;
    @ApiModelProperty(value = "合作方式")
    private String merCooperationModeName;

    @ApiModelProperty(value = "结算状态:待结算-unsettled 已结算-settled")
    private String settleStatus;

    @ApiModelProperty(value = "对账状态:待确认-unconfirmed 已确认-confirmed")
    private String recStatus;

    @ApiModelProperty(value = "发送状态:待发送-unsended 已发送-sended")
    private String sendStatus;
}
