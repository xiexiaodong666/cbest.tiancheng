package com.welfare.service.operator.merchant;

import com.welfare.persist.entity.MerchantCredit;

import java.math.BigDecimal;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email kobe663@gmail.com
 * @date 1/9/2021 10:36 PM
 */
public interface MerAccountTypeOperator {

    /**
     * 扣减
     * @param merchantCredit
     * @param amount
     * @return 实际操作金额
     */
    default BigDecimal decrease(MerchantCredit merchantCredit, BigDecimal amount){
        throw new RuntimeException("not supported method");
    }

    /**
     * 增加
     * @param merchantCredit
     * @param amount
     * @return 实际操作金额
     */
    default BigDecimal increase(MerchantCredit merchantCredit, BigDecimal amount){
        throw new RuntimeException("not supported method");
    }
}
