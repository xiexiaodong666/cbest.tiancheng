package com.welfare.service.dto.pos;

import com.welfare.service.utils.PageReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/1/29 4:13 下午
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("收银机价格模板分页查询")
public class PriceTemplateQueryReq extends PageReq {

  @ApiModelProperty("模板名称")
  private String name;

  @ApiModelProperty("门店号")
  private String storeCode;
}
