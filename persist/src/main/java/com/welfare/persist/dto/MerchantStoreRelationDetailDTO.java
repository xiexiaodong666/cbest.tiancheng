package com.welfare.persist.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/1/8 7:03 PM
 */
@Data
public class MerchantStoreRelationDetailDTO {

  /**
   * 商户名称
   */
  @ApiModelProperty("商户名称")
  private String merName;

  /**
   * 商户编码
   */
  @ApiModelProperty("商户编码")
  private String merCode;

  /**
   * 是否返利
   */
  @ApiModelProperty("是否返利, 1返利, 0不返利")
  private Integer isRebate;

  /**
   * 门店，消费方式集合
   */
  @ApiModelProperty("门店，消费方式集合")
  private List<AdminMerchantStore> adminMerchantStoreList;


  /**
   * 备注
   */
  @ApiModelProperty("备注")
  private String ramark;
}
