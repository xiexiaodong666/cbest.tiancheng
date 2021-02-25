package com.welfare.persist.dto.query;

import lombok.Data;

import java.util.Date;

/**
 * @author qiang.deng
 * @version 1.0.0
 * @date 2021/1/7 4:48 下午
 * @desc 账单明细响应dto
 */
@Data
public class MonthSettleDetailQuery{
    private String orderNo;

    private String storeName;

    private String storeCode;

    private Date startTime;

    private Date endTime;

    private String welfareTypeCode;

    private String merCode;

    private String storeType;


    private String posOnlines;

    private String settleNo;

    private String transNo;

    private Long minId;
    /**
     * 查询数据量限制
     */
    private Integer limit;

    private String phone;

    private String departmentCode;

    /**
     * 消费类型, 线上 线下
     */
    private String consumeType;

    /**
     * 结算状态
     */
    private String settleFlag;
}
