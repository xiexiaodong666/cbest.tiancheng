package com.welfare.persist.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/4/28 12:40 上午
 */
@Data
public class PlatformWholesalePayableDetailSummaryDTO {

    @ApiModelProperty("商户名称")
    private String merCode;

    @ApiModelProperty("合作方式")
    private String cooperationMode;

    @ApiModelProperty("销售金额（元）")
    private BigDecimal saleAmount;

    @ApiModelProperty("待结算金额（元）")
    private BigDecimal unsettledAmount;

    @ApiModelProperty("甜橙营收金额（元）")
    private BigDecimal revenueAmount;
}
