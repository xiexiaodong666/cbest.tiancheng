package com.welfare.servicemerchant.dto;

import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Data;

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
   * 门店编码
   */
  @ApiModelProperty("门店编码")
  private String storeCode;

  /**
   * 门店，消费方式集合
   */
  @ApiModelProperty("门店，消费方式集合")
  private List<AdminMerchantStore> adminMerchantStoreList;

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

  /**
   * 备注
   */
  @ApiModelProperty("备注")
  private String ramark;
}
