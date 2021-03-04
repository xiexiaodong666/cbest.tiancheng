package com.welfare.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/2/24 11:52 上午
 */
@Data
public class AccountConsumeSceneSupplierDTO {

  @ApiModelProperty("供应商编码")
  private String supplierCode;

  @ApiModelProperty("供应商名称")
  private String supplierName;

  @ApiModelProperty("员工类型消费门店配置")
  private List<AccountConsumeStoreRelationInfo> consumeStoreRelationInfos;
}
