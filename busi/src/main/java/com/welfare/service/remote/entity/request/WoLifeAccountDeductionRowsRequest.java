package com.welfare.service.remote.entity.request;

import com.welfare.service.dto.payment.PaymentRequest;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

import static com.welfare.common.constants.WelfareConstant.DEFAULT_SALE_UNID;
import static com.welfare.common.constants.WelfareConstant.DEFAULT_SALE_UNNAME;

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
    woLifeAccountDeductionRowsRequest.setName(DEFAULT_SALE_UNNAME);
    woLifeAccountDeductionRowsRequest.setPrice(paymentRequest.getAmount());
    woLifeAccountDeductionRowsRequest.setSaleUnId(DEFAULT_SALE_UNID);
    return woLifeAccountDeductionRowsRequest;
  }
}
