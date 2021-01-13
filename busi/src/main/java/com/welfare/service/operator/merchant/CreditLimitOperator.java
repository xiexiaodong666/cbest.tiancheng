package com.welfare.service.operator.merchant;

import com.welfare.common.constants.WelfareConstant;
import com.welfare.common.exception.BusiException;
import com.welfare.common.exception.ExceptionCode;
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
import java.util.Objects;

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
    BigDecimal creditLimit = merchantCredit.getCreditLimit();
    return super.decrease(merchantCredit, amount, transNo);
  }

  @Override
  public List<MerchantAccountOperation> increase(MerchantCredit merchantCredit, BigDecimal amount, String transNo) {
    log.info("ready to increase merchantCredit.creditLimit for {}", amount.toString());
    return doWhenMoreThan(merchantCredit, amount, transNo);
  }

  @Override
  protected List<MerchantAccountOperation> doWhenNotEnough(MerchantCredit merchantCredit, BigDecimal amountLeftToBeDecrease, String transNo) {
    return super.doWhenNotEnough(merchantCredit, amountLeftToBeDecrease, transNo);
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
    merchantCredit.setRemainingLimit(remainingLimit.add(amountLeftToBeIncrease));
    List<MerchantAccountOperation> moreOperations = nextOperator.increase(merchantCredit,amountLeftToBeIncrease,transNo);
    operations.addAll(moreOperations);
    return operations;
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    super.next(remainingLimitOperator);
  }
}
