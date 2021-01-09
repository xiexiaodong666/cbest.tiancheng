package com.welfare.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.welfare.common.constants.WelfareConstant.MerCreditType;
import com.welfare.common.exception.BusiException;
import com.welfare.common.exception.ExceptionCode;
import com.welfare.common.util.SpringBeanUtils;
import  com.welfare.persist.dao.MerchantCreditDao;
import com.welfare.persist.entity.MerchantCredit;
import com.welfare.persist.mapper.MerchantCreditMapper;
import com.welfare.service.operator.merchant.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.welfare.service.MerchantCreditService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static com.welfare.common.constants.RedisKeyConstant.MER_ACCOUNT_TYPE_OPERATE;
import static com.welfare.common.constants.WelfareConstant.MerCreditType.*;

/**
 * 商户额度信服务接口实现
 *
 * @author Yuxiang Li
 * @since 2021-01-06 13:49:25
 * @description 由 Mybatisplus Code Generator 创建
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class MerchantCreditServiceImpl implements MerchantCreditService, InitializingBean {

    private final RedissonClient redissonClient;
    private final MerchantCreditMapper merchantCreditMapper;
    private final MerchantCreditDao merchantCreditDao;

    private final CreditLimitOperator creditLimitOperator;
    private final CurrentBalanceOperator currentBalanceOperator;
    private final RechargeLimitOperator rechargeLimitOperator;
    private final RemainingLimitOperator remainingLimitOperator;
    private final RebateLimitOperator rebateLimitOperator;

    private final Map<MerCreditType, MerAccountTypeOperator> operatorMap = new HashMap<>();


    @Override
    public int decreaseRechargeLimit(BigDecimal increaseLimit, Long id) {
        return merchantCreditMapper.decreaseRechargeLimit(increaseLimit, id);
    }

    @Override
    public MerchantCredit getByMerCode(String merCode) {
        QueryWrapper<MerchantCredit> query = new QueryWrapper<>();
        query.eq(MerchantCredit.MER_CODE, merCode);
        return  merchantCreditDao.getOne(query);
    }



    @Override
    @Transactional(rollbackFor = Exception.class)
    public void decreaseAccountType(String merCode, MerCreditType merCreditType, BigDecimal amount) {
        RLock lock = redissonClient.getFairLock(MER_ACCOUNT_TYPE_OPERATE + ":" + merCode);
        lock.lock();
        try{
            MerchantCredit merchantCredit = this.getByMerCode(merCode);
            MerAccountTypeOperator merAccountTypeOperator = operatorMap.get(merCreditType);
            BigDecimal operatedAmount = merAccountTypeOperator.decrease(merchantCredit, amount);
            if(operatedAmount.subtract(amount).compareTo(BigDecimal.ZERO) != 0){
                //todo 如果需要多个扣款通道累计，需要修改此处抛出的异常
                throw new RuntimeException("operated amount not equals to target amount.");
            }
            merchantCreditDao.updateById(merchantCredit);
        } finally {
            lock.unlock();
        }
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void increaseAccountType(String merCode, MerCreditType merCreditType, BigDecimal amount) {
        RLock lock = redissonClient.getFairLock(MER_ACCOUNT_TYPE_OPERATE + ":" + merCode);
        lock.lock();
        try{
            MerchantCredit merchantCredit = this.getByMerCode(merCode);
            MerAccountTypeOperator merAccountTypeOperator = operatorMap.get(merCreditType);
            merAccountTypeOperator.increase(merchantCredit, amount);
            merchantCreditDao.updateById(merchantCredit);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void afterPropertiesSet() {
        operatorMap.put(CREDIT_LIMIT, creditLimitOperator);
        operatorMap.put(CURRENT_BALANCE, currentBalanceOperator);
        operatorMap.put(RECHARGE_LIMIT, rechargeLimitOperator);
        operatorMap.put(REMAINING_LIMIT, remainingLimitOperator);
        operatorMap.put(REBATE_LIMIT, rebateLimitOperator);
    }
}