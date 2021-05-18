package com.welfare.persist.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/4/29 10:27 上午
 */
@Data
public class WholesalePayableBillGroupDTO {

    @ApiModelProperty("结算金额")
    private BigDecimal settleAmount;
    @ApiModelProperty("商品销售金额")
    private BigDecimal goodsSaleAmount;
    @ApiModelProperty("销售金额")
    private BigDecimal saleAmount;
    @ApiModelProperty("甜橙营收金额")
    private BigDecimal revenueAmount;
}
