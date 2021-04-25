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
 * @date 2021/1/16 4:08 下午
 * @desc
 */
@Data
public class SettleMerTransDetailPageReq extends PageReq {

    @ApiModelProperty(value = "出入账类型 in-入账 out-出账")
    private String inOrOutType;

    @ApiModelProperty(value = "所属类型(目前余额:currentBalance 自主充值:self_deposit 信用额度:creditLimit 剩余信用额度:remainingLimit 返利余额:rebateLimit 批发采购信用额度:wholesaleCreditLimit 剩余批发采购信用额度:wholesaleCredit)")
    private String transType;

    @ApiModelProperty(value = "起始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
}
