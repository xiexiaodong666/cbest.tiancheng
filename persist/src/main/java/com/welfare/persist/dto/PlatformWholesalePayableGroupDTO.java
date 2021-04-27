package com.welfare.persist.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/4/27 8:55 下午
 */
@Data
public class PlatformWholesalePayableGroupDTO {

    @ApiModelProperty("待结算金额")
    private BigDecimal settleAmount;
    @ApiModelProperty("消费金额")
    private BigDecimal transAmount;
    @ApiModelProperty("营收金额")
    private BigDecimal revenueAmount;
}
