package com.welfare.persist.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/1/8 5:07 PM
 */
@Data
public class AdminMerchantStore {

  /**
   * merchantStoreId
   */
  @ApiModelProperty("merchantStoreId")
  private String merchantStoreId;

  /**
   * 门店编码
   */
  @ApiModelProperty("门店编码")
  private String storeCode;

  /**
   * 门店别名
   */
  @ApiModelProperty("门店别名")
  private String storeAlias;

  /**
   * 消费方式
   */
  @ApiModelProperty("消费方式,json"
      + "{"
      + "  \"o2o\": true,"
      + "  \"onlineMall\": true,"
      + "  \"shopShopping\": false"
      + "}")
  private String consumType;

  /**
   * 是否返利
   */
  @ApiModelProperty("是否返利")
  private Integer isRebate;

  /**
   * 返利类型
   */
  @ApiModelProperty("返利类型")
  private String rebateType;
  /**
   * 返利比率
   */
  @ApiModelProperty("返利比率")
  private Integer rebateRatio;

}
