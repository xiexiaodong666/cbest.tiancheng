package com.welfare.service.remote.entity.request;

import java.math.BigDecimal;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/3/11 4:09 PM
 */
@Data
public class WoLifeAccountDeductionRowsRequest {

  /**
   * 商品名称
   */
  @NotBlank
  private String name;

  /**
   * 商品单价
   */
  @NotNull
  private BigDecimal price;

  /**
   * 购买数量
   */
  @NotNull
  private Integer count;

  /**
   * 商品编号
   */
  @NotBlank
  private String saleUnId;
}
