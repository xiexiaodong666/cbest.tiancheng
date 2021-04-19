package com.welfare.service.remote.entity.request;

import com.welfare.service.dto.payment.PaymentRequest;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/3/11 3:53 PM
 */
@Data
public class WoLifeAccountDeductionRequest {


  /**
   * data
   */
  @NotNull
  private WoLifeAccountDeductionDataRequest data;

  public static WoLifeAccountDeductionRequest of(PaymentRequest paymentRequest){
    WoLifeAccountDeductionRequest woLifeAccountDeductionRequest = new WoLifeAccountDeductionRequest();
    // woLifeAccountDeductionRequest.setPhone(paymentRequest.getPhone());
    woLifeAccountDeductionRequest.setData(WoLifeAccountDeductionDataRequest.of(paymentRequest));
    return woLifeAccountDeductionRequest;
  }
}
