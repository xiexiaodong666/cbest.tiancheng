package com.welfare.service.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


/**
 * @author Rongya.huang
 * @date 11:03 2021/3/4
 * @description 员工授信消费账单列表响应
 **/
@Data
public class EmployeeSettleBillResp {

    @ApiModelProperty(value = "账单id")
    private String settleId;

    @ApiModelProperty(value = "账单编号")
    private String settleNo;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty(value = "员工姓名")
    private String accountName;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "组织机构")
    private String departmentName;

    @ApiModelProperty(value = "自营消费金额")
    private String selfAmount;

    @ApiModelProperty(value = "第三方消费金额")
    private String thirdAmount;

    @ApiModelProperty(value = "消费总额")
    private String transAmount;

    @ApiModelProperty(value = "消费笔数")
    private String orderNum;

    @ApiModelProperty(value = "账单结算金额")
    private String settleAmount;

    @ApiModelProperty(value = "账单结算周期")
    private String settlePeriod;

    @ApiModelProperty(value = "员工授信额度")
    private String quota;

    @ApiModelProperty(value = "结算状态:待结算-unsettled 已结算-settled")
    private String settleStatus;

    @ApiModelProperty(value = "结算时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(format = "yyyy-MM-dd")
    private Date settleTime;


}

