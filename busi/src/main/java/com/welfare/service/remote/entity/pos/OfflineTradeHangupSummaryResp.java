package com.welfare.service.remote.entity.pos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/3/22 1:48 下午
 */
@Data
public class OfflineTradeHangupSummaryResp {

  @ApiModelProperty("今日挂起订单数")
  private Integer todayOrderCount;

  @ApiModelProperty("今日挂起交易额，单位“分”")
  private Long todayOrderAmount;

  @ApiModelProperty("今日离线挂起人数")
  private Integer todayAccountCount;

  @ApiModelProperty("累计挂起订单数")
  private Integer totalOrderCount;

  @ApiModelProperty("累计挂起人数")
  private Integer totalAccountCount;
}
