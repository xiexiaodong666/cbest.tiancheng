package com.welfare.service.remote.entity.request;

import com.welfare.service.dto.payment.PaymentRequest;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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

  public static WoLifeGetAccountDeductionRequest of(PaymentRequest paymentRequest){
    WoLifeGetAccountDeductionRequest woLifeGetAccountDeductionRequest = new WoLifeGetAccountDeductionRequest();
    woLifeGetAccountDeductionRequest.phone = paymentRequest.getPhone();
    woLifeGetAccountDeductionRequest.data = WoLifeGetAccountDeductionDataRequest.of(paymentRequest);
    return woLifeGetAccountDeductionRequest;
  }
}
