package com.welfare.persist.dto;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

import java.io.Serializable;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 2/26/2021
 */
@Data
public class MonthSettleDetailSummaryDTO implements Serializable {
    @ApiModelProperty("线上消费金额")
    private String onlineConsumeAmount;
    @ApiModelProperty("线下消费金额")
    private String offlineConsumeAmount;
    @ApiModelProperty("余额消费金额")
    private String balanceConsumeAmount;
}
