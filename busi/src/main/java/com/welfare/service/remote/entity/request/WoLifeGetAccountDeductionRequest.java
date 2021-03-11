package com.welfare.service.remote.entity.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/3/11 4:47 PM
 */
@Data
public class WoLifeGetAccountDeductionRequest {

  @NotBlank
  private String phone;

  @NotNull
  private WoLifeGetAccountDeductionDataRequest data;
}
