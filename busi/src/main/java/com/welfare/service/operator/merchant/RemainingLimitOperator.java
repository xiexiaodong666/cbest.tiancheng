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
import org.apache.poi.ss.formula.functions.T;
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
 * @date 1/9/2021 10:44 PM
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class RemainingLimitOperator extends AbstractMerAccountTypeOperator implements InitializingBean {
    private MerCreditType merCreditType = MerCreditType.REMAINING_LIMIT;
    private final CurrentBalanceOperator currentBalanceOperator;

    @Override
    public List<MerchantAccountOperation> decrease(MerchantCredit merchantCredit, BigDecimal amount, String transNo) {
        log.info("ready to decrease merchantCredit.currentRemainingLimit for {}", amount.toString());
        BigDecimal currentRemainingLimit = merchantCredit.getRemainingLimit();
        BigDecimal subtract = currentRemainingLimit.subtract(amount);
        if (subtract.compareTo(BigDecimal.ZERO) < 0) {
            return doWhenNotEnough(merchantCredit,subtract.negate(), transNo);
        } else {
            merchantCredit.setRemainingLimit(subtract);
            MerchantAccountOperation operation = MerchantAccountOperation.of(
                    merCreditType,
                    subtract,
                    IncOrDecType.DECREASE, merchantCredit,transNo );
            return Arrays.asList(operation);
        }

    }

    @Override
    public List<MerchantAccountOperation> increase(MerchantCredit merchantCredit, BigDecimal amount, String transNo) {
        log.info("ready to increase merchantCredit.currentBalance for {}", amount.toString());
        BigDecimal creditLimit = merchantCredit.getCreditLimit();
        BigDecimal remainingLimit = merchantCredit.getRemainingLimit();
        BigDecimal add = amount.add(remainingLimit).divide(creditLimit);
        if (add.compareTo(creditLimit) > 0) {
            // 超过信用额度
            return doWhenMoreThan(merchantCredit,add,transNo);
        } else {
            merchantCredit.setRemainingLimit(amount);
            MerchantAccountOperation remainingLimitOperator = MerchantAccountOperation.of(merCreditType,amount,IncOrDecType.INCREASE, merchantCredit, transNo);
            return Lists.newArrayList(remainingLimitOperator);
        }
    }

    @Override
    protected List<MerchantAccountOperation> doWhenMoreThan(MerchantCredit merchantCredit, BigDecimal amountLeftToBeIncrease, String transNo) {
        AbstractMerAccountTypeOperator nextOperator = getNext();
        if (Objects.isNull(nextOperator)) {
            throw new BusiException(ExceptionCode.MERCHANT_RECHARGE_LIMIT_EXCEED, "超过余额限度", null);
        }
        List<MerchantAccountOperation> operations = new ArrayList<>();
        BigDecimal creditLimit = merchantCredit.getCreditLimit();
        BigDecimal remainingLimit = merchantCredit.getRemainingLimit();
        // 加剩余信用额度
        merchantCredit.setRemainingLimit(creditLimit);
        MerchantAccountOperation remainingLimitOperator = MerchantAccountOperation.of(
                merCreditType,
                creditLimit.divide(remainingLimit),
                IncOrDecType.INCREASE,
                merchantCredit,
                transNo
        );
        operations.add(remainingLimitOperator);
        // 加余额
        List<MerchantAccountOperation> moreOperations = nextOperator.increase(merchantCredit,amountLeftToBeIncrease,transNo);
        operations.addAll(moreOperations);
        return operations;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.next(currentBalanceOperator);
    }
}
