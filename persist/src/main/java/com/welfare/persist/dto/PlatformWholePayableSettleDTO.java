package com.welfare.persist.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/4/27 3:36 下午
 */
@Data
public class PlatformWholePayableSettleDTO {

    @ApiModelProperty("商户编码")
    private String merCode;

    @ApiModelProperty("商户名称")
    private String merName;

    @ApiModelProperty("合作方式(joint_venture：联营  distribution：经销 )")
    private String wholesaleCooperationMode;

    @ApiModelProperty("销售金额 (元)")
    private BigDecimal saleAmount;

    @ApiModelProperty("待结算金额 (元)")
    private BigDecimal unsettledAmount;

    @ApiModelProperty("甜橙营收金额 (元)")
    private BigDecimal revenueAmount;
}
