package com.welfare.persist.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author duanhy
 * @version 1.0.0
 * @description
 * @date 2021/1/14  1:15 PM
 */
@Data
public class AccountApplyTotalDTO {

  @ApiModelProperty("充值账号数量")
  private Integer userCount;

  @ApiModelProperty("充值总金额")
  private BigDecimal totalAmount;
}