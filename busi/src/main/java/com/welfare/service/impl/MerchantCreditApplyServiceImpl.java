package com.welfare.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.common.annotation.ApiUser;
import com.welfare.common.constants.RedisKeyConstant;
import com.welfare.common.constants.WelfareConstant;
import com.welfare.common.domain.ApiUserInfo;
import com.welfare.common.domain.MerchantUserInfo;
import com.welfare.common.enums.MerCooperationModeEnum;
import com.welfare.common.enums.MerIdentityEnum;
import com.welfare.common.exception.BusiException;
import com.welfare.common.exception.ExceptionCode;
import  com.welfare.persist.dao.MerchantCreditApplyDao;
import com.welfare.persist.entity.AccountDepositApply;
import com.welfare.persist.entity.Merchant;
import com.welfare.persist.entity.MerchantCredit;
import com.welfare.persist.entity.MerchantCreditApply;
import com.welfare.service.MerchantCreditService;
import com.welfare.service.MerchantService;
import com.welfare.service.converter.MerchantCreditApplyConverter;
import com.welfare.service.dto.*;
import com.welfare.service.enums.ApprovalStatus;
import com.welfare.service.helper.QueryHelper;
import com.welfare.service.utils.PageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.welfare.service.MerchantCreditApplyService;
import org.apache.commons.collections4.CollectionUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 商户金额申请服务接口实现
 *
 * @author Yuxiang Li
 * @since 2021-01-06 13:49:25
 * @description 由 Mybatisplus Code Generator 创建
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class MerchantCreditApplyServiceImpl implements MerchantCreditApplyService {

    @Autowired
    private MerchantCreditApplyDao merchantCreditApplyDao;

    @Autowired
    private MerchantCreditApplyConverter creditApplyConverter;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private MerchantCreditService merchantCreditService;

    @Autowired
    private MerchantService merchantService;

    @Override
    public Long save(MerchantCreditApplyRequest request, ApiUserInfo user) {
        MerchantCreditApply apply = getByRequestId(request.getRequestId());
        if (apply != null) {
            return apply.getId();
        }
        String lockKey = RedisKeyConstant.buidKey(RedisKeyConstant.MERCHANT_CREDIT_APPLY_REQUEST_ID, request.getRequestId());
        RLock lock = redissonClient.getLock(lockKey);
        try {
            boolean locked = lock.tryLock(2, TimeUnit.SECONDS);
            if (locked) {
                apply = getByRequestId(request.getRequestId());
                if (apply != null) {
                    return apply.getId();
                }
                validationParmas(request.getApplyType(), request.getMerCode());
                apply = creditApplyConverter.toApply(request);
                apply.setApprovalStatus(ApprovalStatus.AUDITING.getCode());
                apply.setApplyCode(UUID.randomUUID().toString());
                apply.setApplyUser(user.getUserName());
                apply.setApplyTime(new Date());
                merchantCreditApplyDao.save(apply);
                return apply.getId();
            } else {
                throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "操作频繁稍后再试！", null);
            }
        } catch (Exception e) {
            throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "新增商户额度申请失败", e);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    public void validationParmas(String typeStr, String merCode){
        Merchant merchant = merchantService.detailByMerCode(merCode);
        if (merchant == null) {
            throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "商户不存在！", null);
        }
        WelfareConstant.MerCreditType type = WelfareConstant.MerCreditType.findByCode(typeStr);
        if (!merchant.getMerIdentity().equals(MerIdentityEnum.customer.getCode())) {
            throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "仅支持[客户]充值", null);
        }
        if (merchant.getMerCooperationMode().equals(MerCooperationModeEnum.payFirt.getCode())) {
            if (WelfareConstant.MerCreditType.CREDIT_LIMIT == type
                    || WelfareConstant.MerCreditType.REMAINING_LIMIT == type) {
                throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, String.format("不能充值[%s]类型",type.desc()), null);
            }
        }
        if (merchant.getMerCooperationMode().equals(MerCooperationModeEnum.payed.getCode())) {
            if (WelfareConstant.MerCreditType.CURRENT_BALANCE == type) {
                throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, String.format("不能充值[%s]类型",type.desc()), null);
            }
        }
    }

    @Override
    public MerchantCreditApply getByRequestId(String requestId) {
        QueryWrapper<MerchantCreditApply> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(MerchantCreditApply.REQUEST_ID, requestId);
        return merchantCreditApplyDao.getOne(queryWrapper);
    }

    @Override
    public Long update(MerchantCreditApplyUpdateReq request, ApiUserInfo user) {
        MerchantCreditApply apply = merchantCreditApplyDao.getById(request.getId());
        if (apply == null) {
            throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "商户额度申请不存在", null);
        }
        if (!apply.getApprovalStatus().equals(ApprovalStatus.AUDITING.getCode())) {
            throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "已经审批过了", null);
        }
        String lockKey = RedisKeyConstant.buidKey(RedisKeyConstant.MERCHANT_CREDIT_APPLY, request.getId()+"");
        RLock lock = redissonClient.getLock(lockKey);
        try {
            boolean locked = lock.tryLock(2, TimeUnit.SECONDS);
            if (locked) {
                apply = merchantCreditApplyDao.getById(request.getId());
                if (apply == null) {
                    throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "商户额度申请不存在", null);
                }
                if (!apply.getApprovalStatus().equals(ApprovalStatus.AUDITING.getCode())) {
                    throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "已经审批过了", null);
                }
                validationParmas(request.getApplyType(), apply.getMerCode());
                apply.setApplyType(request.getApplyType());
                apply.setBalance(request.getBalance());
                apply.setRemark(request.getRemark());
                apply.setEnclosure(request.getEnclosure());
                apply.setApplyUser(user.getUserName());
                merchantCreditApplyDao.saveOrUpdate(apply);
                return apply.getId();
            } else {
                throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "操作频繁稍后再试！", null);
            }
        } catch (Exception e) {
            throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "修改商户额度申请失败", e);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long approval(MerchantCreditApprovalReq request, ApiUserInfo user) {
        MerchantCreditApply apply = merchantCreditApplyDao.getById(request.getId());
        if (apply == null) {
            throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "商户额度申请不存在", null);
        }
        if (!apply.getApprovalStatus().equals(ApprovalStatus.AUDITING.getCode())) {
            throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "已经审批过了", null);
        }
        String lockKey = RedisKeyConstant.buidKey(RedisKeyConstant.MERCHANT_CREDIT_APPLY, request.getId()+"");
        RLock lock = redissonClient.getLock(lockKey);
        try {
            boolean locked = lock.tryLock(2, TimeUnit.SECONDS);
            if (locked) {
                apply = merchantCreditApplyDao.getById(request.getId());
                if (apply == null) {
                    throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "商户额度申请不存在", null);
                }
                if (!apply.getApprovalStatus().equals(ApprovalStatus.AUDITING.getCode())) {
                    throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "已经审批过了", null);
                }
                validationParmas(apply.getApplyType(), apply.getMerCode());
                apply.setApprovalRemark(request.getApplyRemark());
                apply.setApplyUser(request.getApprovalUser());
                apply.setApprovalStatus(request.getApprovalStatus().getCode());
                merchantCreditApplyDao.saveOrUpdate(apply);
                if (request.getApprovalStatus().equals(ApprovalStatus.AUDIT_SUCCESS)) {
                    // 审批通过修改金额
//                    WelfareConstant.MerCreditType type =  WelfareConstant.MerCreditType.findByCode(apply.getApplyCode());
//                    merchantCreditService.increaseAccountType(apply.getMerCode(),type, apply.getBalance());
                }
                return apply.getId();
            } else {
                throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "操作频繁稍后再试！", null);
            }
        } catch (Exception e) {
            throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "审批商户额度申请失败", e);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    @Override
    public MerchantCreditApplyInfo detail(Long id) {
        MerchantCreditApply apply = merchantCreditApplyDao.getById(id);
        if (apply == null) {
            throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "商户额度申请不存在！", null);
        }
        MerchantCreditApplyInfo info = creditApplyConverter.toInfo(apply);
        Merchant merchant = merchantService.detailByMerCode(apply.getMerCode());
        info.setMerName(merchant.getMerName());
        info.setMerCooperationMode(merchant.getMerCooperationMode());
        return info;
    }

    @Override
    public Page<MerchantCreditApplyInfo> page(int current, int size, MerchantCreditApplyQuery query, ApiUserInfo user) {
        Page<MerchantCreditApply> page = new Page<>();
        page.setCurrent(current);
        page.setSize(size);
        Page<MerchantCreditApply> result = merchantCreditApplyDao.page(page, QueryHelper.getWrapper(query));
        List<MerchantCreditApplyInfo> infos = null;
        if (result != null && CollectionUtils.isNotEmpty(result.getRecords())) {
            List<MerchantCreditApply> applys = result.getRecords();
            infos = creditApplyConverter.toInfoList(applys);
            infos.forEach(info -> {
                Merchant merchant = merchantService.detailByMerCode(info.getMerCode());
                info.setMerName(merchant.getMerName());
                info.setMerCooperationMode(merchant.getMerCooperationMode());
            });
        }
        return PageUtils.toPage(result, infos);
    }

    @Override
    public List<MerchantCreditApplyInfo> list(MerchantCreditApplyQuery query, ApiUserInfo user) {
        List<MerchantCreditApply> result =  merchantCreditApplyDao.getBaseMapper().selectList(QueryHelper.getWrapper(query));
        List<MerchantCreditApplyInfo> infos = creditApplyConverter.toInfoList(result);
        if (CollectionUtils.isNotEmpty(infos)) {
            infos.forEach(info -> {
                Merchant merchant = merchantService.detailByMerCode(info.getMerCode());
                info.setApprovalStatus(ApprovalStatus.getByCode(info.getApprovalStatus()).getValue());
                info.setMerName(merchant.getMerName());
                info.setMerCooperationMode(merchant.getMerCooperationMode());
            });
        }
        return infos;
    }
}