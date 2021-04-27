package com.welfare.service.dto.nhc;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/4/12 11:50 上午
 */
@Data
public class NhcQueryUserReq {

  @ApiModelProperty("用户编码")
  private String accountCode;

  @ApiModelProperty("手机号")
  private String phone;
}
