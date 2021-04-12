package com.welfare.service.dto.nhc;

import com.welfare.service.utils.PageReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/4/12 10:54 上午
 */
@Data
public class NhcUserPageReq extends PageReq {

  @ApiModelProperty(value = "用户/员工编码", required = true)
  @NotEmpty(message = "编码不能为空")
  private String code;
}
