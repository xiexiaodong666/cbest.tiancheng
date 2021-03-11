package com.welfare.service.remote.entity.request;

import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

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
