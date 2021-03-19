package com.welfare.service.dto.offline;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/3/19 2:55 下午
 */
@Data
public class OfflineOrderDTO {

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
