package com.welfare.servicemerchant.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.Data;

/**
 * @author yaoxiao
 * @version 0.0.1
 * @date 2021/1/7 17:03
 */
@Data
@ApiModel("供应商门店DTO")
public class AccountConsumeStoreResp implements Serializable {
  private Long id;
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
   * 门店层级
   */
  @ApiModelProperty("门店层级")
  private Integer storeLevel;
  /**
   * 父级门店
   */
  @ApiModelProperty("父级门店")
  private String storeParent;
  /**
   * 门店路径
   */
  @ApiModelProperty("门店路径")
  private String storePath;
  /**
   * 备注
   */
  @ApiModelProperty("备注")
  private String remark;
  /**
   * 门店可支持消费方式
   */
  @ApiModelProperty("门店可支持消费方式")
  private String consumType;

  /**
   * 员工选择的该门店消费方式
   */
  @ApiModelProperty("员工选择的该门店消费方式")
  private String selectConsumType;
}
