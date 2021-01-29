package com.welfare.service.dto.pos;

import com.welfare.service.utils.PageReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/1/29 4:15 下午
 */
@Data
public class TerminalPriceTemplateQueryReq extends PageReq {

  @ApiModelProperty("门店号")
  private String storeCode;
}
