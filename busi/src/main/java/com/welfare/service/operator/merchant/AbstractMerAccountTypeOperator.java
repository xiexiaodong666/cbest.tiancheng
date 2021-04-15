package com.welfare.service.operator.merchant;

import com.welfare.common.exception.BizException;
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
public abstract class AbstractMerAccountTypeOperator {

    private AbstractMerAccountTypeOperator next;


    /**
     * 扣减
     * @param merchantCredit
     * @param amount
     * @param transNo
     * @param transType
     * @return 实际操作金额
     */
    public List<MerchantAccountOperation> decrease(MerchantCredit merchantCredit, BigDecimal amount, String transNo, String transType){
        throw new RuntimeException("not supported method");
    }

    /**
     * 增加
     * @param merchantCredit
     * @param amount
     * @param transNo
     * @param transType
     * @return 实际操作金额
     */
    public List<MerchantAccountOperation> increase(MerchantCredit merchantCredit, BigDecimal amount, String transNo, String transType){
        throw new RuntimeException("not supported method");
    }

    /**
     * 设置
     * @param merchantCredit
     * @param amount
     * @param transNo
     * @return 实际操作金额
     */
    public List<MerchantAccountOperation> set(MerchantCredit merchantCredit, BigDecimal amount, String transNo){
        throw new RuntimeException("not supported method");
    }

    /**
     * 下一个MerAccountTypeOperator，用于构建operator的链表
     * @param merAccountTypeOperator
     */
    public void next(AbstractMerAccountTypeOperator merAccountTypeOperator){
        this.next = merAccountTypeOperator;
    }

    protected AbstractMerAccountTypeOperator getNext(){
        return next;
    }

    /**
     * 默认抛出余额不足异常,子类可以自定义其他操作
     * @param merchantCredit
     * @param amountLeftToBeDecrease
     * @param operatedAmount
     * @param transNo
     * @param transType
     * @return
     */
    protected List<MerchantAccountOperation> doWhenNotEnough(MerchantCredit merchantCredit,
                                                             BigDecimal amountLeftToBeDecrease,
                                                             BigDecimal operatedAmount,
                                                             String transNo, String transType){
        throw new BizException(ExceptionCode.MERCHANT_RECHARGE_LIMIT_EXCEED, "组织(公司)余额不足", null);
    }
    /**
     * 默认抛出超过余额限度异常,子类可以自定义其他操作
     * @param merchantCredit
     * @param amountLeftToBeIncrease
     * @param transNo
     * @param transType
     * @return
     */
    protected List<MerchantAccountOperation> doWhenMoreThan(MerchantCredit merchantCredit, BigDecimal amountLeftToBeIncrease, String transNo, String transType){
        throw new BizException(ExceptionCode.MERCHANT_RECHARGE_LIMIT_EXCEED, "超过余额限度", null);
    }
}
