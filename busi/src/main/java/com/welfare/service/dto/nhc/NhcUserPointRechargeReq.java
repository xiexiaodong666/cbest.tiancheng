package com.welfare.service.dto.nhc;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/4/12 11:40 上午
 */
@Data
public class NhcUserPointRechargeReq {

  @ApiModelProperty(value = "请求id，用幂等处理", required = true)
  @NotEmpty(message = "请求id不能为空")
  private String requestId;

  @ApiModelProperty(value = "积分", required = true)
  @DecimalMin(value = "0", message = "积分必须大于零")
  private BigDecimal amount;

  @ApiModelProperty(value = "要充值的用户编码列表", required = true)
  @NotEmpty(message = "用户编码列表不能为空")
  private Set<String> accountCodes;
}
