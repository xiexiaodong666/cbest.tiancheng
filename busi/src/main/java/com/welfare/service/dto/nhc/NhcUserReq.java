package com.welfare.service.dto.nhc;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/4/12 10:47 上午
 */
@Data
public class NhcUserReq {

  @ApiModelProperty(value = "用户名称", required = true)
  @NotEmpty(message = "用户名称为空")
  private String userName;

  @ApiModelProperty("用户编码（修改要传)")
  private String accountCode;

  @ApiModelProperty("要加入家庭的用户编码")
  private String familyUserCode;

  @ApiModelProperty(value = "商户CODE", required = true)
  @NotEmpty(message = "商户CODE为空")
  private String merCode;

  @ApiModelProperty(value = "用户手机号码")
  private String phone;

  /**
   * 账号状态
   */
  @ApiModelProperty("账号状态(1正常 2禁用)")
  private Integer accountStatus;
}
