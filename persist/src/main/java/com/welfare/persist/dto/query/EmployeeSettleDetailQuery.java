package com.welfare.persist.dto.query;

import lombok.Data;

import java.util.Date;

/**
 * @Author: chengang
 * @Version: 0.0.1
 * @Date: 2021/3/3 5:10 下午
 */
@Data
public class EmployeeSettleDetailQuery {

    private String orderId;

    private String transNo;

    private String storeType;

    private Date transTimeStart;

    private Date transTimeEnd;

    private String merAccountType;

    private String storeCode;

    private String accountCode;

    private String settleNo;

    private String settleFlag;

    private Integer limit;

    private Long minId;
}
