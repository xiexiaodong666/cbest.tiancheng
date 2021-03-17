package com.welfare.service.remote.entity.request;

import java.math.BigDecimal;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.welfare.service.dto.payment.PaymentRequest;
import lombok.Data;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/3/11 4:09 PM
 */
@Data
public class WoLifeAccountDeductionRowsRequest {

  /**
   * 商品名称
   */
  @NotBlank
  private String name;

  /**
   * 商品单价
   */
  @NotNull
  private BigDecimal price;

  /**
   * 购买数量
   */
  @NotNull
  private Integer count;

  /**
   * 商品编号
   */
  @NotBlank
  private String saleUnId;

  public static WoLifeAccountDeductionRowsRequest of(PaymentRequest paymentRequest){
    WoLifeAccountDeductionRowsRequest woLifeAccountDeductionRowsRequest = new WoLifeAccountDeductionRowsRequest();
    woLifeAccountDeductionRowsRequest.setCount(1);
    woLifeAccountDeductionRowsRequest.setName("重百线下消费商品");
    woLifeAccountDeductionRowsRequest.setPrice(paymentRequest.getAmount());
    woLifeAccountDeductionRowsRequest.setSaleUnId("cbest-offline-default");
    return woLifeAccountDeductionRowsRequest;
  }
}
