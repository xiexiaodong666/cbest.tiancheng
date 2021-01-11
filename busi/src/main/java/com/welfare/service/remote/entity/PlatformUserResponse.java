package com.welfare.service.remote.entity;

import java.util.List;
import lombok.Data;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/1/11 10:12 AM
 */
@Data
public class PlatformUserResponse<T> {

  private Integer code;
  private String msg;
  private T data;

  public PlatformUserResponse(Integer code, String msg, T data) {
    this.code = code;
    this.msg = msg;
    this.data = data;
  }

}
