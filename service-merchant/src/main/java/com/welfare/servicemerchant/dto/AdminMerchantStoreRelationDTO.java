package com.welfare.servicemerchant.dto;

import com.welfare.persist.dto.AdminMerchantStore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/1/8 11:12 AM
 */
@Data
@ApiModel("商户消费场景配置")
public class AdminMerchantStoreRelationDTO {
  /**
   * id
   */
  @ApiModelProperty("id")
  private String id;


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
   * 状态
   */
  @ApiModelProperty("状态 0 正常,1 禁用")
  private String status;


  /**
   * 配置门店
   */
  @ApiModelProperty("备注")
  private List<AdminMerchantStore> merchantStores;
}

