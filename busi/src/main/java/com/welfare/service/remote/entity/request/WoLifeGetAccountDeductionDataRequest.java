package com.welfare.service.remote.entity.request;

import com.welfare.service.dto.payment.PaymentRequest;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/3/11 4:48 PM
 */
@Data
public class WoLifeGetAccountDeductionDataRequest {

  /**
   * 订单id
   */
  @NotBlank
  private String oid;

  public static WoLifeGetAccountDeductionDataRequest of(PaymentRequest paymentRequest){
    WoLifeGetAccountDeductionDataRequest woLifeGetAccountDeductionDataRequest = new WoLifeGetAccountDeductionDataRequest();
    woLifeGetAccountDeductionDataRequest.setOid(paymentRequest.getTransNo());
    return woLifeGetAccountDeductionDataRequest;
  }
}
