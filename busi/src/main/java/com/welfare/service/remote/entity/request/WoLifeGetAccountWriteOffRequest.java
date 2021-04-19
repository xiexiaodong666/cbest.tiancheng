package com.welfare.service.remote.entity.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/3/11 4:52 PM
 */
@Data
public class WoLifeGetAccountWriteOffRequest {

  /**
   * 用户手机号
   */
  @NotBlank
  private String phone;

  @NotNull
  private WoLifeGetAccountWriteOffDataRequest data;
}
