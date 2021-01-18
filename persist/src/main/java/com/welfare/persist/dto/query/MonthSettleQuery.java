package com.welfare.persist.dto.query;

import lombok.Data;

/**
 * @author qiang.deng
 * @version 1.0.0
 * @date 2021/1/7 4:47 下午
 * @desc 账单列表请求dto
 */
@Data
public class MonthSettleQuery{

    private String merCode;

    private String merName;

    private String startMonthStr;

    private String endMonthStr;

    private String merCooperationMode;

    private String settleStatus;

    private String recStatus;

    private String sendStatus;
}
