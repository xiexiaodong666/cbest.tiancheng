package com.welfare.service.remote.entity.pos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
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

  @ApiModelProperty("价格模板简要信息")
  @NotNull(message = "价格模板简要信息不能为空")
  private PriceTemplateBrief brief;

  @ApiModelProperty("价格模板进餐配置")
  @NotNull(message = "价格模板进餐配置不能为空")
  private List<PriceTemplateMeal> meals;

  @ApiModelProperty("价格模板扣费策略")
  @NotNull(message = "价格模板扣费策略不能为空")
  private List<PriceTemplatePolicy> policies;

  @Data
  @ApiModel("价格模板进餐配置")
  public static class PriceTemplateMeal {

    @ApiModelProperty("进餐类型，1.早餐 2.午餐 3.晚餐 4.夜宵")
    @NotNull(message = "进餐类型不能为空")
    private Integer mealType;

    @ApiModelProperty("是否有效，0.无效 1.有效")
    @NotNull(message = "是否有效不能为空")
    private Integer effected;

    @ApiModelProperty("开始时间，格式“HH:mm”")
    @NotBlank(message = "开始时间不能为空")
    private String beginTime;

    @ApiModelProperty("结束时间，格式“HH:mm”")
    @NotBlank(message = "结束时间不能为空")
    private String endTime;

    @ApiModelProperty("价格，单位“分”")
    @NotNull(message = "价格不能为空")
    @Min(message = "价格不能小于0", value = 0)
    private Integer price;

    @ApiModelProperty("帐户扣费规则")
    @NotNull(message = "帐户扣费规则不能为空")
    private PriceTemplateAccountRule accountRules;

  }

  @Data
  @ApiModel("帐户扣费规则")
  public static class PriceTemplateAccountRule {

    @ApiModelProperty("帐户类型")
    @NotBlank(message = "帐户类型不能为空")
    private String accountType;

    @ApiModelProperty("价格，单位“分”")
    @NotNull(message = "价格不能为空")
    @Min(message = "价格不能小于0", value = 0)
    private Integer price;
  }

  @Data
  @ApiModel("价格模板扣费策略")
  public static class PriceTemplatePolicy {

    @ApiModelProperty("帐户类型")
    @NotBlank(message = "帐户类型不能为空")
    private String accountType;

    @ApiModelProperty("扣费策略，0.同时段单次计费 1.按次计费")
    @NotNull(message = "扣费策略不能为空")
    private Integer policyType;
  }
}
