package com.welfare.service.dto;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/2/24 1:16 下午
 */
@Data
public class AccountWelfareConsumeStoreRelationEditReq {

  /**
   * 门店编码
   */
  @ApiModelProperty("门店编码")
  @NotEmpty(message = "门店编码编码为空")
  private String storeCode;
  /**
   * 消费方式
   */
  @ApiModelProperty("消费方式（多个以逗号隔开)")
  @NotEmpty(message = "消费方式编码为空")
  private String sceneConsumType;
}
