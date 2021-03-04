package com.welfare.persist.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/2/23 7:06 下午
 */
@Data
public class WelfareTypeTotalAmountDTO {

  @ApiModelProperty("消费支出类型")
  private String type;

  @ApiModelProperty("消费支出类型名称")
  private String typeName;

  @ApiModelProperty("总金额")
  private BigDecimal amount;
}
