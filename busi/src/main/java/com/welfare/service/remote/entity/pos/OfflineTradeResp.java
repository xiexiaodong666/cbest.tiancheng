package com.welfare.service.remote.entity.pos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/3/22 1:46 下午
 */
@Data
public class OfflineTradeResp {

  @ApiModelProperty("交易时间，格式“yyyy-MM-dd HH:mm:ss”")
  private String tradeTime;

  @ApiModelProperty("门店名称")
  private String storeName;

  @ApiModelProperty("部门名称")
  private String departmentName;

  @ApiModelProperty("帐户名称")
  private String accountName;

  @ApiModelProperty("手机号")
  private String phone;

  @ApiModelProperty("消费金额，单位“分”")
  private Integer amount;

  @ApiModelProperty("状态名称")
  private String statusName;
}
