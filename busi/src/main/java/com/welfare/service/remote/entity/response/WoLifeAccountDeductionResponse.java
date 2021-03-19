package com.welfare.service.remote.entity.response;

import java.math.BigDecimal;
import lombok.Data;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/3/11 3:46 PM
 */
@Data
public class WoLifeAccountDeductionResponse {

  /**
   * 扣除的金额, 成功时返回
   */
  private BigDecimal usedMoney;

  /**
   * 扣除的积分, 成功时返回
   */
  private BigDecimal usedIntegral;

  /**
   * 扣除的福利积点, 成功时返回
   */
  private BigDecimal usedWelfareIntegral;
}
