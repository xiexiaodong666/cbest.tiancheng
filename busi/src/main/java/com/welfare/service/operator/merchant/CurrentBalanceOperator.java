package com.welfare.service.operator.merchant;

import com.google.common.collect.Lists;
import com.welfare.common.constants.WelfareConstant.MerCreditType;
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
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email kobe663@gmail.com
 * @date 1/9/2021 10:42 PM
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class CurrentBalanceOperator extends AbstractMerAccountTypeOperator implements InitializingBean {
    private final MerCreditType merCreditType = MerCreditType.CURRENT_BALANCE;

    private final RemainingLimitOperator remainingLimitOperator;

    @Override
    public List<MerchantAccountOperation> decrease(MerchantCredit merchantCredit, BigDecimal amount, String transNo) {
        log.info("ready to decrease merchantCredit.currentBalance for {}", amount.toString());
        BigDecimal currentBalance = merchantCredit.getCurrentBalance();
        BigDecimal subtract = currentBalance.subtract(amount);
        if (subtract.compareTo(BigDecimal.ZERO) < 0) {
            BigDecimal amountLeftToBeDecrease = subtract.negate();
            List<MerchantAccountOperation> operations = doWhenNotEnough(merchantCredit, amountLeftToBeDecrease, transNo);
            return operations;
        } else {
            merchantCredit.setCurrentBalance(subtract);
            MerchantAccountOperation operation = MerchantAccountOperation.of(
                    merCreditType,
                    subtract,
                    IncOrDecType.DECREASE,
                    merchantCredit,
                    transNo
            );
            return Arrays.asList(operation);
        }

    }

    @Override
    protected List<MerchantAccountOperation> doWhenNotEnough(MerchantCredit merchantCredit, BigDecimal amountLeftToBeDecrease, String transNo) {
        AbstractMerAccountTypeOperator nextOperator = getNext();
        if (Objects.isNull(nextOperator)) {
            throw new BusiException(ExceptionCode.MERCHANT_RECHARGE_LIMIT_EXCEED, "余额不足", null);
        }
        merchantCredit.setCurrentBalance(BigDecimal.ZERO);
        MerchantAccountOperation operation = MerchantAccountOperation.of(
                merCreditType,
                merchantCredit.getCurrentBalance(),
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
        MerchantAccountOperation merchantAccountOperation = MerchantAccountOperation.of(merCreditType, amount, IncOrDecType.INCREASE, merchantCredit,transNo );
        return Lists.newArrayList(merchantAccountOperation);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.next(remainingLimitOperator);
    }
}
