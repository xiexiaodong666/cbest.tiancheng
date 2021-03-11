package com.welfare.service.remote.entity.request;

import javax.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/3/11 4:38 PM
 */
@Data
public class WoLifeRefundWriteOffRowsRequest {

  /**
   * 商品编号
   */
  @NotBlank
  private String saleUnId;
}
