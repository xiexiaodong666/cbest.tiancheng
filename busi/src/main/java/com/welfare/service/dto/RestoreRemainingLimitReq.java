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

  @ApiModelProperty(value = "商户编码",required = true)
  @NotEmpty(message = "商户编码不能为空")
  private String merCode;

  @ApiModelProperty(value = "结算单号",required = true)
  @NotEmpty(message = "结算单号不能为空")
  private String transNo;

  @ApiModelProperty(value = "本次恢复金额(不能小于0)",required = true)
  @DecimalMin(message = "金额不能小于0",value = "0")
  private BigDecimal amount;
}