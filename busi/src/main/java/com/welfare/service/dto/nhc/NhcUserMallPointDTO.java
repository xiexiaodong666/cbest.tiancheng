package com.welfare.service.dto.nhc;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/4/13 5:57 下午
 */
@Data
public class NhcUserMallPointDTO {

  @ApiModelProperty("用户编码")
  private String accountCode;

  @ApiModelProperty("用户积分")
  private BigDecimal mallPoint;
}
