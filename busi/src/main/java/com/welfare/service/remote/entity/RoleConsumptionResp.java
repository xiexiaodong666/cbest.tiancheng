package com.welfare.service.remote.entity;

import lombok.Builder;
import lombok.Data;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/1/11 11:05 PM
 */
@Data
public class RoleConsumptionResp<T> {

  private String code;
  private String msg;
  private T data;

  @Builder
  public RoleConsumptionResp(String code, String msg, T data) {
    this.code = code;
    this.msg = msg;
    this.data = data;
  }

  public RoleConsumptionResp() {

  }

}
