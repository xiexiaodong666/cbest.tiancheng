package com.welfare.service.remote.entity.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.welfare.service.dto.RefundRequest;
import lombok.Data;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/3/11 4:38 PM
 */
@Data
public class WoLifeRefundWriteOffRequest {

  /**
   * data
   */
  @NotNull
  private WoLifeRefundWriteOffDataRequest data;

  public static WoLifeRefundWriteOffRequest of(RefundRequest refundRequest){
    WoLifeRefundWriteOffRequest woLifeRefundWriteOffRequest = new WoLifeRefundWriteOffRequest();
    // woLifeRefundWriteOffRequest.setPhone(refundRequest.getPhone());
    woLifeRefundWriteOffRequest.setData(WoLifeRefundWriteOffDataRequest.of(refundRequest));
    return woLifeRefundWriteOffRequest;
  }
}
