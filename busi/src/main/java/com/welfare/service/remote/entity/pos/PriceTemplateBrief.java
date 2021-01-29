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

  @ApiModelProperty(value = "主键", required = true)
  @NotBlank(message = "主键不能为空")
  private Long id;

  @ApiModelProperty(value = "模板名称", required = true)
  @NotBlank(message = "模板名称不能为空")
  private String name;

  @ApiModelProperty(value = "门店号", required = true)
  @NotBlank(message = "模门店号不能为空")
  private String storeCode;

  @ApiModelProperty(value = "门店名称", required = true)
  @NotBlank(message = "门店名称不能为空")
  private String storeName;

  @ApiModelProperty(value = "创建时间，格式“yyyy-MM-dd HH:mm:ss”", required = true)
  @NotBlank(message = "创建时间不能为空")
  private String createTime;
}