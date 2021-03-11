package com.welfare.service.remote.entity.request;

import java.math.BigDecimal;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/3/11 4:09 PM
 */
@Data
public class WoLifeAccountDeductionDataRequest {

  /**
   * 订单id
   */
  @NotBlank
  private String oid;

  /**
   * 订单购买种类数
   */
  @NotNull
  private Integer totalCount;

  /**
   * 订单总价格
   */
  @NotNull
  private BigDecimal totalPrice;

  /**
   * rows
   */
  @NotNull
  private List<WoLifeAccountDeductionRowsRequest> rows;
}
