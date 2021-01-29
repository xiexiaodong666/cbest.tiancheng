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
@ApiModel("价格模板进餐配置")
public class PriceTemplateMeal {

  @ApiModelProperty(value = "进餐类型，1.早餐 2.午餐 3.晚餐 4.夜宵", required = true)
  @NotNull(message = "进餐类型不能为空")
  private Integer mealType;

  @ApiModelProperty(value = "是否有效，0.无效 1.有效", required = true)
  @NotNull(message = "是否有效不能为空")
  private Integer effected;

  @ApiModelProperty(value = "开始时间，格式“HH:mm”", required = true)
  @NotBlank(message = "开始时间不能为空")
  private String beginTime;

  @ApiModelProperty(value = "结束时间，格式“HH:mm”", required = true)
  @NotBlank(message = "结束时间不能为空")
  private String endTime;

  @ApiModelProperty(value = "价格，单位“分”", required = true)
  @NotNull(message = "价格不能为空")
  @Min(message = "价格不能小于0", value = 0)
  private Integer price;

  @ApiModelProperty(value = "帐户扣费规则", required = true)
  @NotNull(message = "帐户扣费规则不能为空")
  private PriceTemplateAccountRule accountRules;
}
