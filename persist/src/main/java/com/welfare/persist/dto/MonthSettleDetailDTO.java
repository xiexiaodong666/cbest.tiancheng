package com.welfare.persist.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author qiang.deng
 * @version 1.0.0
 * @date 2021/1/7 4:48 下午
 * @desc 账单明细请求dto
 */
@Data
public class MonthSettleDetailDTO{
    private Long id;

    private String transNo;

    private String orderNo;

    private Date transTime;

    private String storeCode;

    private String storeName;

    private String merCode;

    private String merName;

    private String type;

    private String welfareTypeCode;

    private String welfareTypeName;

    private String payAmount;

    private String refundAmount;

    private String realAmount;


    private String settleAmount;



    private String phone;

    private String departmentName;

    private String accountName;

    private String supplierName;

    private String consumeType;

    private String settleFlag;

    private String orderChannel;

    private String unsettledAmount;

    private String merDeductionType;

    private String merDeductionTypeName;

    private String consumeTypeName;
}
