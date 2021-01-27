package com.welfare.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.welfare.common.constants.WelfareConstant;
import com.welfare.common.constants.WelfareConstant.MerCreditType;
import com.welfare.common.exception.BusiException;
import com.welfare.common.util.DistributedLockUtil;
import com.welfare.persist.dao.MerchantBillDetailDao;
import com.welfare.persist.dao.MerchantCreditDao;
import com.welfare.persist.entity.MerchantBillDetail;
import com.welfare.persist.entity.MerchantCredit;
import com.welfare.service.MerchantBillDetailService;
import com.welfare.service.MerchantCreditService;
import com.welfare.service.MerchantService;
import com.welfare.service.dto.RestoreRemainingLimitReq;
import com.welfare.service.operator.merchant.*;
import com.welfare.service.operator.merchant.domain.MerchantAccountOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.welfare.common.constants.RedisKeyConstant.MER_ACCOUNT_TYPE_OPERATE;
import static com.welfare.common.constants.RedisKeyConstant.RESTORE_REMAINING_LIMIT_REQUEST_ID;
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
    private final MerchantCreditDao merchantCreditDao;

    private final CreditLimitOperator creditLimitOperator;
    private final CurrentBalanceOperator currentBalanceOperator;
    private final RechargeLimitOperator rechargeLimitOperator;
    private final RemainingLimitOperator remainingLimitOperator;
    private final RebateLimitOperator rebateLimitOperator;
    private final MerchantBillDetailDao merchantBillDetailDao;
    private final SelfDepositBalanceOperator selfDepositBalanceOperator;
    @Autowired
    private  MerchantService merchantService;
    private final Map<MerCreditType, AbstractMerAccountTypeOperator> operatorMap = new HashMap<>();
    private final MerchantBillDetailService merchantBillDetailService;

    @Override
    public boolean init(String merCode) {
        //初始化商户额度信
        MerchantCredit credit = new MerchantCredit();
        credit.setMerCode(merCode);
        credit.setRechargeLimit(BigDecimal.ZERO);
        credit.setCurrentBalance(BigDecimal.ZERO);
        credit.setCreditLimit(BigDecimal.ZERO);
        credit.setRemainingLimit(BigDecimal.ZERO);
        credit.setRebateLimit(BigDecimal.ZERO);
        credit.setSelfDepositBalance(BigDecimal.ZERO);
        return merchantCreditDao.save(credit);
    }

    @Override
    public MerchantCredit getByMerCode(String merCode) {
        QueryWrapper<MerchantCredit> query = new QueryWrapper<>();
        query.eq(MerchantCredit.MER_CODE, merCode);
        MerchantCredit credit = merchantCreditDao.getOne(query);
        if (credit == null) {
            credit = new MerchantCredit();
            credit.setMerCode(merCode);
            credit.setRechargeLimit(BigDecimal.ZERO);
            credit.setCurrentBalance(BigDecimal.ZERO);
            credit.setCreditLimit(BigDecimal.ZERO);
            credit.setRemainingLimit(BigDecimal.ZERO);
            credit.setRebateLimit(BigDecimal.ZERO);
            credit.setSelfDepositBalance(BigDecimal.ZERO);
            merchantCreditDao.save(credit);
        }
        return credit;
    }



    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<MerchantAccountOperation> decreaseAccountType(String merCode, MerCreditType merCreditType, BigDecimal amount, String transNo, String transType) {
        AbstractMerAccountTypeOperator merAccountTypeOperator = operatorMap.get(merCreditType);
        RLock lock = redissonClient.getFairLock(MER_ACCOUNT_TYPE_OPERATE + ":" + merCode);
        lock.lock();
        try{
            List<MerchantAccountOperation> operations = doOperateAccount(merCode, amount, transNo, merAccountTypeOperator, transType);
            List<MerchantBillDetail> merchantBillDetails = operations.stream()
                    .map(MerchantAccountOperation::getMerchantBillDetail)
                    .collect(Collectors.toList());
            merchantBillDetailDao.saveBatch(merchantBillDetails);
            return operations;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public List<MerchantAccountOperation> doOperateAccount(String merCode, BigDecimal amount, String transNo, AbstractMerAccountTypeOperator merAccountTypeOperator, String transType) {
        MerchantCredit merchantCredit = this.getByMerCode(merCode);
        List<MerchantAccountOperation> operations = merAccountTypeOperator.decrease(merchantCredit, amount, transNo, transType);
        merchantCreditDao.updateById(merchantCredit);
        return operations;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void increaseAccountType(String merCode, MerCreditType merCreditType, BigDecimal amount, String transNo, String transType) {
        RLock lock = redissonClient.getFairLock(MER_ACCOUNT_TYPE_OPERATE + ":" + merCode);
        lock.lock();
        try{
            MerchantCredit merchantCredit = this.getByMerCode(merCode);
            AbstractMerAccountTypeOperator merAccountTypeOperator = operatorMap.get(merCreditType);
            List<MerchantAccountOperation> increase = merAccountTypeOperator.increase(merchantCredit, amount,transNo, transType);
            merchantCreditDao.updateById(merchantCredit);
            List<MerchantBillDetail> merchantBillDetails = increase.stream()
                    .map(MerchantAccountOperation::getMerchantBillDetail)
                    .collect(Collectors.toList());
            merchantBillDetailDao.saveBatch(merchantBillDetails);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void setAccountType(String merCode, MerCreditType merCreditType, BigDecimal amount, String transNo) {
        RLock lock = redissonClient.getFairLock(MER_ACCOUNT_TYPE_OPERATE + ":" + merCode);
        lock.lock();
        try{
            MerchantCredit merchantCredit = this.getByMerCode(merCode);
            AbstractMerAccountTypeOperator merAccountTypeOperator = operatorMap.get(merCreditType);
            List<MerchantAccountOperation> increase = merAccountTypeOperator.set(merchantCredit, amount,transNo );
            merchantCreditDao.updateById(merchantCredit);
            List<MerchantBillDetail> merchantBillDetails = increase.stream()
                    .map(MerchantAccountOperation::getMerchantBillDetail)
                    .collect(Collectors.toList());
            merchantBillDetailDao.saveBatch(merchantBillDetails);
        } finally {
            lock.unlock();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void restoreRemainingLimit(RestoreRemainingLimitReq req) {
        if (merchantService.queryByCode(req.getMerCode())== null) {
            throw new BusiException("商户不存在");
        }
        if (req.getAmount() == null) {
            return;
        }
        List<MerchantBillDetail> details = merchantBillDetailService.findByTransNoAndTransType(req.getTransNo(),MerCreditType.REMAINING_LIMIT.code());
        if (CollectionUtils.isNotEmpty(details)) {
            return;
        }
        RLock lock = redissonClient.getFairLock(RESTORE_REMAINING_LIMIT_REQUEST_ID + ":" + req.getTransNo());
        lock.lock();
        try{
            details = merchantBillDetailService.findByTransNoAndTransType(req.getTransNo(),MerCreditType.REMAINING_LIMIT.code());
            if (CollectionUtils.isNotEmpty(details)) {
                return;
            }
            increaseAccountType(req.getMerCode(),MerCreditType.REMAINING_LIMIT,req.getAmount(),req.getTransNo(), WelfareConstant.TransType.RESET.code());
        } finally {
            DistributedLockUtil.unlock(lock);
        }
    }

    @Override
    public void afterPropertiesSet() {
        operatorMap.put(CREDIT_LIMIT, creditLimitOperator);
        operatorMap.put(CURRENT_BALANCE, currentBalanceOperator);
        operatorMap.put(RECHARGE_LIMIT, rechargeLimitOperator);
        operatorMap.put(REMAINING_LIMIT, remainingLimitOperator);
        operatorMap.put(REBATE_LIMIT, rebateLimitOperator);
        operatorMap.put(SELF_DEPOSIT,selfDepositBalanceOperator);
    }
}