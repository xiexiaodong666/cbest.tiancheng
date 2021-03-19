package com.welfare.persist.dto.commodityOfflineOrder;

import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/3/17 8:22 PM
 */
@Data
public class CommodityOfflineOrderDetailResponse {

  /**
   * 消费时间
   */
  @ApiModelProperty("消费时间")
  private Date consumeTime;

  /**
   * 商品名称
   */
  @ApiModelProperty("商品名称")
  private String goodsName;

  /**
   * 订单号
   */
  @ApiModelProperty("订单号")
  private String orderId;

  /**
   * 销量
   */
  @ApiModelProperty("销量")
  private Integer saleCount;

  /**
   * 订单状态
   */
  @ApiModelProperty("订单状态")
  private String orderStatus;

  /**
   * 销售额
   */
  @ApiModelProperty("销售额")
  private BigDecimal sales;

  /**
   * 消费人
   */
  @ApiModelProperty("消费人")
  private String accountName;

  /**
   * 组织机构
   */
  @ApiModelProperty("组织机构")
  private String departmentName;

  /**
   * 手机号
   */
  @ApiModelProperty("手机号")
  private String phone;
}
