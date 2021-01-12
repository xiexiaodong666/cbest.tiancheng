package com.welfare.persist.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author qiang.deng
 * @version 1.0.0
 * @date 2021/1/7 4:48 下午
 * @desc 账单明细请求dto
 */
@Data
public class MonthSettleDetailDTO{
    private String transNo;

    private String orderNO;

    private String transTime;

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
}
