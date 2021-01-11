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
public class RebateLimitOperator implements MerAccountTypeOperator{
    private final MerchantCreditDao merchantCreditDao;

    @Override
    public BigDecimal decrease(MerchantCredit merchantCredit, BigDecimal amount){
        log.info("ready to decrease merchantCredit.rebateLimit for {}",amount.toString());
        BigDecimal currentBalance = merchantCredit.getRechargeLimit();
        BigDecimal subtract = currentBalance.subtract(amount);
        if(subtract.compareTo(BigDecimal.ZERO) < 0){
            throw new RuntimeException("返利余额不足以扣减,current:"+currentBalance.toString()+",to decrease:"+amount.toString());
        }else{
            merchantCredit.setRebateLimit(subtract);
            return amount;
        }

    }
    @Override
    public BigDecimal increase(MerchantCredit merchantCredit, BigDecimal amount){
        log.info("ready to increase merchantCredit.rebateLimit for {}",amount.toString());
        merchantCredit.setRebateLimit(merchantCredit.getRechargeLimit().add(amount));
        return amount;
    }
}
