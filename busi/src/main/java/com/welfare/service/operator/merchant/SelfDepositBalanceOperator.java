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
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 1/13/2021
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class SelfDepositBalanceOperator extends AbstractMerAccountTypeOperator implements InitializingBean {
    private final CurrentBalanceOperator currentBalanceOperator;
    private final WelfareConstant.MerCreditType merCreditType = WelfareConstant.MerCreditType.SELF_DEPOSIT;

    @Override
    public List<MerchantAccountOperation> decrease(MerchantCredit merchantCredit, BigDecimal amount, String transNo) {
        log.info("ready to decrease merchantCredit.selfDepositBalance for {}", amount.toString());
        BigDecimal selfDepositBalance = merchantCredit.getSelfDepositBalance();
        BigDecimal subtract = selfDepositBalance.subtract(amount);
        if (subtract.compareTo(BigDecimal.ZERO) < 0) {
            BigDecimal amountLeftToBeDecrease = subtract.negate();
            return doWhenNotEnough(merchantCredit, amountLeftToBeDecrease, selfDepositBalance , transNo);
        } else {
            merchantCredit.setSelfDepositBalance(subtract);
            MerchantAccountOperation operation = MerchantAccountOperation.of(
                    merCreditType,
                    amount,
                    IncOrDecType.DECREASE,
                    merchantCredit,
                    transNo
            );
            return Collections.singletonList(operation);
        }

    }

    @Override
    protected List<MerchantAccountOperation> doWhenNotEnough(MerchantCredit merchantCredit, BigDecimal amountLeftToBeDecrease, BigDecimal operatedAmount, String transNo) {
        AbstractMerAccountTypeOperator nextOperator = getNext();
        if (Objects.isNull(nextOperator)) {
            throw new BusiException(ExceptionCode.MERCHANT_RECHARGE_LIMIT_EXCEED, "余额不足", null);
        }
        merchantCredit.setSelfDepositBalance(BigDecimal.ZERO);
        MerchantAccountOperation operation = MerchantAccountOperation.of(
                merCreditType,
                operatedAmount,
                IncOrDecType.DECREASE,
                merchantCredit,
                transNo
        );
        List<MerchantAccountOperation> operations = new ArrayList<>();
        operations.add(operation);
        List<MerchantAccountOperation> moreOperations = nextOperator.decrease(merchantCredit, amountLeftToBeDecrease,transNo);
        operations.addAll(moreOperations);
        return operations;
    }

    @Override
    public List<MerchantAccountOperation> increase(MerchantCredit merchantCredit, BigDecimal amount, String transNo) {
        log.info("ready to increase merchantCredit.currentBalance for {}", amount.toString());
        merchantCredit.setCurrentBalance(merchantCredit.getCurrentBalance().add(amount));
        MerchantAccountOperation operation = MerchantAccountOperation.of(merCreditType, amount, IncOrDecType.INCREASE, merchantCredit, transNo);
        return Collections.singletonList(operation);
    }

    @Override
    public void afterPropertiesSet() {
        this.next(currentBalanceOperator);
    }
}
