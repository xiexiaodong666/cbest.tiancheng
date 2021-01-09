package com.welfare.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.welfare.common.constants.RedisKeyConstant;
import com.welfare.common.domain.MerchantUserInfo;
import com.welfare.common.exception.BusiException;
import com.welfare.common.exception.ExceptionCode;
import  com.welfare.persist.dao.AccountDepositApplyDao;
import com.welfare.persist.dao.AccountDepositApplyDetailDao;
import com.welfare.persist.entity.*;
import com.welfare.service.*;
import com.welfare.service.converter.AccountDepositApplyConverter;
import com.welfare.service.converter.DepositApplyDetailConverter;
import com.welfare.service.dto.*;
import com.welfare.service.enums.ApprovalStatus;
import com.welfare.service.enums.ApprovalType;
import com.welfare.service.enums.RechargeStatus;
import com.welfare.service.helper.QueryHelper;
import com.welfare.service.utils.PageUtils;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.jcp.xml.dsig.internal.dom.DOMUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

    @Autowired
    private AccountDepositApplyDetailService depositApplyDetailService;

    @Autowired
    private MerchantCreditService merchantCreditService;

    @Autowired
    private AccountAmountTypeService accountAmountTypeService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private DepositApplyDetailConverter depositApplyDetailConverter;

    @Autowired
    private TempAccountDepositApplyService tempAccountDepositApplyService;

    @Autowired
    private DepartmentService departmentService;

    @Override
    public Long saveOne(DepositApplyRequest request, MerchantUserInfo merchantUser) {

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
                // 判断金额是否超限
                MerchantCredit merchantCredit = merchantCreditService.getByMerCode(merchantUser.getMerchantCode());
                if (merchantCredit == null) {
                    throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "商户额度不存在", null);
                }
                if (merchantCredit.getRechargeLimit().compareTo(request.getInfo().getRechargeAmount()) < 0) {
                    throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "商户充值额度不足！", null);
                }
                // 初始化主表
                apply = depositApplyConverter.toAccountDepositApply(request);
                initAccountDepositApply(apply, merchantUser);
                // 设置充值人数
                apply.setRechargeNum(1);
                // 设置充值总金额
                apply.setRechargeAmount(request.getInfo().getRechargeAmount());
                apply.setApprovalType(ApprovalType.SINGLE.getCode());
                // 初始化明细
                AccountDepositApplyDetail detail = assemblyAccountDepositApplyDetailList(apply, request.getInfo());
                accountDepositApplyDao.save(apply);
                accountDepositApplyDetailDao.save(detail);
                return Long.valueOf(apply.getId());
            } else {
                throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "操作频繁稍后再试！", null);
            }
        } catch (Exception e) {
            log.error("新增员工账号申请失败, 参数:{}, 商户:{}", JSON.toJSONString(request), JSON.toJSONString(merchantUser), e);
            throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "新增员工账号申请失败", e);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    @Override
    public Long saveBatch(DepositApplyRequest request, String fileId, MerchantUserInfo merchantUser) {

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
                initAccountDepositApply(apply, merchantUser);
                List<TempAccountDepositApply> deposits = tempAccountDepositApplyService.getAllByFileId(fileId);
                if (CollectionUtils.isEmpty(deposits)) {
                    throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "至少选一个员工！", null);
                }
                double sumAmoun = deposits.stream().mapToDouble(value -> value.getRechargeAmount().doubleValue()).sum();
                BigDecimal sumAmount = new BigDecimal(sumAmoun);
                // 判断金额是否超限
                MerchantCredit merchantCredit = merchantCreditService.getByMerCode(merchantUser.getMerchantCode());
                if (merchantCredit == null) {
                    throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "商户额度不存在", null);
                }
                if (merchantCredit.getRechargeLimit().compareTo(sumAmount) < 0) {
                    throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "商户充值额度不足！", null);
                }
                // 设置充值总金额
                apply.setRechargeAmount(sumAmount);
                // 设置充值人数
                apply.setApprovalType(ApprovalType.BATCH.getCode());
                // 初始化明细
                List<AccountDepositApplyDetail> details = assemblyAccountDepositApplyDetailList(apply, deposits);
                accountDepositApplyDao.save(apply);
                accountDepositApplyDetailDao.saveBatch(details);
                //删除临时文件记录表相关数据
                tempAccountDepositApplyService.delByFileId(fileId);
                return Long.valueOf(apply.getId());
            } else {
                throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "操作频繁稍后再试！", null);
            }
        } catch (Exception e) {
            log.error("批量员工账号申请失败, 参数:{}, 商户:{}", JSON.toJSONString(request), JSON.toJSONString(merchantUser), e);
            throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "批量员工账号申请失败", e);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    @Override
    public Long updateOne(DepositApplyUpdateRequest request, MerchantUserInfo merchantUserInfo) {
        AccountDepositApply apply = accountDepositApplyDao.getById(request.getId());
        if (apply == null) {
            throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "申请不存在", null);
        }
        //已经审批过了
        if (!apply.getApprovalStatus().equals(ApprovalStatus.AUDITING.getCode())) {
            throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "已经审批过了", null);
        }
        String lockKey = RedisKeyConstant.buidKey(RedisKeyConstant.ACCOUNT_DEPOSIT_APPLY__ID, request.getId()+"");
        RLock lock = redissonClient.getLock(lockKey);
        try {
            boolean locked = lock.tryLock(4, TimeUnit.SECONDS);
            if (locked) {
                apply = accountDepositApplyDao.getById(request.getId());
                if (apply == null) {
                    throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "申请不存在", null);
                }
                //已经审批过了
                if (!apply.getApprovalStatus().equals(ApprovalStatus.AUDITING.getCode())) {
                    throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "已经审批过了", null);
                }
                // 判断金额是否超限
                MerchantCredit merchantCredit = merchantCreditService.getByMerCode(merchantUserInfo.getMerchantCode());
                if (merchantCredit == null) {
                    throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "商户额度不存在", null);
                }
                // 修改充值明细表
                if (merchantCredit.getRechargeLimit().compareTo(request.getInfo().getRechargeAmount()) < 0) {
                    throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "商户充值额度不足！", null);
                }
                Date now = new Date();
                apply.setUpdateTime(now);
                apply.setUpdateUser(merchantUserInfo.getUserCode());
                apply.setRechargeAmount(request.getInfo().getRechargeAmount());
                apply.setApplyRemark(request.getApplyRemark());
                apply.setMerAccountTypeCode(request.getMerAccountTypeCode());
                accountDepositApplyDao.save(apply);
                List<AccountDepositApplyDetail> details = depositApplyDetailService.listByApplyCode(apply.getApplyCode());
                if (CollectionUtils.isNotEmpty(details)) {
                    AccountDepositApplyDetail detail = details.get(0);
                    detail.setRechargeAmount(request.getInfo().getRechargeAmount());
                    detail.setUpdateUser(merchantUserInfo.getUserCode());
                    detail.setUpdateTime(new Date());
                    accountDepositApplyDetailDao.save(detail);
                }
                return Long.valueOf(apply.getId());
            } else {
                throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "操作频繁稍后再试！", null);
            }
        } catch (Exception e) {
            log.error("修改员工账号申请失败, 参数:{}, 商户:{}", JSON.toJSONString(request), e);
            throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "修改员工账号申请失败", e);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    @Override
    public Long updateBatch(DepositApplyUpdateRequest request, String fileId, MerchantUserInfo merchantUserInfo) {
        AccountDepositApply apply = accountDepositApplyDao.getById(request.getId());
        if (apply == null) {
            throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "申请不存在", null);
        }
        //已经审批过了
        if (!apply.getApprovalStatus().equals(ApprovalStatus.AUDITING.getCode())) {
            throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "已经审批过了", null);
        }
        String lockKey = RedisKeyConstant.buidKey(RedisKeyConstant.ACCOUNT_DEPOSIT_APPLY__ID, request.getId()+"");
        RLock lock = redissonClient.getLock(lockKey);
        try {
            boolean locked = lock.tryLock(4, TimeUnit.SECONDS);
            if (locked) {
                apply = accountDepositApplyDao.getById(request.getId());
                if (apply == null) {
                    throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "申请不存在", null);
                }
                //已经审批过了
                if (!apply.getApprovalStatus().equals(ApprovalStatus.AUDITING.getCode())) {
                    throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "已经审批过了", null);
                }

                List<TempAccountDepositApply> temps = tempAccountDepositApplyService.getAllByFileId(fileId);
                // 至少选一个员工
                if (CollectionUtils.isEmpty(temps)) {
                    throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "至少上传一个员工", null);
                }
                // 判断金额是否超限
                double sumAmoun = temps.stream().mapToDouble(value -> value.getRechargeAmount().doubleValue()).sum();
                BigDecimal sumAmount = new BigDecimal(sumAmoun);
                MerchantCredit merchantCredit = merchantCreditService.getByMerCode(merchantUserInfo.getMerchantCode());
                if (merchantCredit == null) {
                    throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "商户额度不存在", null);
                }
                // 修改充值明细表
                if (merchantCredit.getRechargeLimit().compareTo(sumAmount) < 0) {
                    throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "商户充值额度不足！", null);
                }
                Date now = new Date();
                apply.setUpdateTime(now);
                apply.setUpdateUser(merchantUserInfo.getUserCode());
                apply.setRechargeAmount(sumAmount);
                apply.setRechargeNum(temps.size());
                apply.setApplyRemark(request.getApplyRemark());
                apply.setMerAccountTypeCode(request.getMerAccountTypeCode());
                accountDepositApplyDao.save(apply);
                depositApplyDetailService.delByApplyCode(apply.getApplyCode());
                List<AccountDepositApplyDetail> details = assemblyAccountDepositApplyDetailList(apply, temps);
                accountDepositApplyDetailDao.saveBatch(details);
                return Long.valueOf(apply.getId());
            } else {
                throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "操作频繁稍后再试！", null);
            }
        } catch (Exception e) {
            log.error("批量修改员工账号申请失败, 参数:{}, 商户:{}", JSON.toJSONString(request), e);
            throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "批量修改员工账号申请失败", e);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
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
            throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "申请不存在", null);
        }
        //已经审批过了
        if (!apply.getApprovalStatus().equals(ApprovalStatus.AUDITING.getCode())) {
            return Long.valueOf(apply.getId());
        }
        String lockKey = RedisKeyConstant.buidKey(RedisKeyConstant.ACCOUNT_DEPOSIT_APPLY__ID, request.getId()+"");
        RLock lock = redissonClient.getLock(lockKey);
        try {
            boolean locked = lock.tryLock(4, TimeUnit.SECONDS);
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
                apply.setApprovalUser(request.getApprovalUser());
                apply.setApprovalOpinion(request.getApprovalOpinion());
                // 修改明细
                List<AccountDepositApplyDetail> details = depositApplyDetailService.listByApplyCode(apply.getApplyCode());
                if (CollectionUtils.isEmpty(details)) {
                    return Long.valueOf(apply.getId());
                }
                if (apply.getApprovalStatus().equals(ApprovalStatus.AUDIT_SUCCESS.getCode())) {
                    details.stream().forEach(detail -> {
                        detail.setRechargeStatus(Integer.parseInt(RechargeStatus.SUCCESS.getCode()));
                        detail.setUpdateUser(request.getApprovalUser());
                    });
                    // 如果审批通过，需要给员工增加余额；减少商户充值额度；保存商户充值额度变动明细
                    incrBalanceAndReduceRechargeLimit(details, apply);
                }
                if (apply.getApprovalStatus().equals(ApprovalStatus.AUDIT_FAILED.getCode())) {
                    details.stream().forEach(detail -> {
                        detail.setRechargeStatus(Integer.parseInt(RechargeStatus.NO.getCode()));
                        detail.setUpdateUser(request.getApprovalUser());
                    });
                }
                // 修改申请主表
                accountDepositApplyDao.save(apply);
                // 更新申请明细充值状态
                accountDepositApplyDetailDao.saveBatch(details);
                return Long.valueOf(apply.getId());
            } else {
                throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "操作频繁稍后再试！", null);
            }
        } catch (Exception e) {
            log.error("审批员工账号申请失败, 参数:{}, 商户:{}", JSON.toJSONString(request), e);
            throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "审批员工账号申请失败", e);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    @Override
    public Page<AccountDepositApplyInfo> page(Integer currentPage, Integer pageSize, AccountDepositApplyQuery query) {
        Page<AccountDepositApply> page = new Page<>();
        page.setCurrent(currentPage);
        page.setSize(pageSize);
        Page<AccountDepositApply> result = accountDepositApplyDao.page(page, QueryHelper.getWrapper(query));
        List<AccountDepositApplyInfo> infos = null;
        if (result != null && CollectionUtils.isNotEmpty(result.getRecords())) {
            List<AccountDepositApply> applys = result.getRecords();
            infos = depositApplyConverter.toInfoList(applys);
            infos.forEach(info -> {
                ApprovalStatus approvalStatus = ApprovalStatus.getByCode(info.getApprovalStatus());
                if (approvalStatus != null) {
                    info.setApprovalStatus(approvalStatus.getValue());
                }
            });
        }
        return PageUtils.toPage(result, infos);
    }

    @Override
    public List<AccountDepositApplyInfo> list(AccountDepositApplyQuery query) {
        List<AccountDepositApplyInfo> infos =  accountDepositApplyDao.getBaseMapper().selectList(QueryHelper.getWrapper(query));
        if (CollectionUtils.isNotEmpty(infos)) {
            infos.forEach(info -> {
                ApprovalStatus approvalStatus = ApprovalStatus.getByCode(info.getApprovalStatus());
                if (approvalStatus != null) {
                    info.setApprovalStatus(approvalStatus.getValue());
                }
            });
        }
        return infos;
    }

    @Override
    public AccountDepositApplyDetailInfo detail(Integer id) {
        AccountDepositApplyDetailInfo detailInfo = new AccountDepositApplyDetailInfo();
        AccountDepositApply apply = accountDepositApplyDao.getById(id);
        if (apply == null) {
            throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "申请不存在", null);
        }
        AccountDepositApplyInfo info = depositApplyConverter.toInfo(apply);
        detailInfo.setMainInfo(info);
        List<AccountDepositApplyDetail> details = depositApplyDetailService.listByApplyCode(apply.getApplyCode());
        if (CollectionUtils.isNotEmpty(details)) {
            AccountDepositApplyDetail detail = details.get(0);
            Account account = accountService.getByAccountCode(detail.getAccountCode());
            Department department = departmentService.getByDepartmentCode(account.getStoreCode());
            AccountDepositApplyItem item = new AccountDepositApplyItem();
            item.setAccountCode(detail.getAccountCode());
            item.setAccountName(account.getAccountName());
            item.setRechargeAmount(detail.getRechargeAmount());
            item.setDepartmentCode(account.getStoreCode());
            item.setDepartmentName(department.getDepartmentName());
            detailInfo.setItems(Lists.newArrayList(item));
        }
        return detailInfo;
    }

    /**
     * 给果审批通过，需要给员工增加余额；减少商户充值额度
     * @param details
     * @param apply
     */
    private void incrBalanceAndReduceRechargeLimit(List<AccountDepositApplyDetail> details, AccountDepositApply apply) {
        if (CollectionUtils.isNotEmpty(details)) {
            MerchantCredit merchantCredit = merchantCreditService.getByMerCode(apply.getMerCode());
            if (merchantCredit == null) {
                throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "商户额度不存在", null);
            }
            // 判断充值额度是否超过商户充值额度,减少商户充值额度
            double sumAmount = details.stream().mapToDouble(value -> value.getRechargeAmount().doubleValue()).sum();
            int success = merchantCreditService.decreaseRechargeLimit(new BigDecimal(sumAmount), merchantCredit.getId());
            if (success > 0) {
                // 增加员工福利余额及总余额
                List<AccountAmountType> list = assemblyAccountAmountTypeList(details, apply);
                accountAmountTypeService.batchSaveOrUpdate(list);
                list.forEach(accountAmountType -> {
                    accountService.increaseAccountBalance(accountAmountType.getAccountBalance(),apply.getApprovalUser(), accountAmountType.getAccountCode());
                });
            }
        }
    }

    private List<AccountAmountType> assemblyAccountAmountTypeList(List<AccountDepositApplyDetail> details, AccountDepositApply apply){
        List<AccountAmountType> list = new ArrayList<>();
        AccountAmountType accountAmountType = null;
        Date now = new Date();
        for (AccountDepositApplyDetail detail: details) {
            accountAmountType = new AccountAmountType();
            accountAmountType.setAccountCode(detail.getAccountCode());
            accountAmountType.setMerAccountTypeCode(apply.getMerAccountTypeCode());
            accountAmountType.setAccountBalance(detail.getRechargeAmount());
            accountAmountType.setDeleted(Boolean.FALSE);
            accountAmountType.setCreateTime(now);
            accountAmountType.setCreateUser(apply.getApprovalUser());
            accountAmountType.setUpdateTime(now);
            accountAmountType.setUpdateUser(apply.getApprovalUser());
            accountAmountType.setVersion(0);
            list.add(accountAmountType);
        }
        return list;
    }

    private AccountDepositApplyDetail assemblyAccountDepositApplyDetailList(AccountDepositApply apply,AccountDepositRequest accountAmounts) {
        AccountDepositApplyDetail detail;
        detail = new AccountDepositApplyDetail();
        detail.setAccountCode(accountAmounts.getAccountCode());
        detail.setApplyCode(apply.getApplyCode());
        detail.setRechargeAmount(accountAmounts.getRechargeAmount());
        detail.setRechargeStatus(Integer.parseInt(RechargeStatus.INIT.getCode()));
        detail.setDeleted(Boolean.FALSE);
        Date now = new Date();
        detail.setCreateTime(now);
        detail.setCreateUser(apply.getApprovalUser());
        detail.setUpdateTime(now);
        detail.setUpdateUser(apply.getApprovalUser());
        detail.setVersion(0);
        return detail;
    }

    private List<AccountDepositApplyDetail> assemblyAccountDepositApplyDetailList(AccountDepositApply apply,List<TempAccountDepositApply> temp) {
        List<AccountDepositApplyDetail> details = new ArrayList<>();
        AccountDepositApplyDetail detail = null;
        for (TempAccountDepositApply depositRequest : temp) {
            detail = new AccountDepositApplyDetail();
            detail.setAccountCode(depositRequest.getAccountCode());
            detail.setApplyCode(apply.getApplyCode());
            detail.setRechargeAmount(depositRequest.getRechargeAmount());
            detail.setRechargeStatus(Integer.parseInt(RechargeStatus.INIT.getCode()));
            detail.setDeleted(Boolean.FALSE);
            Date now = new Date();
            detail.setCreateTime(now);
            detail.setCreateUser(apply.getApprovalUser());
            detail.setUpdateTime(now);
            detail.setUpdateUser(apply.getApprovalUser());
            detail.setVersion(0);
            details.add(detail);
        }
        return details;
    }

    private void initAccountDepositApply(AccountDepositApply apply, MerchantUserInfo merchantUser) {
        apply.setApplyUser(merchantUser.getUsername());
        apply.setApplyTime(new Date());
        apply.setMerCode(merchantUser.getMerchantCode());
        apply.setApplyCode(UUID.randomUUID().toString());
        apply.setRechargeStatus(RechargeStatus.INIT.getCode());
        apply.setApprovalStatus(ApprovalStatus.AUDITING.getCode());
        apply.setCreateUser(merchantUser.getUserCode());
        apply.setUpdateUser(merchantUser.getUserCode());
        Date now = new Date();
        apply.setCreateTime(now);
        apply.setUpdateTime(now);
        apply.setVersion(0);
        apply.setDeleted(Boolean.FALSE);
    }
}