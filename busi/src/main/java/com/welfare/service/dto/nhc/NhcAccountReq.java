package com.welfare.service.dto.nhc;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/4/12 9:43 上午
 */
@Data
public class NhcAccountReq {

  @ApiModelProperty(value = "员工名称", required = true)
  @NotEmpty(message = "员工名称为空")
  private String accountName;

  @ApiModelProperty("员工编码（修改要传)")
  private String accountCode;

  @ApiModelProperty(value = "商户CODE", required = true)
  @NotEmpty(message = "商户CODE为空")
  private String merCode;

  @ApiModelProperty(value = "员工手机号码", required = true)
  @NotEmpty(message = "员工手机号码为空")
  private String phone;

  @ApiModelProperty(value = "所属部门Code")
  private String departmentCode;

  @ApiModelProperty(value = "账号状态(1正常2禁用)", required = true)
  @NotNull(message = "账号状态为空")
  private Integer status;

  public static NhcAccountReq of(NhcUserReq userReq) {
    NhcAccountReq nhcAccountReq = new NhcAccountReq();
    if (Objects.nonNull(userReq)) {
      nhcAccountReq.setAccountName(userReq.getUserName());
      nhcAccountReq.setAccountCode(userReq.getAccountCode());
      nhcAccountReq.setMerCode(userReq.getMerCode());
      nhcAccountReq.setPhone(userReq.getPhone());
    }
    return nhcAccountReq;
  }
}
