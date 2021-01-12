package com.welfare.service.operator.merchant;

import com.welfare.common.exception.BusiException;
import com.welfare.common.exception.ExceptionCode;
import com.welfare.persist.entity.MerchantCredit;
import com.welfare.service.operator.merchant.domain.MerchantAccountOperation;

import java.math.BigDecimal;
import java.util.List;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email kobe663@gmail.com
 * @date 1/9/2021 10:36 PM
 */
public abstract class MerAccountTypeOperator {

    private MerAccountTypeOperator next;


    /**
     * 扣减
     * @param merchantCredit
     * @param amount
     * @param transNo
     * @return 实际操作金额
     */
    public List<MerchantAccountOperation> decrease(MerchantCredit merchantCredit, BigDecimal amount, String transNo){
        throw new RuntimeException("not supported method");
    }

    /**
     * 增加
     * @param merchantCredit
     * @param amount
     * @param transNo
     * @return 实际操作金额
     */
    public MerchantAccountOperation increase(MerchantCredit merchantCredit, BigDecimal amount, String transNo){
        throw new RuntimeException("not supported method");
    }

    /**
     * 下一个MerAccountTypeOperator，用于构建operator的链表
     * @param merAccountTypeOperator
     */
    public void putNext(MerAccountTypeOperator merAccountTypeOperator){
        this.next = merAccountTypeOperator;
    }

    protected MerAccountTypeOperator getNext(){
        return next;
    }

    /**
     * 默认抛出余额不足异常,子类可以自定义其他操作
     * @param merchantCredit
     * @param amountLeftToBeDecrease
     * @param transNo
     * @return
     */
    protected List<MerchantAccountOperation> doWhenNotEnough(MerchantCredit merchantCredit, BigDecimal amountLeftToBeDecrease, String transNo){
        throw new BusiException(ExceptionCode.MERCHANT_RECHARGE_LIMIT_EXCEED, "余额不足", null);
    }
}
