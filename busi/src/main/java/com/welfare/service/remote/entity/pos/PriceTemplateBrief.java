package com.welfare.service.remote.entity.pos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/1/29 10:33 上午
 */
@Data
@ApiModel("价格模板简要信息")
public class PriceTemplateBrief {

  @ApiModelProperty("主键")
  private Long id;

  @ApiModelProperty("模板名称")
  @NotBlank(message = "模板名称不能为空")
  private String name;

  @ApiModelProperty("门店号")
  @NotBlank(message = "模门店号不能为空")
  private String storeCode;

  @ApiModelProperty("门店名称")
  @NotBlank(message = "门店名称不能为空")
  private String storeName;

  @ApiModelProperty("创建时间，格式“yyyy-MM-dd HH:mm:ss”")
  @NotBlank(message = "创建时间不能为空")
  private String createTime;
}