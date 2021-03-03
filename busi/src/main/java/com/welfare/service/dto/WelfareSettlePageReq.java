package com.welfare.service.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
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
public class WelfareSettlePageReq extends PageReq {

    @ApiModelProperty(value = "商户代码")
    private String merCode;

    @ApiModelProperty(value = "商户名称")
    private String merName;

    @ApiModelProperty(value = "合作方式")
    private String merCooperationMode;

    @ApiModelProperty(value = "起始交易时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @ApiModelProperty(value = "结束交易时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

}
