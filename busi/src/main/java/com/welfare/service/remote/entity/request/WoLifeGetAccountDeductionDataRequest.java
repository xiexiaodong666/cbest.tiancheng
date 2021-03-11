package com.welfare.service.remote.entity.request;

import javax.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/3/11 4:48 PM
 */
@Data
public class WoLifeGetAccountDeductionDataRequest {

  /**
   * 订单id
   */
  @NotBlank
  private String oid;
}
