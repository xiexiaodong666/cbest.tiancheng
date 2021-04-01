package com.welfare.persist.dto.commodityOfflineOrder;

import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import lombok.Data;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/3/17 8:22 PM
 */
@Data
public class CommodityOfflineOrderTotalResponse {

  /**
   * 商品id
   */
  @ApiModelProperty("商品id")
  private String goodsId;

  /**
   * 商品名称
   */
  @ApiModelProperty("商品名称")
  private String goodsName;

  /**
   * 销量
   */
  @ApiModelProperty("销量")
  private Long saleCount;

  /**
   * 销售额
   */
  @ApiModelProperty("销售额")
  private BigDecimal sales;

  /**
   * 消费人次
   */
  @ApiModelProperty("消费人次")
  private Integer salePeopleCount;
}
