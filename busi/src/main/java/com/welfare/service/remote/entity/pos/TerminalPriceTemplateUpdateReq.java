package com.welfare.service.remote.entity.pos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/1/29 11:53 上午
 */
@Data
public class TerminalPriceTemplateUpdateReq {

  @ApiModelProperty(value = "主键", required = true)
  @NotNull(message = "主键")
  private Long id;

  @ApiModelProperty(value = "模板主键", required = true)
  @NotNull(message = "模板主键")
  private Long templateId;
}
