package com.welfare.service.dto;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author qiang.deng
 * @version 1.0.0
 * @date 2021/1/7 4:48 下午
 * @desc 账单明细请求dto
 */
@Data
@ExcelIgnoreUnannotated
public class MonthSettleDetailResp{
    @ExcelProperty(value = "序列号")
    @ApiModelProperty(value = "序列号")
    private Long id;

    @ExcelProperty(value = "交易流水号")
    @ApiModelProperty(value = "交易流水号")
    private String transNo;

    @ExcelProperty(value = "订单号")
    @ApiModelProperty(value = "订单号")
    private String orderNO;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
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

    @ApiModelProperty(value = "福利类型")
    private String welfareTypeCode;

    @ExcelProperty(value = "福利类型名称")
    @ApiModelProperty(value = "福利类型名称")
    private String welfareTypeName;

    @ExcelProperty(value = "实际付款金额")
    @ApiModelProperty(value = "实际付款金额")
    private String payAmount;

    @ExcelProperty(value = "退款金额")
    @ApiModelProperty(value = "退款金额")
    private String refundAmount;

    @ExcelProperty(value = "实付金额")
    @ApiModelProperty(value = "实付金额")
    private String realAmount;

    @ExcelProperty(value = "结算金额")
    @ApiModelProperty(value = "结算金额")
    private String settleAmount;

    @ExcelProperty("手机号")
    @ApiModelProperty("手机号")
    private String phone;

    @ExcelProperty("组织机构")
    @ApiModelProperty("组织机构")
    private String departmentName;

    @ExcelProperty("消费人")
    @ApiModelProperty("消费人")
    private String accountName;

    @ExcelProperty("供应商")
    @ApiModelProperty("供应商")
    private String supplierName;

    @ApiModelProperty("消费类型")
    private String consumeType;

    @ExcelProperty("消费类型")
    @ApiModelProperty("消费类型")
    private String consumeTypeName;

    @ApiModelProperty("待结算金额")
    private String unsettledAmount;

    @ApiModelProperty("商户扣款方式")
    private String merDeductionType;

    @ExcelProperty("商户扣款方式")
    @ApiModelProperty("商户扣款方式")
    private String merDeductionTypeName;
}
