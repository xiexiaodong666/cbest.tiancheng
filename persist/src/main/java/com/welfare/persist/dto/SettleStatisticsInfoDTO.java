package com.welfare.persist.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author qiang.deng
 * @version 1.0.0
 * @date 2021/1/18 3:31 下午
 * @desc
 */
@Data
public class SettleStatisticsInfoDTO {
    @ApiModelProperty(value = "费用类型编码")
    private String settleAccountTypeCode;
    @ApiModelProperty(value = "费用类型名称")
    private String settleAccountTypeName;
    @ApiModelProperty(value = "费用类型金额")
    private BigDecimal amount;
}
