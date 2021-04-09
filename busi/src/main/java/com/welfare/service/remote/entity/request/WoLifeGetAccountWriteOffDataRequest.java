package com.welfare.service.remote.entity.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/3/11 5:04 PM
 */
@Data
public class WoLifeGetAccountWriteOffDataRequest {

  /**
   * 订单id
   */
  @NotBlank
  private String oid;

  @NotNull
  private List<WoLifeGetAccountWriteOffRowsRequest> rows;
}
