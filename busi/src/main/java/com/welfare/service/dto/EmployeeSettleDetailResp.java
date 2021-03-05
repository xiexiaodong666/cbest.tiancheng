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

    @ExcelProperty(value = "序号")
    @ApiModelProperty(value = "序号")
    private Long id;

    @ApiModelProperty(value = "交易流水号")
    @ExcelProperty(value = "交易流水号")
    private String transNo;

    @ExcelProperty(value = "订单号")
    @ApiModelProperty(value = "订单号")
    private String orderId;

    @ExcelProperty(value = "消费时间")
    @ApiModelProperty(value = "消费时间")
    private Date transTime;

    @ExcelProperty(value = "员工支出方式")
    @ApiModelProperty("员工支出方式 授信额度:surplus_quota, 溢缴款:")
    private String merAccountType;

    @ExcelProperty(value = "消费人")
    @ApiModelProperty("消费人")
    private String accountName;

    @ExcelProperty(value = "手机号")
    @ApiModelProperty("手机号")
    private String phone;

    @ExcelProperty(value = "组织机构")
    @ApiModelProperty("组织机构")
    private String departmentName;

    @ExcelProperty(value = "消费门店")
    @ApiModelProperty(value = "消费门店")
    private String storeName;

    @ExcelProperty(value = "消费类型")
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
