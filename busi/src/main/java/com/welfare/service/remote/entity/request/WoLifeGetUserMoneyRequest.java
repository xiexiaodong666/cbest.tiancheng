package com.welfare.service.remote.entity.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/3/15 11:28 AM
 */
@Data
public class WoLifeGetUserMoneyRequest {

  @NotBlank
  private String phone;
}
