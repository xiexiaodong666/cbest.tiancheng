package com.welfare.persist.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/4/27 9:39 下午
 */
@Data
public class WholesalePayableSettleResp {

    @ApiModelProperty(value = "序列号")
    private String id;

    @ApiModelProperty(value = "账单编号")
    private String settleNo;

    @ApiModelProperty(value = "商户编码")
    private String merCode;

    @ApiModelProperty(value = "商户名称")
    private String merName;

    @ApiModelProperty(value = "账单实际金额")
    private String transAmount;

    @ApiModelProperty(value = "结算金额")
    private String settleAmount;

    @ApiModelProperty(value = "交易笔数")
    private String orderNum;

    @ApiModelProperty(value = "合作方式")
    private String merCooperationMode;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty(value = "合作方式名称")
    private String merCooperationModeName;

    @ApiModelProperty(value = "对账状态:待确认-unconfirmed 已确认-confirmed")
    private String recStatus;

    @ApiModelProperty(value = "结算状态:待结算-unsettled 已结算-settled")
    private String settleStatus;

    @ApiModelProperty(value = "发送状态:待发送-unsended 已发送-sended")
    private String sendStatus;

    @ApiModelProperty(value = "账单发送时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date sendTime;

    @ApiModelProperty(value = "账单确认时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date confirmTime;

    @ApiModelProperty(value = "账单返利金额")
    private String rebateAmount;

    @ApiModelProperty(value = "结算周期start")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(format = "yyyy-MM-dd")
    private Date settleStartTime;

    @ApiModelProperty(value = "结算周期end")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(format = "yyyy-MM-dd")
    private Date settleEndTime;

    private List<SettleStatisticsInfoDTO> settleStatisticsInfoList;
}
