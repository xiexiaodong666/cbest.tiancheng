package com.welfare.serviceaccount.controller.dto;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 3/30/2021
 */
@Data
@ApiModel("免密支付通知对象")
public class PaymentNotification implements Serializable {
    @ApiModelProperty("通知状态")
    private String status;
    @ApiModelProperty("通知信息")
    private String msg;
    @ApiModelProperty("业务状态")
    @JsonProperty("biz_status")
    private String bizStatus;
    @ApiModelProperty("业务信息")
    @JsonProperty("biz_msg")
    private String bizMsg;
    @ApiModelProperty("通知内容，PaymentNotificationContent的JSON字符串")
    @JsonProperty("biz_content")
    private String bizContent;

    /**
     * 将content转为PaymentNotificationContent对象
     * @return
     */
    public PaymentNotificationContent parseContent(){
        return JSON.parseObject(bizContent,PaymentNotificationContent.class);
    }
}
