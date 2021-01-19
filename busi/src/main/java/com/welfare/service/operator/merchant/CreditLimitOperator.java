package com.welfare.service.operator.merchant;

import com.welfare.common.constants.WelfareConstant;
import com.welfare.persist.entity.MerchantCredit;
import com.welfare.service.enums.IncOrDecType;
import com.welfare.service.operator.merchant.domain.MerchantAccountOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email kobe663@gmail.com
 * @date 1/9/2021 10:43 PM
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class CreditLimitOperator extends AbstractMerAccountTypeOperator implements InitializingBean{
  private WelfareConstant.MerCreditType merCreditType = WelfareConstant.MerCreditType.CREDIT_LIMIT;
  @Autowired
  private RemainingLimitOperator remainingLimitOperator;

  @Override
  public List<MerchantAccountOperation> decrease(MerchantCredit merchantCredit, BigDecimal amount, String transNo) {
    log.info("ready to decrease merchantCredit.creditLimit for {}", amount.toString());

    return doWhenNotEnough(merchantCredit, amount, merchantCredit.getCreditLimit(), transNo);
  }

  @Override
  public List<MerchantAccountOperation> increase(MerchantCredit merchantCredit, BigDecimal amount, String transNo) {
    log.info("ready to increase merchantCredit.creditLimit for {}", amount.toString());
    return doWhenMoreThan(merchantCredit, amount, transNo);
  }

  @Override
  protected List<MerchantAccountOperation> doWhenNotEnough(MerchantCredit merchantCredit, BigDecimal amountLeftToBeDecrease, BigDecimal operatedAmount, String transNo) {
    List<MerchantAccountOperation> operations = new ArrayList<>();
    BigDecimal oldCreditLimit = merchantCredit.getCreditLimit();
    BigDecimal oldRemainingLimit = merchantCredit.getRemainingLimit();

    merchantCredit.setCreditLimit(oldCreditLimit.subtract(amountLeftToBeDecrease));
    MerchantAccountOperation creditLimitLimitOperator = MerchantAccountOperation.of(
            merCreditType,
            amountLeftToBeDecrease,
            IncOrDecType.DECREASE,
            merchantCredit,
            transNo
    );
    operations.add(creditLimitLimitOperator);
    AbstractMerAccountTypeOperator nextOperator = getNext();
    // 减剩余授信额度，不够减就为负数
    merchantCredit.setRemainingLimit(oldRemainingLimit.subtract(amountLeftToBeDecrease));
    MerchantAccountOperation remainingLimitLimitOperator = MerchantAccountOperation.of(
            WelfareConstant.MerCreditType.REMAINING_LIMIT,
            amountLeftToBeDecrease,
            IncOrDecType.DECREASE,
            merchantCredit,
            transNo
    );
    operations.add(remainingLimitLimitOperator);
    return operations;
  }

  @Override
  protected List<MerchantAccountOperation> doWhenMoreThan(MerchantCredit merchantCredit, BigDecimal amountLeftToBeIncrease, String transNo) {
    AbstractMerAccountTypeOperator nextOperator = getNext();
    List<MerchantAccountOperation> operations = new ArrayList<>();
    BigDecimal creditLimit = merchantCredit.getCreditLimit();
    BigDecimal remainingLimit = merchantCredit.getRemainingLimit();
    // 加信用额度
    merchantCredit.setCreditLimit(creditLimit.add(amountLeftToBeIncrease));
    MerchantAccountOperation remainingLimitOperator = MerchantAccountOperation.of(
            merCreditType,
            amountLeftToBeIncrease,
            IncOrDecType.INCREASE,
            merchantCredit,
            transNo
    );
    operations.add(remainingLimitOperator);
    // 加剩余信用额度
    List<MerchantAccountOperation> moreOperations = nextOperator.increase(merchantCredit,amountLeftToBeIncrease,transNo);
    operations.addAll(moreOperations);
    return operations;
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    super.next(remainingLimitOperator);
  }

  @Override
  public List<MerchantAccountOperation> set(MerchantCredit merchantCredit, BigDecimal amount, String transNo) {
    BigDecimal creditLimit = merchantCredit.getCreditLimit();
    if (amount.compareTo(creditLimit) >= 0 ) {
      return increase(merchantCredit,amount.subtract(creditLimit),transNo);
    } else {
      return decrease(merchantCredit,creditLimit.subtract(amount),transNo);
    }
  }
}
