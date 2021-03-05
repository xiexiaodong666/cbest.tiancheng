package com.welfare.service.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Author: chenggang
 * @Version: 0.0.1
 * @Date: 2021/3/3 5:10 下午
 */@Data
public class EmployeeSettleDetailReq {

    @ApiModelProperty("订单编号")
    private String orderId;

    @ApiModelProperty("消费流水号")
    private String transNo;

    @ApiModelProperty("消费类型 自营:self, 第三方:third")
    private String storeType;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("消费起始时间")
    private Date transTimeStart;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("消费截止时间")
    private Date transTimeEnd;

    @ApiModelProperty("员工支出方式 授信额度:surplus_quota, 溢缴款:")
    private String merAccountType;

    @ApiModelProperty(value = "门店编号")
    private String storeCode;
}
