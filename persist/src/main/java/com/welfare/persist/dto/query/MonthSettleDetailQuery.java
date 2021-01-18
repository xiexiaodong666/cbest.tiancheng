package com.welfare.persist.dto.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

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

    private Long minId;

    private String posOnlines;

    private String settleNo;

    private String transNo;
}
