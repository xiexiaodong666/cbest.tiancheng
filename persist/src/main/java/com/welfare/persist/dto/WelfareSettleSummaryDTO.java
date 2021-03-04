package com.welfare.persist.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 2/24/2021
 */
@Data
@ApiModel("结算明细求和")
public class WelfareSettleSummaryDTO implements Serializable {
    @ApiModelProperty("商户名称")
    private String merName;
    @ApiModelProperty("合作方式")
    private String merCooperationMode;
    @ApiModelProperty("总消费金额")
    private BigDecimal totalConsumeAmount;
    @ApiModelProperty("线上消费金额")
    private BigDecimal onlineConsumeAmount;
    @ApiModelProperty("线下消费金额")
    private BigDecimal offlineConsumeAmount;
    @ApiModelProperty("余额消费金额")
    private BigDecimal balanceConsumeAmount;
    @ApiModelProperty("待结算金额")
    private BigDecimal unsettledAmount;

}
