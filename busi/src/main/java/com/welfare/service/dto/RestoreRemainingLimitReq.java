package com.welfare.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;

/**
 * @author duanhy
 * @version 1.0.0
 * @description
 * @date 2021/1/16  3:47 PM
 */
@Data
public class RestoreRemainingLimitReq {

  @ApiModelProperty("请求id")
  @NotEmpty(message = "请求id不能为空")
  private String requestId;

  @ApiModelProperty("商户编码")
  @NotEmpty(message = "商户编码不能为空")
  private String merCode;

  @ApiModelProperty("流水号")
  @NotEmpty(message = "流水号不能为空")
  private String transNo;

  @ApiModelProperty("本次恢复金额(不能小于0)")
  @DecimalMin(message = "金额不能小于0",value = "0")
  private BigDecimal amount;
}