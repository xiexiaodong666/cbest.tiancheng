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
 * @date 1/9/2021 10:42 PM
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class CurrentBalanceOperator implements MerAccountTypeOperator{

    @Override
    public BigDecimal decrease(MerchantCredit merchantCredit, BigDecimal amount){
        log.info("ready to decrease merchantCredit.currentBalance for {}",amount.toString());
        BigDecimal currentBalance = merchantCredit.getCurrentBalance();
        BigDecimal subtract = currentBalance.subtract(amount);
        if(subtract.compareTo(BigDecimal.ZERO) < 0){
            merchantCredit.setCurrentBalance(BigDecimal.ZERO);
            return currentBalance;
        }else{
            merchantCredit.setCurrentBalance(subtract);
            return amount;
        }

    }
    @Override
    public BigDecimal increase(MerchantCredit merchantCredit, BigDecimal amount){
        log.info("ready to increase merchantCredit.currentBalance for {}",amount.toString());
        merchantCredit.setCurrentBalance(merchantCredit.getCurrentBalance().add(amount));
        return amount;
    }
}
