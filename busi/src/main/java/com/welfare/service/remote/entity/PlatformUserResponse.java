package com.welfare.service.remote.entity;

import lombok.Builder;
import lombok.Data;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/1/11 10:12 AM
 */
@Data
public class PlatformUserResponse<T> {

  private Integer code;
  private String message;
  private T data;

  @Builder
  public PlatformUserResponse(Integer code, String message, T data) {
    this.code = code;
    this.message = message;
    this.data = data;
  }
  public PlatformUserResponse(){

  }

}
