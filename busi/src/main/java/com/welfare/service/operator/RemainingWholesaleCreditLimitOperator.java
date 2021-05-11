package com.welfare.service.operator;

import com.google.common.collect.Lists;
import com.welfare.common.constants.WelfareConstant;
import com.welfare.common.exception.BizException;
import com.welfare.common.exception.ExceptionCode;
import com.welfare.persist.entity.MerchantCredit;
import com.welfare.service.enums.IncOrDecType;
import com.welfare.service.operator.merchant.AbstractMerAccountTypeOperator;
import com.welfare.service.operator.merchant.CurrentBalanceOperator;
import com.welfare.service.operator.merchant.domain.MerchantAccountOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/4/25 10:13 上午
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class RemainingWholesaleCreditLimitOperator  extends AbstractMerAccountTypeOperator implements InitializingBean {
    private WelfareConstant.MerCreditType remainingWholesaleCreditType = WelfareConstant.MerCreditType.WHOLESALE_CREDIT;
    @Autowired
    private CurrentBalanceOperator currentBalanceOperator;
    @Override
    public List<MerchantAccountOperation> decrease(MerchantCredit merchantCredit, BigDecimal amount, String transNo, String transType) {
        log.info("ready to decrease merchantCredit.currentRemainingWholesaleCredit for {}", amount.toString());
        BigDecimal currentRemainingWholesaleCredit = merchantCredit.getWholesaleCredit();
        BigDecimal subtract = currentRemainingWholesaleCredit.subtract(amount);
        if (subtract.compareTo(BigDecimal.ZERO) < 0) {
            return doWhenNotEnough(merchantCredit,subtract.negate(),currentRemainingWholesaleCredit , transNo, transType);
        } else {
            merchantCredit.setWholesaleCredit(subtract);
            MerchantAccountOperation operation = MerchantAccountOperation.of(
                    remainingWholesaleCreditType,
                    amount,
                    IncOrDecType.DECREASE, merchantCredit,transNo, transType);
            return Collections.singletonList(operation);
        }
    }

    @Override
    public List<MerchantAccountOperation> increase(MerchantCredit merchantCredit, BigDecimal amount, String transNo, String transType) {
        log.info("ready to increase merchantCredit.currentRemainingWholesaleCredit for {}", amount.toString());
        BigDecimal wholesaleCreditLimit = merchantCredit.getWholesaleCreditLimit();
        BigDecimal remainingWholesaleCreditLimit = merchantCredit.getWholesaleCredit();
        BigDecimal add = amount.add(remainingWholesaleCreditLimit).subtract(wholesaleCreditLimit);
        if (add.compareTo(BigDecimal.ZERO) > 0) {
            // 超过批发信用额度
            return doWhenMoreThan(merchantCredit,add,transNo,transType);
        } else {
            merchantCredit.setWholesaleCredit(remainingWholesaleCreditLimit.add(amount));
            MerchantAccountOperation operation = MerchantAccountOperation.of(remainingWholesaleCreditType,amount,IncOrDecType.INCREASE, merchantCredit, transNo, transType);
            return Lists.newArrayList(operation);
        }
    }

    @Override
    protected List<MerchantAccountOperation> doWhenMoreThan(MerchantCredit merchantCredit, BigDecimal amountLeftToBeIncrease, String transNo, String transType) {
        AbstractMerAccountTypeOperator nextOperator = getNext();
        if (Objects.isNull(nextOperator)) {
            throw new BizException(ExceptionCode.MERCHANT_RECHARGE_LIMIT_EXCEED, "超过余额限度", null);
        }
        List<MerchantAccountOperation> operations = new ArrayList<>();
        BigDecimal wholesaleCreditLimit = merchantCredit.getWholesaleCreditLimit();
        BigDecimal remainingWholesaleCreditLimit = merchantCredit.getWholesaleCredit();
        // 加剩余批发信用额
        merchantCredit.setWholesaleCredit(wholesaleCreditLimit);
        MerchantAccountOperation operation = MerchantAccountOperation.of(
                remainingWholesaleCreditType,
                wholesaleCreditLimit.subtract(remainingWholesaleCreditLimit),
                IncOrDecType.INCREASE,
                merchantCredit,
                transNo,
                transType
        );
        if(operation.getAmount().compareTo(BigDecimal.ZERO) != 0){
            operations.add(operation);
        }
        // 加余额
        List<MerchantAccountOperation> moreOperations = nextOperator.increase(merchantCredit,amountLeftToBeIncrease,transNo, transType);
        operations.addAll(moreOperations);
        return operations;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.next(currentBalanceOperator);
    }
}
