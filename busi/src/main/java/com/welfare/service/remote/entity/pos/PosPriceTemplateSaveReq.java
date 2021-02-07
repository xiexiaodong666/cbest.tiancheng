package com.welfare.service.remote.entity.pos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/1/29 11:33 上午
 */
@Data
@ApiModel("价格模板新增")
public class PosPriceTemplateSaveReq {

  @ApiModelProperty(value = "模板名称", required = true)
  @NotBlank(message = "模板名称不能为空")
  private String name;

  @ApiModelProperty(value = "门店号", required = true)
  @NotBlank(message = "模门店号不能为空")
  private String storeCode;

  @ApiModelProperty(value = "价格模板进餐配置", required = true)
  @NotNull(message = "价格模板进餐配置不能为空")
  private List<PriceTemplateMeal> meals;

  @ApiModelProperty("价格模板扣费策略")
  private List<PriceTemplatePolicy> policies;
}
