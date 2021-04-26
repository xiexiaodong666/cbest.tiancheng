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
    private BigDecimal transAmount;
}
