package com.welfare.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.welfare.common.constants.RedisKeyConstant;
import com.welfare.common.constants.WelfareConstant;
import com.welfare.common.domain.UserInfo;
import com.welfare.common.enums.MerCooperationModeEnum;
import com.welfare.common.enums.MerIdentityEnum;
import com.welfare.common.exception.BusiException;
import com.welfare.common.exception.ExceptionCode;
import com.welfare.common.util.DistributedLockUtil;
import com.welfare.persist.dao.MerchantCreditApplyDao;
import com.welfare.persist.dto.MerchantCreditApplyInfoDTO;
import com.welfare.persist.dto.query.MerchantCreditApplyQueryReq;
import com.welfare.persist.entity.MerDepositApplyFile;
import com.welfare.persist.entity.Merchant;
import com.welfare.persist.entity.MerchantCreditApply;
import com.welfare.service.*;
import com.welfare.service.converter.MerchantCreditApplyConverter;
import com.welfare.service.dto.merchantapply.*;
import com.welfare.service.enums.ApprovalStatus;
import com.welfare.service.utils.PageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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

    private final MerchantCreditApplyDao merchantCreditApplyDao;
    private final SequenceService sequenceService;
    private final MerchantCreditApplyConverter creditApplyConverter;
    private final RedissonClient redissonClient;
    private final MerchantCreditService merchantCreditService;
    private final MerchantService merchantService;
    private final MerDepositApplyFileService merDepositApplyFileService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String save(MerchantCreditApplyRequest request, UserInfo user) {
        MerchantCreditApply apply = getByRequestId(request.getRequestId());
        if (apply != null) {
            return String.valueOf(apply.getId());
        }
        String lockKey = RedisKeyConstant.buidKey(RedisKeyConstant.MERCHANT_CREDIT_APPLY_REQUEST_ID, request.getRequestId());
        RLock lock = redissonClient.getFairLock(lockKey);
        try {
            boolean locked = lock.tryLock(2, TimeUnit.SECONDS);
            if (locked) {
                apply = getByRequestId(request.getRequestId());
                if (apply != null) {
                    return apply.getId()+"";
                }
                validationParmas(request.getApplyType(), request.getMerCode());
                apply = creditApplyConverter.toApply(request);
                apply.setApprovalStatus(ApprovalStatus.AUDITING.getCode());
                apply.setApplyCode(sequenceService.nextFullNo(WelfareConstant.SequenceType.MERCHANT_CREDIT_APPLY.code()));
                apply.setApplyUser(user.getUserName());
                apply.setApplyTime(new Date());
                merchantCreditApplyDao.save(apply);
                merDepositApplyFileService.save(apply.getApplyCode(), request.getEnclosures());
                return String.valueOf(apply.getId());
            } else {
                throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "操作频繁稍后再试！", null);
            }
        } catch (Exception e) {
            throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, e.getMessage(), e);
        } finally {
            DistributedLockUtil.unlock(lock);
        }
    }

    @Override
    public MerchantCreditApply getByRequestId(String requestId) {
        QueryWrapper<MerchantCreditApply> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(MerchantCreditApply.REQUEST_ID, requestId);
        return merchantCreditApplyDao.getOne(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String update(MerchantCreditApplyUpdateReq request, UserInfo user) {
        MerchantCreditApply apply = merchantCreditApplyDao.getById(Long.parseLong(request.getId()));
        if (apply == null) {
            throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "商户额度申请不存在", null);
        }
        if (!apply.getApprovalStatus().equals(ApprovalStatus.AUDITING.getCode())) {
            throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "已经审批过了", null);
        }
        String lockKey = RedisKeyConstant.buidKey(RedisKeyConstant.MERCHANT_CREDIT_APPLY, request.getId()+"");
        RLock lock = redissonClient.getFairLock(lockKey);
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
                apply.setApplyUser(user.getUserName());
                merDepositApplyFileService.save(apply.getApplyCode(), request.getEnclosures());
                merchantCreditApplyDao.saveOrUpdate(apply);
                return String.valueOf(apply.getId());
            } else {
                throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "操作频繁稍后再试！", null);
            }
        } catch (Exception e) {
            throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, e.getMessage(), e);
        } finally {
            DistributedLockUtil.unlock(lock);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String approval(MerchantCreditApprovalReq request, UserInfo user) {
        MerchantCreditApply apply = merchantCreditApplyDao.getById(Long.parseLong(request.getId()));
        if (apply == null) {
            throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "商户额度申请不存在", null);
        }
        if (!apply.getApprovalStatus().equals(ApprovalStatus.AUDITING.getCode())) {
            throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "已经审批过了", null);
        }
        String lockKey = RedisKeyConstant.buidKey(RedisKeyConstant.MERCHANT_CREDIT_APPLY, request.getId()+"");
        RLock lock = redissonClient.getFairLock(lockKey);
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
                apply.setApprovalRemark(request.getApprovalRemark());
                apply.setApprovalUser(user.getUserName());
                apply.setApprovalStatus(request.getApprovalStatus().getCode());
                apply.setApprovalTime(new Date());
                merchantCreditApplyDao.saveOrUpdate(apply);
                if (request.getApprovalStatus().equals(ApprovalStatus.AUDIT_SUCCESS)) {
                    // 审批通过修改金额
                    WelfareConstant.MerCreditType type = WelfareConstant.MerCreditType.findByCode(apply.getApplyType());
                    operatorMerAccountByType(apply.getMerCode(),type,apply.getBalance(),apply.getApplyCode());
                }
                return String.valueOf(apply.getId());
            } else {
                throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "操作频繁稍后再试！", null);
            }
        } catch (Exception e) {
            throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, e.getMessage(), e);
        } finally {
            DistributedLockUtil.unlock(lock);
        }
    }

    @Override
    public MerchantCreditApplyInfo detail(Long id) {
        MerchantCreditApply apply = merchantCreditApplyDao.getById(id);
        if (apply == null) {
            throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "商户额度申请不存在！", null);
        }
        MerchantCreditApplyInfo info = creditApplyConverter.toInfo(apply);
        List<MerDepositApplyFile> fileUrls = merDepositApplyFileService.listByMerDepositApplyCode(apply.getApplyCode());
        info.setId(apply.getId()+"");
        Merchant merchant = merchantService.detailByMerCode(apply.getMerCode());
        info.setMerName(merchant.getMerName());
        List<String> list = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(fileUrls)) {
            list = fileUrls.stream().map(MerDepositApplyFile::getFileUrl).collect(Collectors.toList());
        }
        info.setEnclosures(list);
        return info;
    }

    @Override
    public Page<MerchantCreditApplyInfo> page(Integer current, Integer size, MerchantCreditApplyQueryReq query, UserInfo user) {
        Page<MerchantCreditApply> page = new Page<>();
        page.setCurrent(current);
        page.setSize(size);
        Page<MerchantCreditApplyInfoDTO> result = merchantCreditApplyDao.getBaseMapper().queryByPage(page, query);
        List<MerchantCreditApplyInfo> infos = new ArrayList<>();
        if (result != null && CollectionUtils.isNotEmpty(result.getRecords())) {
            List<MerchantCreditApplyInfoDTO> dtos = result.getRecords();
            dtos.forEach(dto -> {
                MerchantCreditApplyInfo info = new MerchantCreditApplyInfo();
                BeanUtils.copyProperties(dto,info);
                info.setId(dto.getId()+"");
                info.setMerCooperationMode(MerCooperationModeEnum.getByCode(info.getMerCooperationMode()).getDesc());
                info.setApplyType(WelfareConstant.MerCreditType.findByCode(info.getApplyType()).desc());
                info.setApprovalStatus(ApprovalStatus.getByCode(info.getApprovalStatus()).getValue());
                infos.add(info);
            });
        }
        return PageUtils.toPage(result, infos);
    }

    @Override
    public List<MerchantCreditApplyExcelInfo> list(MerchantCreditApplyQueryReq query, UserInfo user) {
        List<MerchantCreditApplyInfoDTO> result =  merchantCreditApplyDao.getBaseMapper().queryByPage(query);
        List<MerchantCreditApplyExcelInfo> infos = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(result)) {
            result.forEach(dto -> {
                MerchantCreditApplyExcelInfo info = new MerchantCreditApplyExcelInfo();
                BeanUtils.copyProperties(dto,info);
                info.setMerCooperationMode(MerCooperationModeEnum.getByCode(info.getMerCooperationMode()).getDesc());
                info.setApplyType(WelfareConstant.MerCreditType.findByCode(info.getApplyType()).desc());
                info.setApprovalStatus(ApprovalStatus.getByCode(info.getApprovalStatus()).getValue());
                info.setId(String.valueOf(dto.getId()));
                infos.add(info);
            });
        }
        return infos;
    }

    private void validationParmas(String typeStr, String merCode){
        Merchant merchant = merchantService.detailByMerCode(merCode);
        if (merchant == null) {
            throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "商户不存在！", null);
        }
        if (StringUtils.isBlank(merchant.getMerIdentity())) {
            throw new BusiException("商户没有设置属性！");
        }
        List<String > merIdentityList = Lists.newArrayList(merchant.getMerIdentity().split(","));
        if (!merIdentityList.contains(MerIdentityEnum.customer.getCode())) {
            throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "仅支持[客户]充值", null);
        }
    }

    /**
     * 通过不同申请额度类型操作对应的额度
     */
    private void operatorMerAccountByType(String merCode, WelfareConstant.MerCreditType merCreditType, BigDecimal amount, String transNo){
        if (merCreditType == WelfareConstant.MerCreditType.REBATE_LIMIT) {
            merchantCreditService.decreaseAccountType(merCode,merCreditType,amount,transNo, WelfareConstant.TransType.RESET_DECR.code());
        } else if (merCreditType == WelfareConstant.MerCreditType.CREDIT_LIMIT) {
            merchantCreditService.setAccountType(merCode,merCreditType,amount,transNo);
        } else {
            merchantCreditService.increaseAccountType(merCode,merCreditType,amount,transNo, WelfareConstant.TransType.RESET_INCR.code());
        }
    }
}