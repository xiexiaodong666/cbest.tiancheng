package com.welfare.servicemerchant.dto;

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
   * 门店编码
   */
  @ApiModelProperty("门店编码")
  private String storeCode;

  /**
   * 门店别名
   */
  @ApiModelProperty("门店别名")
  private String storeAlias;
}
