package com.welfare.persist.dto.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Rongya.huang
 * @date 11:05 2021/3/4
 * @description 员工授信消费账单查询对象
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeSettleBillQuery {

    private String accountName;

    private String phone;

    private String departmentCode;

    private Date startTime;

    private Date endTime;

    private String settleStatus;

    private String merCode;
}
