package com.welfare.service.remote.entity.pos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/1/29 11:51 上午
 */
@Data
public class TerminalPriceTemplateReq {

  @ApiModelProperty(value = "查询条件", required = true)
  @NotNull(message = "查询条件不能为空")
  private PagingCondition paging;

  @ApiModelProperty("门店号")
  private String storeCode;
}
