package com.welfare.persist.dto.settlement.wholesale;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 4/26/2021
 */
@Data
public class PlatformWholesaleSettleGroupDTO {
    private String merCode;
    private String merName;
    private BigDecimal settleAmount;
    /**
     * 消费总金额
     */
    private BigDecimal totalConsumeAmount;

    /**
     * 待结算金额
     */
    private BigDecimal unSettleAmount;
}
