package com.welfare.persist.dto.commodityOfflineOrder;

import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import lombok.Data;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/3/17 8:22 PM
 */
@Data
public class CommodityOfflineOrderTotalRequest {

  /**
   * 分页current
   */
  @ApiModelProperty("分页current")
  Integer current;

  /**
   * 分页size
   */
  @ApiModelProperty("分页size")
  Integer size;

  /**
   * 所属部门path集合
   */
  @ApiModelProperty("所属部门path集合")
  private List<String> departmentPathList;

  /**
   * 员工类型编码
   */
  @ApiModelProperty("员工类型编码")
  private List<String> accountTypeCodes;

  /**
   * 日期筛选开始
   */
  @ApiModelProperty("日期筛选开始")
  private Date filterDateStart;

  /**
   * 日期筛选结束
   */
  @ApiModelProperty("日期筛选结束")
  private Date filterDateEnd;

  /**
   * 商品id,查询明细需要传这个
   */
  @ApiModelProperty("商品id,明细需要传这个")
  private BigDecimal goodsId;

  /**
   * 商品名称
   */
  @ApiModelProperty("商品名称")
  private String goodsName;

  /**
   * 供应商编码
   */
  @ApiModelProperty("供应商编码")
  private String merCode;

  /**
   * 消费门店
   */
  @ApiModelProperty("消费门店")
  private String storeCode;

}
