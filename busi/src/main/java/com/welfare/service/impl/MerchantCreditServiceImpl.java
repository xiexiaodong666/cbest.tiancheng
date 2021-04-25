package com.welfare.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.welfare.common.constants.WelfareConstant;
import com.welfare.common.constants.WelfareConstant.MerCreditType;
import com.welfare.common.exception.BizException;
import com.welfare.common.util.DistributedLockUtil;
import com.welfare.persist.dao.MerchantBillDetailDao;
import com.welfare.persist.dao.MerchantCreditDao;
import com.welfare.persist.entity.Merchant;
import com.welfare.persist.entity.MerchantBillDetail;
import com.welfare.persist.entity.MerchantCredit;
import com.welfare.service.MerchantBillDetailService;
import com.welfare.service.MerchantCreditService;
import com.welfare.service.MerchantService;
import com.welfare.service.dto.DecreaseMerchantCredit;
import com.welfare.service.dto.MerchantExtendDTO;
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
import java.util.*;
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
    private final WholesaleCreditLimitOperator wholesaleCreditLimitOperator;


    @Autowired
    private  MerchantService merchantService;
    private final Map<MerCreditType, AbstractMerAccountTypeOperator> operatorMap = new HashMap<>();
    private final MerchantBillDetailService merchantBillDetailService;
    @Autowired
    private MerchantCreditService creditService;
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
        RLock lock = DistributedLockUtil.lockFairly(MER_ACCOUNT_TYPE_OPERATE + ":" + merCode);
        try{
            MerchantCredit merchantCredit = this.getByMerCode(merCode);
            List<MerchantAccountOperation> operations = doOperateAccount(merchantCredit, amount, transNo, merAccountTypeOperator, transType);
            List<MerchantBillDetail> merchantBillDetails = operations.stream()
                    .map(MerchantAccountOperation::getMerchantBillDetail)
                    .collect(Collectors.toList());
            //上一句里面已经对merchantCredit进行了操作
            merchantCreditDao.getBaseMapper().decreaseRechargeLimit(merchantCredit.getRechargeLimit(), merchantCredit.getId());
            merchantBillDetailDao.saveBatch(merchantBillDetails);
            return operations;
        } finally {
            DistributedLockUtil.unlock(lock);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDecreaseLimit(List<DecreaseMerchantCredit> decreaseMerchantCredits) {
        if (CollectionUtils.isNotEmpty(decreaseMerchantCredits)) {
            List<RLock> locks = new ArrayList<>();
            try {
                List<MerchantAccountOperation> operations = new ArrayList<>();
                // 给所有商户加锁
                Set<String> merCodeSet = decreaseMerchantCredits.stream().map(DecreaseMerchantCredit::getMerCode)
                        .collect(Collectors.toSet());;
                for (String merCode : merCodeSet) {
                    RLock lock = DistributedLockUtil.lockFairly(MER_ACCOUNT_TYPE_OPERATE + ":" + merCode);
                    locks.add(lock);
                }
                List<String> merCodeList = decreaseMerchantCredits.stream().map(DecreaseMerchantCredit::getMerCode).collect(Collectors.toList());
                Map<String, MerchantCredit> merchantCreditMap = merchantCreditDao.mapByMerCodes(merCodeList);
                for (DecreaseMerchantCredit decreaseMerCredit : decreaseMerchantCredits) {
                    AbstractMerAccountTypeOperator merAccountTypeOperator = operatorMap.get(decreaseMerCredit.getMerCreditType());
                    MerchantCredit merchantCredit = merchantCreditMap.get(decreaseMerCredit.getMerCode());
                    if (Objects.isNull(merchantCredit)) {
                        merchantCredit = this.getByMerCode(decreaseMerCredit.getMerCode());
                        merchantCreditMap.put(decreaseMerCredit.getMerCode(), merchantCredit);
                    }
                    List<MerchantAccountOperation> accountOperations = doOperateAccount(
                            merchantCredit,
                            decreaseMerCredit.getAmount(),
                            decreaseMerCredit.getTransNo(),
                            merAccountTypeOperator,
                            decreaseMerCredit.getTransType());
                    operations.addAll(accountOperations);
                }
                // 批量更新金额
                merchantCreditDao.getBaseMapper().batchUpdateRechargeLimit(Lists.newArrayList(merchantCreditMap.values()));

                // 保存流水
                List<MerchantBillDetail> merchantBillDetails = operations.stream()
                        .map(MerchantAccountOperation::getMerchantBillDetail)
                        .collect(Collectors.toList());
                merchantBillDetailDao.saveBatch(merchantBillDetails);
            } finally {
                locks.forEach(DistributedLockUtil::unlock);
            }
        }
    }

    @Override
    public List<MerchantAccountOperation> doOperateAccount(MerchantCredit merchantCredit, BigDecimal amount, String transNo, AbstractMerAccountTypeOperator merAccountTypeOperator, String transType) {
        return merAccountTypeOperator.decrease(merchantCredit, amount, transNo, transType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<MerchantAccountOperation> increaseAccountType(String merCode, MerCreditType merCreditType, BigDecimal amount, String transNo, String transType) {
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
            return increase;
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
        Merchant merchant = merchantService.queryByCode(req.getMerCode());
        if (Objects.isNull(merchant)) {
            throw new BizException("商户不存在");
        }
        if (req.getAmount() == null) {
            throw new BizException("额度为空");
        }
        List<MerchantBillDetail> details = merchantBillDetailService.findByTransNoAndTransType(
                req.getTransNo(),MerCreditType.REMAINING_LIMIT.code());
        if (CollectionUtils.isNotEmpty(details)) {
            log.warn("该笔结算单已经确认过了,transNo:{}", req.getTransNo());
            return;
        }
        RLock lock = DistributedLockUtil.lockFairly(RESTORE_REMAINING_LIMIT_REQUEST_ID + ":" + req.getTransNo());
        try{
            details = merchantBillDetailService.findByTransNoAndTransType(
                    req.getTransNo(),
                    MerCreditType.REMAINING_LIMIT.code()
            );
            if (CollectionUtils.isNotEmpty(details)) {
                log.warn("该笔结算单已经确认过了,transNo:{}", req.getTransNo());
                return;
            }
            MerchantCredit merchantCredit = creditService.getByMerCode(req.getMerCode());
            if (merchantCredit.getRemainingLimit().add(req.getAmount()).compareTo(merchantCredit.getCreditLimit()) > 0) {
                throw new BizException(String.format("结算金额[%s]超过商户最大信用额度[%s],剩余额度[%s]",
                        req.getAmount(), merchantCredit.getCreditLimit(), merchantCredit.getRemainingLimit()));
            }
            // 恢复商户的信用额度
            increaseAccountType(req.getMerCode(),
                    MerCreditType.REMAINING_LIMIT,
                    req.getAmount(),
                    req.getTransNo(),
                    WelfareConstant.TransType.RESET_INCR.code());
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
        operatorMap.put(WHOLESALE_CREDIT_LIMIT, wholesaleCreditLimitOperator);

    }
}