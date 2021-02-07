package com.welfare.service.remote.entity.pos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/1/29 10:42 上午
 */
@Data
@ApiModel("收银机价格模板分页查询")
@Builder
public class PosPriceTemplateReq {

  @ApiModelProperty("模板名称")
  private String name;

  @ApiModelProperty("门店号")
  private List<String> storeCodes;

  @ApiModelProperty("分页条件")
  @NotNull(message = "分页条件不能为空")
  private PagingCondition paging;
}
