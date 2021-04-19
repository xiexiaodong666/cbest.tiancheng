package com.welfare.service.remote.entity.pos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 收银机价格模板
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/1/29 10:14 上午
 */
@Data
@ApiModel("价格模板")
public class PosPriceTemplate {

  @ApiModelProperty(value = "价格模板简要信息", required = true)
  @NotNull(message = "价格模板简要信息不能为空")
  private PriceTemplateBrief brief;

  @ApiModelProperty(value = "价格模板进餐配置", required = true)
  @NotNull(message = "价格模板进餐配置不能为空")
  private List<PriceTemplateMeal> meals;

  @ApiModelProperty("价格模板扣费策略")
  private List<PriceTemplatePolicy> policies;
}
