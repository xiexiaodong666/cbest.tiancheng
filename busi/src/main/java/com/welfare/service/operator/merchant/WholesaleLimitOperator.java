package com.welfare.service.operator.merchant;

import com.welfare.common.constants.WelfareConstant;
import com.welfare.persist.entity.MerchantCredit;
import com.welfare.service.enums.IncOrDecType;
import com.welfare.service.operator.merchant.domain.MerchantAccountOperation;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Description:
 *
 * @author  rui.gao
 * @date 1/9/2021 10:43 PM
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class WholesaleLimitOperator extends AbstractMerAccountTypeOperator implements InitializingBean{
  private WelfareConstant.MerCreditType merCreditType = WelfareConstant.MerCreditType.WHOLESALE_CREDIT_LIMIT;
  @Autowired
  private RemainingLimitOperator remainingLimitOperator;

  @Override
  public List<MerchantAccountOperation> decrease(MerchantCredit merchantCredit, BigDecimal amount, String transNo, String transType) {
    log.info("ready to decrease merchantCredit.wholesaleLimit for {}", amount.toString());

    return doWhenNotEnough(merchantCredit, amount, merchantCredit.getCreditLimit(), transNo,transType );
  }

  @Override
  public List<MerchantAccountOperation> increase(MerchantCredit merchantCredit, BigDecimal amount, String transNo, String transType) {
    log.info("ready to increase merchantCredit.wholesaleLimit for {}", amount.toString());
    return doWhenMoreThan(merchantCredit, amount, transNo, transType);
  }

  @Override
  protected List<MerchantAccountOperation> doWhenNotEnough(MerchantCredit merchantCredit, BigDecimal amountLeftToBeDecrease, BigDecimal operatedAmount, String transNo, String transType) {
    List<MerchantAccountOperation> operations = new ArrayList<>();
    BigDecimal oldCreditLimit = merchantCredit.getWholesaleCreditLimit();
    BigDecimal oldRemainingLimit = merchantCredit.getRemainingLimit();

    merchantCredit.setCreditLimit(oldCreditLimit.subtract(amountLeftToBeDecrease));
    MerchantAccountOperation creditLimitLimitOperator = MerchantAccountOperation.of(
            merCreditType,
            amountLeftToBeDecrease,
            IncOrDecType.DECREASE,
            merchantCredit,
            transNo,
            transType
    );
    operations.add(creditLimitLimitOperator);
    AbstractMerAccountTypeOperator nextOperator = getNext();
    // 减剩余授信额度，不够减就为负数
    merchantCredit.setWholesaleCredit(oldRemainingLimit.subtract(amountLeftToBeDecrease));
    MerchantAccountOperation remainingLimitLimitOperator = MerchantAccountOperation.of(
            WelfareConstant.MerCreditType.WHOLESALE_CREDIT,
            amountLeftToBeDecrease,
            IncOrDecType.DECREASE,
            merchantCredit,
            transNo,
            transType
    );
    operations.add(remainingLimitLimitOperator);
    return operations;
  }

  @Override
  protected List<MerchantAccountOperation> doWhenMoreThan(MerchantCredit merchantCredit, BigDecimal amountLeftToBeIncrease, String transNo, String transType) {
    AbstractMerAccountTypeOperator nextOperator = getNext();
    List<MerchantAccountOperation> operations = new ArrayList<>();
    BigDecimal creditLimit = merchantCredit.getWholesaleCreditLimit();

    // 加信用额度
    merchantCredit.setWholesaleCreditLimit(creditLimit.add(amountLeftToBeIncrease));
    MerchantAccountOperation wholesaleCreditLimitOperator = MerchantAccountOperation.of(
            merCreditType,
            amountLeftToBeIncrease,
            IncOrDecType.INCREASE,
            merchantCredit,
            transNo,
            transType
    );
    operations.add(wholesaleCreditLimitOperator);
    // 加剩余信用额度
    List<MerchantAccountOperation> moreOperations = nextOperator.increase(merchantCredit,amountLeftToBeIncrease,transNo,transType );
    operations.addAll(moreOperations);
    return operations;
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    // TODO
    //super.next(remainingLimitOperator);
  }

  @Override
  public List<MerchantAccountOperation> set(MerchantCredit merchantCredit, BigDecimal amount, String transNo) {
    BigDecimal creditLimit = merchantCredit.getWholesaleCreditLimit();
    if (amount.compareTo(creditLimit) >= 0 ) {
      return increase(merchantCredit,amount.subtract(creditLimit),transNo, WelfareConstant.TransType.DEPOSIT_INCR.code());
    } else {
      return decrease(merchantCredit,creditLimit.subtract(amount),transNo, WelfareConstant.TransType.DEPOSIT_DECR.code());
    }
  }
}
