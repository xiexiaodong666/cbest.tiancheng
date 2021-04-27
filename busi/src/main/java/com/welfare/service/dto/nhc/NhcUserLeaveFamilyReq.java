package com.welfare.service.dto.nhc;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/4/12 10:29 上午
 */
@Data
public class NhcUserLeaveFamilyReq {

  @ApiModelProperty(value = "用户编码", required = true)
  @NotEmpty(message = "用户编码不能为空")
  private String userCode;
}
