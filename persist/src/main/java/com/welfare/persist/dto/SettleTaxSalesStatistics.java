package com.welfare.persist.dto;

import java.math.BigDecimal;
import lombok.Data;

/**
 * 税点商品结算金额统计
 * @author gaorui
 * @version 1.0.0
 * @date 2021/4/28 8:57 PM
 */
@Data
public class SettleTaxSalesStatistics {

  private BigDecimal tax;
  private BigDecimal amount;
}
