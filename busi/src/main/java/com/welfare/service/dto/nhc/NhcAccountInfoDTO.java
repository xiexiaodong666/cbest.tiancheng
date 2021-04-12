package com.welfare.service.dto.nhc;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/4/12 10:22 上午
 */
@Data
public class NhcAccountInfoDTO {

  @ApiModelProperty("员工名称")
  private String accountName;

  @ApiModelProperty("员工编码")
  private String accountCode;

  @ApiModelProperty("手机号")
  private String phone;

  @ApiModelProperty("组织编码")
  private String departmentCode;

  @ApiModelProperty("组织名称")
  private String departmentName;

  @ApiModelProperty("采购余额")
  private BigDecimal procurementBalance;

  @ApiModelProperty("账号状态(1正常2禁用)")
  private Integer status;
}
