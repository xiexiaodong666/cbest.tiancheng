package com.welfare.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @author chengang
 * @version 1.0.0
 * @date 2021/3/7 4:46 下午
 * @desc 员工授信消费列表响应
 */
@Data
public class EmployeeSettleConsumeResp {

    @ApiModelProperty("员工编号")
    private Long accountCode;

    @ApiModelProperty("员工姓名")
    private String accountName;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("组织code")
    private String departmentCode;

    @ApiModelProperty("组织机构")
    private String departmentName;

    @ApiModelProperty("授信额度")
    private String quota;

    @ApiModelProperty("自营消费金额")
    private String selfConsumerAmount;

    @ApiModelProperty("第三方消费金额")
    private String thirdConsumerAmount;

    @ApiModelProperty("消费总额")
    private String totalConsumerAmount;

    @ApiModelProperty("消费笔数")
    private String orderNum;


}

