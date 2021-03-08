package com.welfare.persist.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Description: 商户端结算单明细summaryDTO
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 3/5/2021
 */
@Data
@ApiModel("商户端结算明细按福利类型分组summaryDTO")
public class MonthSettleDetailMerchantSummaryDTO implements Serializable {

    @ApiModelProperty("福利类型")
    private String merAccountType;

    @ApiModelProperty("福利类型名称")
    private String merAccountTypeName;

    @ApiModelProperty("金额")
    private BigDecimal totalAmount;
}
