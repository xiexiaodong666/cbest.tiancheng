package com.welfare.persist.dto.query;

import lombok.Data;

import java.util.Date;

/**
 * @author qiang.deng
 * @version 1.0.0
 * @date 2021/1/7 4:47 下午
 * @desc 账单列表请求dto
 */
@Data
public class MonthSettleQuery{
    private Long id;

    private String merCode;

    private String merName;

    private String startMonthStr;

    private String endMonthStr;

    private String merCooperationMode;

    private Date startTime;

    private Date endTime;

    private String settleStatus;

    private String recStatus;

    private String sendStatus;
}
