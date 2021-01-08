package com.welfare.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.welfare.common.annotation.MerchantUser;
import com.welfare.common.constants.RedisKeyConstant;
import com.welfare.common.domain.MerchantUserInfo;
import com.welfare.common.exception.BusiException;
import com.welfare.common.exception.ExceptionCode;
import  com.welfare.persist.dao.AccountDepositApplyDao;
import com.welfare.persist.dao.AccountDepositApplyDetailDao;
import com.welfare.persist.entity.AccountDepositApply;
import com.welfare.persist.entity.AccountDepositApplyDetail;
import com.welfare.service.converter.AccountDepositApplyConverter;
import com.welfare.service.dto.AccountDepositApprovalRequest;
import com.welfare.service.dto.AccountDepositRequest;
import com.welfare.service.dto.DepositApplyRequest;
import com.welfare.service.dto.MerchantCreditApprovalReq;
import com.welfare.service.enums.ApprovalStatus;
import com.welfare.service.enums.ApprovalType;
import com.welfare.service.enums.RechargeStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.welfare.service.AccountDepositApplyService;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 账户充值申请服务接口实现
 *
 * @author Yuxiang Li
 * @since 2021-01-06 13:49:25
 * @description 由 Mybatisplus Code Generator 创建
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class AccountDepositApplyServiceImpl implements AccountDepositApplyService {

    @Autowired
    private AccountDepositApplyDao accountDepositApplyDao;

    @Autowired
    private AccountDepositApplyDetailDao accountDepositApplyDetailDao;

    @Autowired
    private AccountDepositApplyConverter depositApplyConverter;

    @Autowired
    private RedissonClient redissonClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(DepositApplyRequest request, MerchantUserInfo merchantUser) {

        AccountDepositApply apply = getByRequestId(request.getRequestId());
        if (apply != null) {
            return Long.valueOf(apply.getId());
        }
        String lockKey = RedisKeyConstant.buidKey(RedisKeyConstant.ACCOUNT_DEPOSIT_APPLY_SAVE_REQUEST_ID, request.getRequestId());
        RLock lock = redissonClient.getLock(lockKey);
        try {
            boolean locked = lock.tryLock(2, TimeUnit.SECONDS);
            if (locked) {
                apply = getByRequestId(request.getRequestId());
                if (apply != null) {
                    return Long.valueOf(apply.getId());
                }
                // 初始化主表
                apply = depositApplyConverter.toAccountDepositApply(request);
                apply.setRequestId(request.getRequestId());
                apply.setApplyUser(merchantUser.getUsername());
                apply.setApplyTime(new Date());
                apply.setMerCode(merchantUser.getMerchantCode());
                apply.setApplyCode(UUID.randomUUID().toString());
                apply.setRechargeNum(1);
                apply.setRechargeStatus(RechargeStatus.NO.getCode());
                apply.setCreateUser(merchantUser.getUserCode());
                apply.setUpdateUser(merchantUser.getUserCode());
                //apply.setVersion(0);
                apply.setApprovalStatus(ApprovalStatus.AUDITING.getCode());
                apply.setApprovalType(ApprovalType.SINGLE.getCode());
                // 初始化明细
                AccountDepositApplyDetail detail = depositApplyConverter.toAccountDepositApplyDetail(apply);
                detail.setAccountCode(request.getInfo().getAccountCode());
                detail.setRechargeAmount(request.getInfo().getRechargeAmount());
                accountDepositApplyDao.save(apply);
                accountDepositApplyDetailDao.save(detail);
                return Long.valueOf(apply.getId());
            }
        } catch (Exception e) {
            log.error("新增员工账号申请失败, 参数:{}, 商户:{}", JSON.toJSONString(request), JSON.toJSONString(merchantUser), e);
            throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "新增员工账号申请失败", e);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
        return null;
    }

    @Override
    public AccountDepositApply getByRequestId(String requestId) {
        QueryWrapper<AccountDepositApply> wrapper = new QueryWrapper<>();
        wrapper.eq(AccountDepositApply.REQUEST_ID, requestId);
        return accountDepositApplyDao.getOne(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long approval(AccountDepositApprovalRequest request) {
        AccountDepositApply apply = accountDepositApplyDao.getById(request.getId());
        if (apply == null) {
            throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "已经审批过了", null);
        }
        //已经审批过了
        if (!apply.getApprovalStatus().equals(ApprovalStatus.AUDITING.getCode())) {
            return Long.valueOf(apply.getId());
        }
        String lockKey = RedisKeyConstant.buidKey(RedisKeyConstant.ACCOUNT_DEPOSIT_APPLY_APPROVAL_REQUEST_ID, request.getId()+"");
        RLock lock = redissonClient.getLock(lockKey);
        try {
            boolean locked = lock.tryLock(2, TimeUnit.SECONDS);
            if (locked) {
                apply = accountDepositApplyDao.getById(request.getId());
                if (apply == null) {
                    throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, String.format("账号存款申请不存在[requestId:%s]", request.getId()), null);
                }
                //已经审批过了
                if (!apply.getApprovalStatus().equals(ApprovalStatus.AUDITING.getCode())) {
                    return Long.valueOf(apply.getId());
                }
                apply.setApprovalStatus(request.getApprovalStatus());
                apply.setApprovalRemark(request.getApplyRemark());
                apply.setApplyUser(request.getApprovalUser());
                apply.setApprovalOpinion(request.getApprovalOpinion());
                // 修改主表
                accountDepositApplyDao.save(apply);
                // 修改明细
                return Long.valueOf(apply.getId());
            }
        } catch (InterruptedException e) {
            log.error("审批员工账号申请失败, 参数:{}, 商户:{}", JSON.toJSONString(request), e);
            throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "审批员工账号申请失败", e);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
        return null;
    }
}