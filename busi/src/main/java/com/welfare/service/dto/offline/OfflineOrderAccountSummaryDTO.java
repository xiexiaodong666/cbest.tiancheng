package com.welfare.service.dto.offline;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/3/19 3:01 下午
 */
@Data
public class OfflineOrderAccountSummaryDTO {

  @ApiModelProperty("部门名称")
  private String departmentName;

  @ApiModelProperty("帐户名称")
  private String accountName;

  @ApiModelProperty("手机号")
  private String phone;

  @ApiModelProperty("挂起订单数")
  private Integer orderCount;

  @ApiModelProperty("挂起订单金额，单位“分”")
  private Long orderAmount;
}
