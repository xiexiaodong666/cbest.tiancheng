package com.welfare.service.remote.entity.request;

import javax.validation.constraints.NotBlank;

import com.welfare.service.dto.RefundRequest;
import lombok.Data;

import static com.welfare.common.constants.WelfareConstant.DEFAULT_SALE_UNID;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/3/11 4:38 PM
 */
@Data
public class WoLifeRefundWriteOffRowsRequest {

  /**
   * 商品编号
   */
  @NotBlank
  private String saleUnId;

  public static WoLifeRefundWriteOffRowsRequest of(RefundRequest refundRequest){
    WoLifeRefundWriteOffRowsRequest woLifeRefundWriteOffRowsRequest = new WoLifeRefundWriteOffRowsRequest();
    woLifeRefundWriteOffRowsRequest.setSaleUnId(DEFAULT_SALE_UNID);
    return woLifeRefundWriteOffRowsRequest;
  }
}
