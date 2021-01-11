package com.welfare.service.operator.merchant;

import com.welfare.common.constants.WelfareConstant.MerCreditType;
import com.welfare.persist.entity.MerchantCredit;
import com.welfare.service.enums.IncOrDecType;
import com.welfare.service.operator.merchant.domain.MerchantAccountOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

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
public class RemainingLimitOperator extends MerAccountTypeOperator {
    private MerCreditType merCreditType = MerCreditType.REMAINING_LIMIT;
    @Override
    public List<MerchantAccountOperation> decrease(MerchantCredit merchantCredit, BigDecimal amount) {
        log.info("ready to decrease merchantCredit.currentRemainingLimit for {}", amount.toString());
        BigDecimal currentRemainingLimit = merchantCredit.getRemainingLimit();
        BigDecimal subtract = currentRemainingLimit.subtract(amount);
        if (subtract.compareTo(BigDecimal.ZERO) < 0) {
            return doWhenNotEnough(merchantCredit,subtract.negate());
         } else {
            merchantCredit.setRemainingLimit(subtract);
            MerchantAccountOperation operation = MerchantAccountOperation.of(
                    merCreditType,
                    subtract,
                    IncOrDecType.DECREASE);
            return Arrays.asList(operation);
        }

    }

    @Override
    public MerchantAccountOperation increase(MerchantCredit merchantCredit, BigDecimal amount) {
        log.info("ready to increase merchantCredit.currentBalance for {}", amount.toString());
        merchantCredit.setRemainingLimit(merchantCredit.getRemainingLimit().add(amount));
        return MerchantAccountOperation.of(merCreditType,amount,IncOrDecType.INCREASE);
    }
}
