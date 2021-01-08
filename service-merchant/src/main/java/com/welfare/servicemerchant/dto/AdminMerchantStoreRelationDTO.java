package com.welfare.servicemerchant.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import java.util.List;
import lombok.Data;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/1/8 11:12 AM
 */
@Data
@ApiModel("商户消费场景配置")
public class AdminMerchantStoreRelationDTO {

  /**
   * 商户代码
   */
  @ApiModelProperty("商户代码")
  private String merCode;

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
   * 创建时间
   */
  @ApiModelProperty("创建时间")
  private Date createTime;

  /**
   * 备注
   */
  @ApiModelProperty("备注")
  private String ramark;


  /**
   * 配置门店
   */
  @ApiModelProperty("备注")
  private List<MerchantStore> merchantStores;
}

@Data
class MerchantStore{

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
