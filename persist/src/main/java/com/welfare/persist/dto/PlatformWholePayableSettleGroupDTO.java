package com.welfare.persist.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/4/27 3:50 下午
 */
@Data
public class PlatformWholePayableSettleGroupDTO {


    @ApiModelProperty("销售金额 (元)")
    private BigDecimal saleTotalAmount;

    @ApiModelProperty("待结算金额 (元)")
    private BigDecimal unsettledTotalAmount;

    @ApiModelProperty("甜橙营收金额 (元)")
    private BigDecimal revenueTotalAmount;
}
