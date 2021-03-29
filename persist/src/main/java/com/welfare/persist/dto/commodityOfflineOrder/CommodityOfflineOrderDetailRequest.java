package com.welfare.persist.dto.commodityOfflineOrder;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/3/17 8:22 PM
 */
@Data
public class CommodityOfflineOrderDetailRequest extends CommodityOfflineOrderTotalRequest {

  /**
   * 订单号
   */
  @ApiModelProperty("订单号")
  private String orderId;

  /**
   * 下单人
   */
  @ApiModelProperty("下单人")
  private String accountName;

  /**
   * 手机号
   */
  @ApiModelProperty("手机号")
  private String phone;
}
