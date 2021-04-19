package com.welfare.service.remote.entity.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.welfare.common.enums.ConsumeTypeEnum;

import com.welfare.common.exception.BizException;
import com.welfare.service.dto.payment.PaymentRequest;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import org.apache.logging.log4j.util.Strings;


/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/3/11 4:09 PM
 */
@Data
public class WoLifeAccountDeductionDataRequest {
  /**
   * 订单id
   */
  @NotBlank
  private String oid;

  /**
   * 订单购买种类数
   */
  @NotNull
  private Integer totalCount;

  /**
   * 订单总价格
   */
  @NotNull
  private BigDecimal totalPrice;

  /**
   * rows
   */
  @NotNull
  private List<WoLifeAccountDeductionRowsRequest> rows;

  public static WoLifeAccountDeductionDataRequest of(PaymentRequest paymentRequest){
    WoLifeAccountDeductionDataRequest woLifeAccountDeductionDataRequest = new WoLifeAccountDeductionDataRequest();

    woLifeAccountDeductionDataRequest.setOid(paymentRequest.getTransNo());
    woLifeAccountDeductionDataRequest.setTotalPrice(paymentRequest.getAmount());
    String saleRows = paymentRequest.getSaleRows();

    // 处理线上商城支付请求
    if(Strings.isNotEmpty(saleRows) && ConsumeTypeEnum.ONLINE_MALL.getCode().equals(paymentRequest.getPaymentScene())) {
      ObjectMapper objectMapper = new ObjectMapper();
      try {
        List<WoLifeAccountDeductionRowsRequest> rows = objectMapper.readValue(saleRows, new TypeReference<List<WoLifeAccountDeductionRowsRequest>>(){});
        woLifeAccountDeductionDataRequest.setRows(rows);
        woLifeAccountDeductionDataRequest.setTotalCount(rows.size());
      } catch (JsonProcessingException e) {
        throw new BizException("[沃生活馆]支付异常:线上支付商品行参数转换错误" + saleRows);
      }

    } else {
      woLifeAccountDeductionDataRequest.setRows(Collections.singletonList(WoLifeAccountDeductionRowsRequest.of(paymentRequest)));
      woLifeAccountDeductionDataRequest.setTotalCount(1);
    }

    return woLifeAccountDeductionDataRequest;
  }
}
