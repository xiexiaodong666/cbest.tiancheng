package com.welfare.persist.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/4/29 11:46 上午
 */
@Data
public class PlatformPayableSettleGroupDTO {

    @ApiModelProperty("商户编码")
    private String merCode;
    @ApiModelProperty("商户名称")
    private String merName;
    @ApiModelProperty("合作方式(joint_venture:联营 distribution:经销)")
    private String cooperationMode;
    @ApiModelProperty("营收金额")
    private BigDecimal revenueAmount;
    @ApiModelProperty("销售总金额")
    private BigDecimal totalConsumeAmount;
    @ApiModelProperty("待结算金额")
    private BigDecimal unSettleAmount;
}
