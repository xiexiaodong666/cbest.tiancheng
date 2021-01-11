package com.welfare.service.operator.merchant;

import com.welfare.persist.dao.MerchantCreditDao;
import com.welfare.persist.entity.MerchantCredit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

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
public class RemainingLimitOperator implements MerAccountTypeOperator {

    @Override
    public BigDecimal decrease(MerchantCredit merchantCredit, BigDecimal amount) {
        log.info("ready to decrease merchantCredit.currentRemainingLimit for {}", amount.toString());
        BigDecimal currentRemainingLimit = merchantCredit.getRemainingLimit();
        BigDecimal subtract = currentRemainingLimit.subtract(amount);
        if (subtract.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("剩余信用额度不足以扣减,current:" + currentRemainingLimit.toString() + ",to decrease:" + amount.toString());
         } else {
            merchantCredit.setRemainingLimit(subtract);
            return amount;
        }

    }

    @Override
    public BigDecimal increase(MerchantCredit merchantCredit, BigDecimal amount) {
        log.info("ready to increase merchantCredit.currentBalance for {}", amount.toString());
        merchantCredit.setRemainingLimit(merchantCredit.getRemainingLimit().add(amount));
        return amount;
    }
}
