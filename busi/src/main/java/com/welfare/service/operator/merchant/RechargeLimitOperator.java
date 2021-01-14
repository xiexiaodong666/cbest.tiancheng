package com.welfare.service.operator.merchant;

import com.google.common.collect.Lists;
import com.welfare.common.constants.WelfareConstant;
import com.welfare.persist.entity.MerchantCredit;
import com.welfare.service.enums.IncOrDecType;
import com.welfare.service.operator.merchant.domain.MerchantAccountOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
public class RechargeLimitOperator extends AbstractMerAccountTypeOperator {
    private WelfareConstant.MerCreditType operateType = WelfareConstant.MerCreditType.RECHARGE_LIMIT;
    @Override
    public List<MerchantAccountOperation> decrease(MerchantCredit merchantCredit, BigDecimal amount, String transNo){
        log.info("ready to decrease merchantCredit.rechargeLimit for {}",amount.toString());
        BigDecimal currentRechargeLimit = merchantCredit.getRechargeLimit();
        BigDecimal subtract = currentRechargeLimit.subtract(amount);
        if(subtract.compareTo(BigDecimal.ZERO) < 0){
            return doWhenNotEnough(merchantCredit,subtract.negate(), transNo);
        }else{
            merchantCredit.setRechargeLimit(subtract);
            MerchantAccountOperation operation = MerchantAccountOperation.of(operateType, amount, IncOrDecType.DECREASE, merchantCredit, transNo);
            return Collections.singletonList(operation);
        }

    }
    @Override
    public List<MerchantAccountOperation> increase(MerchantCredit merchantCredit, BigDecimal amount, String transNo){
        log.info("ready to increase merchantCredit.rechargeLimit for {}",amount.toString());
        merchantCredit.setRechargeLimit(merchantCredit.getRechargeLimit().add(amount));
        MerchantAccountOperation operation = MerchantAccountOperation.of(operateType,amount,IncOrDecType.INCREASE, merchantCredit, transNo);
        return Lists.newArrayList(operation);
    }
}
