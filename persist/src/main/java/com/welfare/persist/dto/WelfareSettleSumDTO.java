package com.welfare.persist.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 2/23/2021
 */
@Data
public class WelfareSettleSumDTO {
    @ApiModelProperty(value = "待结算额度金额")
    private String unSettleAmount;

    @ApiModelProperty(value = "待结算员工充值金额")
    private String unSettleSelfAmount;

    @ApiModelProperty(value = "返利金额")
    private String rebateAmount;

    @ApiModelProperty(value = "消费总金额")
    private String totalConsumeAmount;

    @ApiModelProperty(value = "余额消费金额")
    private String balanceConsumeAmount;
}
