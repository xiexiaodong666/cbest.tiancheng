package com.welfare.servicemerchant.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.Data;

/**
 * @author yaoxiao
 * @version 0.0.1
 * @date 2021/1/7 17:03
 */
@Data
@ApiModel("供应商门店DTO")
public class AccountConsumeStoreResp implements Serializable {

  /**
   * 门店代码
   */
  @ApiModelProperty("门店代码")
  private String storeCode;
  /**
   * 门店名称
   */
  @ApiModelProperty("门店名称")
  private String storeName;
  /**
   * 门店可支持消费方式
   */
  @ApiModelProperty("门店可支持消费方式")
  private String consumType;

  /**
   * 员工选择的该门店消费方式
   */
  @ApiModelProperty("员工选择的该门店消费方式")
  private String selectConsumType;
}
