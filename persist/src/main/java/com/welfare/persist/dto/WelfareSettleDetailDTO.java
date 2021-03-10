package com.welfare.persist.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author qiang.deng
 * @version 1.0.0
 * @date 2021/1/7 4:46 下午
 * @desc 账单列表响应dto
 */
@Data
public class WelfareSettleDetailDTO {
    @ExcelProperty(value = "序号")
    @ApiModelProperty(value = "序号")
    private Long id;

    @ExcelProperty(value = "交易流水号")
    @ApiModelProperty(value = "交易流水号")
    private String transNo;

    @ExcelProperty(value = "订单号")
    @ApiModelProperty(value = "订单号")
    private String orderNo;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelProperty(value = "交易时间")
    @ApiModelProperty(value = "交易时间")
    private Date transTime;

    @ExcelProperty(value = "消费门店编号")
    @ApiModelProperty(value = "消费门店编号")
    private String storeCode;

    @ExcelProperty(value = "消费门店名称")
    @ApiModelProperty(value = "消费门店名称")
    private String storeName;

    @ExcelProperty(value = "商户编号")
    @ApiModelProperty(value = "商户编号")
    private String merCode;

    @ExcelProperty(value = "商户名称")
    @ApiModelProperty(value = "商户名称")
    private String merName;

    @ExcelProperty(value = "门店类型")
    @ApiModelProperty(value = "门店类型")
    private String type;

    @ExcelProperty(value = "福利类型")
    @ApiModelProperty(value = "福利类型")
    private String welfareTypeCode;

    @ExcelProperty(value = "福利类型名称")
    @ApiModelProperty(value = "福利类型名称")
    private String welfareTypeName;

    @ExcelProperty(value = "实际付款金额")
    @ApiModelProperty(value = "实际付款金额")
    private String payAmount;

    @ExcelProperty(value = "结算金额")
    @ApiModelProperty(value = "结算金额")
    private String settleAmount;

    @ExcelProperty(value = "结算状态")
    @ApiModelProperty(value = "结算状态")
    private String settleFlag;

    @ApiModelProperty(value = "结算状态")
    private String settleFlagName;

    @ExcelProperty(value = "手机号")
    @ApiModelProperty(value = "手机号")
    private String phone;

    @ExcelProperty(value = "消费人")
    @ApiModelProperty(value = "消费人")
    private String accountName;

    @ExcelProperty(value = "部门名称")
    @ApiModelProperty(value = "部门名称")
    private String departmentName;

    @ExcelProperty(value = "消费类型")
    @ApiModelProperty("消费类型，线上消费、线下消费")
    private String consumeTypeName;
    @ApiModelProperty("消费类型，online offline")
    private String consumeType;
    @ExcelProperty(value = "供应商编码")
    @ApiModelProperty("供应商编码")
    private String supplierCode;
    @ExcelProperty(value = "供应商名称")
    @ApiModelProperty("供应商名称")
    private String supplierName;

    @ApiModelProperty("商户支出方式")
    private String merDeductionType;

    @ExcelProperty(value = "商户支出方式")
    @ApiModelProperty("商户支出方式")
    private String merDeductionTypeName;
}
