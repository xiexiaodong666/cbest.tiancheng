package com.welfare.service.remote.entity.pos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/1/29 11:35 上午
 */
@Data
@ApiModel("价格模板扣费策略")
public class PriceTemplatePolicy {

  @ApiModelProperty(value = "帐户类型", required = true)
  @NotBlank(message = "帐户类型不能为空")
  private String accountType;

  @ApiModelProperty(value = "扣费策略，0.同时段单次计费 1.按次计费", required = true)
  @NotNull(message = "扣费策略不能为空")
  private Integer policyType;
}
