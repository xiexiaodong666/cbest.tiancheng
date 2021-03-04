package com.welfare.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/2/23 4:08 下午
 */
@Data
public class WelfareTypeTotalAmountResp {

  @ApiModelProperty("消费支出类型编码")
  private String type;

  @ApiModelProperty("消费支出类型名称")
  private String typeName;

  @ApiModelProperty("总金额")
  private BigDecimal amount;
}
