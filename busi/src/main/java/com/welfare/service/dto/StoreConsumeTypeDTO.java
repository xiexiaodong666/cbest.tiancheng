package com.welfare.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/2/1 3:32 PM
 */
@Data
@NoArgsConstructor
public class StoreConsumeTypeDTO {

  /**
   * 消费方法
   */
  @ApiModelProperty("消费方法")
  private String consumeType;

  /**
   * 虚拟收银机号
   */
  @ApiModelProperty("虚拟收银机号")
  private String cashierNo;

}
