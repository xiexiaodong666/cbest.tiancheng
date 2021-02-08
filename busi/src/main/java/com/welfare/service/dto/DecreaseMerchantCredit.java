package com.welfare.service.dto;

import com.welfare.common.constants.WelfareConstant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/2/2 4:52 下午
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DecreaseMerchantCredit {

  private String merCode;
  private WelfareConstant.MerCreditType merCreditType;
  private BigDecimal amount;
  private String transNo;
  private String transType;

  public static DecreaseMerchantCredit of(Deposit deposit) {
    DecreaseMerchantCredit decreaseMerchantCredit = new DecreaseMerchantCredit();
    if (Objects.nonNull(deposit)) {
      decreaseMerchantCredit.setMerCode(deposit.getMerchantCode());
      decreaseMerchantCredit.setMerCreditType(WelfareConstant.MerCreditType.RECHARGE_LIMIT);
      decreaseMerchantCredit.setAmount(deposit.getAmount());
      decreaseMerchantCredit.setTransNo(deposit.getTransNo());
      decreaseMerchantCredit.setTransType(WelfareConstant.TransType.DEPOSIT_DECR.code());
    }
    return decreaseMerchantCredit;
  }

  public static List<DecreaseMerchantCredit> of(List<Deposit> deposits) {
    List<DecreaseMerchantCredit> decreaseMerchantCredits = new ArrayList<>();
    if (CollectionUtils.isNotEmpty(deposits)) {
      deposits.forEach(deposit -> {
        DecreaseMerchantCredit decreaseMerchantCredit = new DecreaseMerchantCredit();
        decreaseMerchantCredit.setMerCode(deposit.getMerchantCode());
        decreaseMerchantCredit.setMerCreditType(WelfareConstant.MerCreditType.RECHARGE_LIMIT);
        decreaseMerchantCredit.setAmount(deposit.getAmount());
        decreaseMerchantCredit.setTransNo(deposit.getTransNo());
        decreaseMerchantCredit.setTransType(WelfareConstant.TransType.DEPOSIT_DECR.code());
        decreaseMerchantCredits.add(decreaseMerchantCredit);
      });
    }
    return decreaseMerchantCredits;
  }
}
