package com.welfare.persist.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/2/24 1:02 下午
 */
@Data
public class MerSupplierStoreResp {

  @ApiModelProperty("供应商编码")
  private String supplierCode;

  @ApiModelProperty("供应商名称")
  private String supplierName;

  @ApiModelProperty("消费门店")
  private List<MerSupplierStoreDTO> supplierStoreDTOS;
}
