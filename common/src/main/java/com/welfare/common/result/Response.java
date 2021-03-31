package com.welfare.common.result;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/3/23 7:18 下午
 */
@Data
@Builder
public class Response<T>{

  @ApiModelProperty(
          value = "code值",
          required = true
  )
  private int code;
  @ApiModelProperty(
          value = "是否成功",
          required = true
  )
  private boolean success;
  @ApiModelProperty(
          value = "消息",
          required = true
  )
  private String msg;
  @ApiModelProperty("返回对象")
  private T data;
}
