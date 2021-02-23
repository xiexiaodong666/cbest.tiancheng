package com.welfare.persist.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/2/23 3:12 下午
 */
@Data
public class AccountConsumeMerStoreRelationDTO {

  @ApiModelProperty("商户供应商编码")
  private String supplierCode;

  @ApiModelProperty("商户供应商名称")
  private String supplierName;

  @ApiModelProperty("消费门店配置")
  private List<AccountConsumeStoreRelationDTO> consumeStoreRelationDTOS;
}
