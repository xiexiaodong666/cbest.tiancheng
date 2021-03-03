package com.welfare.persist.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yaoxiao
 * @version 0.0.1
 * @date 2021/1/8 19:00
 */
@Data
@ApiModel("员工类型消费配置")
public class AccountConsumeStoreRelationDTO {

  @ApiModelProperty("门店消费配置关联关系表ID")
  private String id;

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
