package com.welfare.service.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Author: chengang
 * @Version: 0.0.1
 * @Date: 2021/3/3 5:10 下午
 */
@Data
public class EmployeeSettleDetailResp {

    @ApiModelProperty(value = "交易流水号")
    @ExcelProperty(value = "交易流水号")
    private String transNo;

    @ApiModelProperty(value = "订单号")
    private String orderId;

    @ApiModelProperty(value = "消费时间")
    private Date transTime;

    @ApiModelProperty("员工支出方式 授信额度:surplus_quota, 溢缴款:")
    private String merAccountType;

    @ApiModelProperty("员工姓名")
    private String accountName;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("组织code")
    private String departmentCode;

    @ApiModelProperty("组织机构")
    private String departmentName;

    @ApiModelProperty(value = "门店号" )
    private String storeCode;

    @ApiModelProperty(value = "门店名称")
    private String storeName;

    @ApiModelProperty("消费类型 自营:self, 第三方:third")
    private String storeType;

    @ExcelProperty(value = "消费金额")
    @ApiModelProperty(value = "消费金额")
    private String transAmount;

    @ExcelProperty(value = "结算金额")
    @ApiModelProperty(value = "结算金额")
    private String settleAmount;

    @ExcelProperty(value = "结算状态")
    @ApiModelProperty(value = "结算状态")
    private String settleFlag;
}
