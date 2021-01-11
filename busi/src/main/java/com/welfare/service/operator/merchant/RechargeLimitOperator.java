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
 * @date 1/9/2021 10:39 PM
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class RechargeLimitOperator implements MerAccountTypeOperator {

    @Override
    public BigDecimal decrease(MerchantCredit merchantCredit, BigDecimal amount){
        log.info("ready to decrease merchantCredit.rechargeLimit for {}",amount.toString());
        BigDecimal currentRechargeLimit = merchantCredit.getRechargeLimit();
        BigDecimal subtract = currentRechargeLimit.subtract(amount);
        if(subtract.compareTo(BigDecimal.ZERO) < 0){
            throw new RuntimeException("商户充值余额不足以扣减,current:"+currentRechargeLimit.toString()+",to decrease:"+amount.toString());
        }else{
            merchantCredit.setRechargeLimit(subtract);
            return amount;
        }

    }
    @Override
    public BigDecimal increase(MerchantCredit merchantCredit, BigDecimal amount){
        log.info("ready to increase merchantCredit.rechargeLimit for {}",amount.toString());
        merchantCredit.setRechargeLimit(merchantCredit.getRechargeLimit().add(amount));
        return amount;
    }
}
