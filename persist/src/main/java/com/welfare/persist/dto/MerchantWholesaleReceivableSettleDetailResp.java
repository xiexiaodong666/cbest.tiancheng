package com.welfare.persist.dto;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/4/27 8:26 下午
 */
@Data
@ExcelIgnoreUnannotated
public class MerchantWholesaleReceivableSettleDetailResp {

    @ExcelProperty(value = "序列号")
    @ApiModelProperty(value = "序列号")
    private Long id;

    @ExcelProperty(value = "交易流水号")
    @ApiModelProperty(value = "交易流水号")
    private String transNo;

    @ExcelProperty(value = "订单号")
    @ApiModelProperty(value = "订单号")
    private String orderNo;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @ExcelProperty(value = "交易时间")
    @ApiModelProperty(value = "交易时间")
    private Date transTime;

    @ExcelProperty(value = "消费门店编号")
    @ApiModelProperty(value = "消费门店编号")
    private String storeCode;

    @ExcelProperty(value = "消费门店")
    @ApiModelProperty(value = "消费门店")
    private String storeName;

    @ExcelProperty("手机号")
    @ApiModelProperty("手机号")
    private String phone;

    @ExcelProperty("消费人")
    @ApiModelProperty("消费人")
    private String accountName;

    @ExcelProperty(value = "客户商户")
    @ApiModelProperty(value = "客户商户")
    private String customerMerName;

    @ExcelProperty(value = "客户商户编号")
    private String customerMerCode;

    @ApiModelProperty("销售金额")
    @ExcelProperty("销售金额")
    private BigDecimal saleAmount;

    @ApiModelProperty("结算金额")
    @ExcelProperty("结算金额")
    private BigDecimal settleAmount;
}
