package com.welfare.persist.dto.commodityOfflineOrder;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/3/18 7:20 PM
 */
@Data
public class CommodityOfflineOrderExcel {


  /**
   * 日期筛选结束
   */
  @ApiModelProperty("日期筛选结束")
  private String filterDate;

  /**
   * 供应商
   */
  @ApiModelProperty("商户编码")
  private String merName;

  /**
   * 消费门店
   */
  @ApiModelProperty("消费门店")
  private String storeCode;

  /**
   * 订单类型
   */
  @ApiModelProperty("订单类型")
  private String orderType;
}

