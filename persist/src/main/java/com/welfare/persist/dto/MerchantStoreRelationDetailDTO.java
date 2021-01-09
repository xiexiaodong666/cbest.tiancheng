package com.welfare.persist.dto;

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
   * 商户编码
   */
  @ApiModelProperty("商户编码")
  private String merCode;

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
