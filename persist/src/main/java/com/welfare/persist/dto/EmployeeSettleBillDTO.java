package com.welfare.persist.dto;

import lombok.Data;

import java.util.Date;


/**
 * @author Rongya.huang
 * @date 11:03 2021/3/4
 * @description 员工授信消费账单列表响应DTO
 **/
@Data
public class EmployeeSettleBillDTO {

    private String settleId;

    private String settleNo;

    private Date createTime;

    private String accountName;

    private String phone;

    private String departmentCode;

    private String departmentName;

    private String selfAmount;

    private String thirdAmount;

    private String transAmount;

    private String orderNum;

    private String settleAmount;

    private String settleStatus;

    private String quota;

    private Date settleTime;

    private String settlePeriod;


}

