package com.welfare.persist.dto;

import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import lombok.Data;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/3/18 11:47 PM
 */
@Data
public class AccountPageExtDTO {

  /**
   * 总员工数
   */
  @ApiModelProperty("总员工数")
  private Integer totalAccount;

  /**
   * 总账户余额
   */
  @ApiModelProperty("总账户余额")
  private BigDecimal totalAccountBalance;

  /**
   * 总剩余授信额度
   */
  @ApiModelProperty("总剩余授信额度")
  private BigDecimal totalSurplusQuota;

}
