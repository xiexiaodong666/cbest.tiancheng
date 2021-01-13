package com.welfare.persist.dto;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.Data;

/**
 * @author yaoxiao
 * @version 0.0.1
 * @date 2021/1/13 10:36
 */
@Data
public class MerSupplierStoreDTO implements Serializable {
  /**
   * 商户代码
   */
  @ApiModelProperty("商户代码")
  private String merCode;
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
   * 消费方式
   */
  @ApiModelProperty("消费方式  {  \"o2o\": true,  \"onlineMall\": true,  \"shopShopping\": false}")
  private String consumType;

}
