package com.welfare.service.operator.merchant;

import com.google.common.collect.Lists;
import com.welfare.common.constants.WelfareConstant.MerCreditType;
import com.welfare.persist.dao.MerchantCreditDao;
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
public class RebateLimitOperator extends AbstractMerAccountTypeOperator {
    private MerCreditType merCreditType = MerCreditType.REBATE_LIMIT;
    private final MerchantCreditDao merchantCreditDao;

    @Override
    public List<MerchantAccountOperation> decrease(MerchantCredit merchantCredit, BigDecimal amount, String transNo){
        log.info("ready to decrease merchantCredit.rebateLimit for {}",amount.toString());
        BigDecimal currentBalance = merchantCredit.getRechargeLimit();
        BigDecimal subtract = currentBalance.subtract(amount);
        if(subtract.compareTo(BigDecimal.ZERO) < 0){
            return doWhenNotEnough(merchantCredit,subtract.negate(), transNo);
        }else{
            merchantCredit.setRebateLimit(subtract);
            MerchantAccountOperation operation = MerchantAccountOperation.of(
                    merCreditType,
                    amount,
                    IncOrDecType.DECREASE,
                    merchantCredit,
                    transNo);
            return Arrays.asList(operation);
        }

    }
    @Override
    public List<MerchantAccountOperation> increase(MerchantCredit merchantCredit, BigDecimal amount, String transNo){
        log.info("ready to increase merchantCredit.rebateLimit for {}",amount.toString());
        merchantCredit.setRebateLimit(merchantCredit.getRebateLimit().add(amount));
        MerchantAccountOperation operation = MerchantAccountOperation.of(merCreditType,amount,IncOrDecType.INCREASE,merchantCredit,transNo );
        return Lists.newArrayList(operation);
    }
}
