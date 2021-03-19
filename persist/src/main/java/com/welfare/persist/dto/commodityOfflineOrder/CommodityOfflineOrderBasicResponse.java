package com.welfare.persist.dto.commodityOfflineOrder;

import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Data;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/3/17 8:35 PM
 */
@Data
public class CommodityOfflineOrderBasicResponse<T> {

  @ApiModelProperty("数据")
  private List<T> records;    //数据
  @ApiModelProperty("总条数")
  private long total;         //总数
  @ApiModelProperty("每页显示数量")
  private long size;          //每页显示数量
  @ApiModelProperty("当前页")
  private long current;       //当前页

  @ApiModelProperty("汇总数据")
  private CommodityOfflineOrderBasicExtResponse ext;//额外数据

}
