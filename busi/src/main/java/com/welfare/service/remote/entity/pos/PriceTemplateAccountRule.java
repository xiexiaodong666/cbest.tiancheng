package com.welfare.service.remote.entity.pos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/1/29 11:34 上午
 */
@Data
@ApiModel("帐户扣费规则")
public class PriceTemplateAccountRule {

  @ApiModelProperty(value = "帐户类型", required = true)
  @NotBlank(message = "帐户类型不能为空")
  private String accountType;

  @ApiModelProperty(value = "价格，单位“分”", required = true)
  @NotNull(message = "价格不能为空")
  @Min(message = "价格不能小于0", value = 0)
  private Integer price;
}
