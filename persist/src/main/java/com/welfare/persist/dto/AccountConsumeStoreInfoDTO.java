package com.welfare.persist.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/2/24 3:26 下午
 */
@Data
public class AccountConsumeStoreInfoDTO {

  @ApiModelProperty("员工消费门店配置id")
  private Long id;

  /**
   * 员工类型ID
   */
  @ApiModelProperty("员工类型Code")
  private String accountTypeCode;
  /**
   * 员工类型名称
   */
  @ApiModelProperty("员工类型名称")
  private String accountTypeName;

  /**
   * 商户代码
   */
  @ApiModelProperty("商户代码")
  @NotEmpty(message = "商户代码为空")
  private String merCode;

  @ApiModelProperty("供应商编码")
  private String supplierCode;

  @ApiModelProperty("供应商名称")
  private String supplierName;
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
