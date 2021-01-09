package com.welfare.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.welfare.common.exception.BusiException;
import com.welfare.common.exception.ExceptionCode;
import  com.welfare.persist.dao.MerchantCreditDao;
import com.welfare.persist.entity.MerchantCredit;
import com.welfare.persist.mapper.MerchantCreditMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.welfare.service.MerchantCreditService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static com.welfare.common.constants.RedisKeyConstant.MER_ACCOUNT_TYPE_OPERATE;

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
public class MerchantCreditServiceImpl implements MerchantCreditService {

    private final RedissonClient redissonClient;
    private final MerchantCreditMapper merchantCreditMapper;
    private final MerchantCreditDao merchantCreditDao;

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
    public void updateMerchantRechargeCredit(String merCode, BigDecimal amount) {
        RLock lock = redissonClient.getFairLock(MER_ACCOUNT_TYPE_OPERATE + ":" + merCode);
        lock.lock();
        try{
            MerchantCredit merchantCredit = this.getByMerCode(merCode);
            BigDecimal rechargeLimit = merchantCredit.getRechargeLimit();
            if (rechargeLimit.subtract(amount).compareTo(BigDecimal.ZERO) < 0) {
                throw new BusiException(ExceptionCode.MERCHANT_RECHARGE_LIMIT_EXCEED, "充值额度不足", null);
            }
            merchantCredit.setRechargeLimit(merchantCredit.getRechargeLimit().subtract(amount));
            merchantCreditDao.updateById(merchantCredit);
        } finally {
            lock.unlock();
        }

    }
}