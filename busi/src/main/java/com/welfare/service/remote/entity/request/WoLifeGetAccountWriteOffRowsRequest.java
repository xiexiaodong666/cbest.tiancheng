package com.welfare.service.remote.entity.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/3/11 5:05 PM
 */
@Data
public class WoLifeGetAccountWriteOffRowsRequest {

  /**
   * 商品编号
   */
  @NotBlank
  private String saleUnId;
}
