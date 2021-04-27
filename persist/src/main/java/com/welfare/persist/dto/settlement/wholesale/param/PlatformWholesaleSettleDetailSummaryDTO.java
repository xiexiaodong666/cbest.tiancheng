package com.welfare.persist.dto.settlement.wholesale.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 4/27/2021
 */
@Data
public class PlatformWholesaleSettleDetailSummaryDTO {
    @ApiModelProperty("商户编码")
    private String merCode;
    @ApiModelProperty("商户名称")
    private String merName;
    @ApiModelProperty("商户交易金额")
    private BigDecimal transAmount;
    @ApiModelProperty("商户结算金额")
    private BigDecimal settleAmount;
}
