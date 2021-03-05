package com.welfare.persist.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author Rongya.huang
 * @date 14:01 2021/3/5
 * @description 结算单详情列表数据库返回
 **/
@Data
public class EmployeeSettleDetailDTO {

    private String transNo;

    private String orderId;

    private Date transTime;

    private String merAccountType;

    private String accountName;

    private String phone;

    private String departmentCode;

    private String departmentName;

    private String storeCode;

    private String storeName;

    private String storeType;

    private String transAmount;

    private String settleAmount;

    private String settleFlag;
}
