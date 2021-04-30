package com.welfare.service.dto;

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
 * @Date: 2021/4/28 1:14 上午
 */
@Data
public class WholesalePaySettleDetailReq {
    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    @ApiModelProperty(value = "门店编号")
    private List<String> storeCodes;

    @ApiModelProperty(value = "交易时间 yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @ApiModelProperty(value = "交易时间 yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @ApiModelProperty(value = "消费流水号")
    private String transNo;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("客户商户编码")
    private String customerMerCode;
}
