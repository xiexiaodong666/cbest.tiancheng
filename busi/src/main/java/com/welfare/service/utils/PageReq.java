package com.welfare.service.utils;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author duanhy
 * @version 1.0.0
 * @description
 * @date 2021/1/13  11:30 AM
 */
@Data
public class PageReq {

  @ApiModelProperty("页码从1开始")
  private Integer current = 1;

  @ApiModelProperty("每页大小")
  private Integer size = 10;
}