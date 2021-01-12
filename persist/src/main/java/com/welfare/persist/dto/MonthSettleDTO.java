package com.welfare.persist.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author qiang.deng
 * @version 1.0.0
 * @date 2021/1/7 4:46 下午
 * @desc 账单列表响应dto
 */
@Data
public class MonthSettleDTO {
    private String id;

    private String settleNo;

    private String settleMonth;

    private String merCode;

    private String merName;

    private String transAmount;

    private String settleAmount;

    private String orderNum;

    private String merCooperationMode;

    private String merCooperationModeName;

    private String recStatus;

    private String settleStatus;

    private String sendStatus;

    private String sendTime;

    private String confirmTime;

    private String rebateAmount;

    private String rebate;
}
