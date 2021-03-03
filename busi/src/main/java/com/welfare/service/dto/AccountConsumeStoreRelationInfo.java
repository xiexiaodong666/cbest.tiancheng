package com.welfare.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/2/24 12:48 下午
 */
@Data
@ApiModel("员工类型消费门店配置")
public class AccountConsumeStoreRelationInfo {

  @ApiModelProperty("供应商编码")
  private String supplierCode;

  @ApiModelProperty("供应商名称")
  private String supplierName;
  /**
   * 门店编码
   */
  @ApiModelProperty("门店编码")
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
  private String sceneConsumType;
}
