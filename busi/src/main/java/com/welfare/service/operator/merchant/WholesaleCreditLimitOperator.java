package com.welfare.service.operator.merchant;

import com.welfare.common.constants.WelfareConstant;
import com.welfare.common.exception.BizAssert;
import com.welfare.common.exception.ExceptionCode;
import com.welfare.persist.entity.MerchantCredit;
import com.welfare.service.enums.IncOrDecType;
import com.welfare.service.operator.RemainingWholesaleCreditLimitOperator;
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
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/4/25 10:11 上午
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class WholesaleCreditLimitOperator extends AbstractMerAccountTypeOperator implements InitializingBean {

    private final WelfareConstant.MerCreditType wholesaleCreditType = WelfareConstant.MerCreditType.WHOLESALE_CREDIT_LIMIT;

    @Autowired
    private RemainingWholesaleCreditLimitOperator remainingWholesaleCreditLimitOperator;
    @Override
    public List<MerchantAccountOperation> decrease(MerchantCredit merchantCredit, BigDecimal amount, String transNo, String transType) {
        log.info("ready to decrease merchantCredit.wholesaleCreditLimit for {}", amount.toString());
        return doWhenNotEnough(merchantCredit, amount, merchantCredit.getCreditLimit(), transNo,transType );
    }

    @Override
    public List<MerchantAccountOperation> increase(MerchantCredit merchantCredit, BigDecimal amount, String transNo, String transType) {
        log.info("ready to increase merchantCredit.wholesaleCreditLimit for {}", amount.toString());
        return doWhenMoreThan(merchantCredit, amount, transNo, transType);
    }

    @Override
    protected List<MerchantAccountOperation> doWhenNotEnough(MerchantCredit merchantCredit, BigDecimal amountLeftToBeDecrease, BigDecimal operatedAmount, String transNo, String transType) {
        List<MerchantAccountOperation> operations = new ArrayList<>();
        BigDecimal oldWholesaleCreditLimit = merchantCredit.getWholesaleCreditLimit();
        BigDecimal oldRemainingWholesaleCredit = merchantCredit.getWholesaleCredit();

        merchantCredit.setCreditLimit(oldWholesaleCreditLimit.subtract(amountLeftToBeDecrease));
        MerchantAccountOperation wholesaleCreditLimitLimitOperator = MerchantAccountOperation.of(
                wholesaleCreditType,
                amountLeftToBeDecrease,
                IncOrDecType.DECREASE,
                merchantCredit,
                transNo,
                transType
        );
        operations.add(wholesaleCreditLimitLimitOperator);
        // 减批发剩余授信额度，不够减就为负数
        merchantCredit.setRemainingLimit(oldRemainingWholesaleCredit.subtract(amountLeftToBeDecrease));
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
        BigDecimal wholesaleCreditLimit = merchantCredit.getWholesaleCreditLimit();
        // 加批发信用额度
        merchantCredit.setWholesaleCreditLimit(wholesaleCreditLimit.add(amountLeftToBeIncrease));
        MerchantAccountOperation wholesaleCreditLimitOperator = MerchantAccountOperation.of(
                wholesaleCreditType,
                amountLeftToBeIncrease,
                IncOrDecType.INCREASE,
                merchantCredit,
                transNo,
                transType
        );
        operations.add(wholesaleCreditLimitOperator);
        // 加剩余批发信用额度
        List<MerchantAccountOperation> moreOperations = nextOperator.increase(merchantCredit,amountLeftToBeIncrease,transNo,transType );
        operations.addAll(moreOperations);
        return operations;
    }

    @Override
    public List<MerchantAccountOperation> set(MerchantCredit merchantCredit, BigDecimal amount, String transNo) {
        BigDecimal wholesaleCreditLimit = merchantCredit.getWholesaleCreditLimit();
        BigDecimal remainingWholesaleCreditLimit = merchantCredit.getWholesaleCredit();
        if (amount.compareTo(wholesaleCreditLimit) >= 0 ) {
            return increase(merchantCredit,amount.subtract(wholesaleCreditLimit),transNo, WelfareConstant.TransType.DEPOSIT_INCR.code());
        } else {
            BizAssert.isTrue(amount.compareTo(remainingWholesaleCreditLimit) < 0,
                    ExceptionCode.WHOLESALE_CREDIT_LIMIT_LESS_THAN_REMAINING_WHOLESALE_CREDIT);
            return decrease(merchantCredit,wholesaleCreditLimit.subtract(amount),transNo, WelfareConstant.TransType.DEPOSIT_DECR.code());
        }
    }

    @Override
    public void afterPropertiesSet() {
        this.next(remainingWholesaleCreditLimitOperator);
    }
}
