package com.welfare.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.welfare.common.constants.RedisKeyConstant;
import com.welfare.common.constants.WelfareConstant;
import com.welfare.common.domain.MerchantUserInfo;
import com.welfare.common.enums.MerIdentityEnum;
import com.welfare.common.exception.BusiException;
import com.welfare.common.exception.ExceptionCode;
import com.welfare.common.util.MerchantUserHolder;
import  com.welfare.persist.dao.AccountDepositApplyDao;
import com.welfare.persist.dao.AccountDepositApplyDetailDao;
import com.welfare.persist.dto.TempAccountDepositApplyDTO;
import com.welfare.persist.entity.*;
import com.welfare.service.*;
import com.welfare.service.converter.AccountDepositApplyConverter;
import com.welfare.service.converter.DepositApplyDetailConverter;
import com.welfare.service.dto.*;
import com.welfare.service.dto.accountapply.*;
import com.welfare.service.enums.ApprovalStatus;
import com.welfare.service.enums.ApprovalType;
import com.welfare.service.enums.RechargeStatus;
import com.welfare.service.helper.QueryHelper;
import com.welfare.service.utils.PageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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

    @Autowired
    private DepositService depositService;

    @Autowired
    private MerchantService merchantService;

    @Autowired
    private MerchantAccountTypeService accountTypeService;

    @Autowired
    private SequenceService sequenceService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long saveOne(DepositApplyRequest request, MerchantUserInfo merchantUser) {

        AccountDepositApply apply = getByRequestId(request.getRequestId());
        if (apply != null) {
            return apply.getId();
        }
        String lockKey = RedisKeyConstant.buidKey(RedisKeyConstant.ACCOUNT_DEPOSIT_APPLY_SAVE_REQUEST_ID, request.getRequestId());
        RLock lock = redissonClient.getFairLock(lockKey);
        try {
            boolean locked = lock.tryLock(2, TimeUnit.SECONDS);
            if (locked) {
                apply = getByRequestId(request.getRequestId());
                if (apply != null) {
                    return apply.getId();
                }
                Merchant merchant = merchantService.detailByMerCode(merchantUser.getMerchantCode());
                validationParmas(request,merchant,merchantUser,request.getInfo().getRechargeAmount());
                // 初始化主表
                apply = depositApplyConverter.toAccountDepositApply(request);
                if (accountService.findByPhone(request.getInfo().getPhone()) == null) {
                    throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "员工不存在！", null);
                }
                initAccountDepositApply(apply, request, merchant, merchantUser);
                // 设置充值人数
                apply.setRechargeNum(1);
                // 设置充值总金额
                apply.setRechargeAmount(request.getInfo().getRechargeAmount());
                apply.setApprovalType(ApprovalType.SINGLE.getCode());
                // 初始化明细
                AccountDepositApplyDetail detail = assemblyAccountDepositApplyDetailList(apply, request.getInfo());
                accountDepositApplyDao.save(apply);
                accountDepositApplyDetailDao.save(detail);
                return apply.getId();
            } else {
                throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "操作频繁稍后再试！", null);
            }
        } catch (Exception e) {
            log.error("新增员工账号申请失败, 参数:{}, 商户:{}", JSON.toJSONString(request), JSON.toJSONString(merchantUser), e);
            throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, e.getMessage(), e);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long saveBatch(DepositApplyRequest request, String fileId, MerchantUserInfo merchantUser) {

        AccountDepositApply apply = getByRequestId(request.getRequestId());
        if (apply != null) {
            return Long.valueOf(apply.getId());
        }
        String lockKey = RedisKeyConstant.buidKey(RedisKeyConstant.ACCOUNT_DEPOSIT_APPLY_SAVE_REQUEST_ID, request.getRequestId());
        RLock lock = redissonClient.getFairLock(lockKey);
        try {
            boolean locked = lock.tryLock(2, TimeUnit.SECONDS);
            if (locked) {
                apply = getByRequestId(request.getRequestId());
                if (apply != null) {
                    return Long.valueOf(apply.getId());
                }
                Merchant merchant = merchantService.detailByMerCode(merchantUser.getMerchantCode());
                // 初始化主表
                apply = depositApplyConverter.toAccountDepositApply(request);
                initAccountDepositApply(apply,request,merchant,merchantUser);
                List<TempAccountDepositApplyDTO> deposits = tempAccountDepositApplyService.listByFileIdExistAccount(fileId);
                if (CollectionUtils.isEmpty(deposits)) {
                    throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "请导入已存在的员工", null);
                }
                double sumAmoun = deposits.stream().mapToDouble(value -> value.getRechargeAmount().doubleValue()).sum();
                BigDecimal sumAmount = new BigDecimal(sumAmoun);
                validationParmas(request,merchant,merchantUser,sumAmount);
                apply.setMerAccountTypeCode(request.getMerAccountTypeCode());
                // 设置充值总金额
                apply.setRechargeAmount(sumAmount);
                // 设置充值人数
                apply.setRechargeNum(deposits.size());
                apply.setApprovalType(ApprovalType.BATCH.getCode());
                 // 初始化明细
                List<AccountDepositApplyDetail> details = assemblyAccountDepositApplyDetailList(apply, deposits);
                accountDepositApplyDetailDao.saveBatch(details);
                accountDepositApplyDao.save(apply);
                //删除临时文件记录表相关数据
                tempAccountDepositApplyService.delByFileId(fileId);
                return Long.valueOf(apply.getId());
            } else {
                throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "操作频繁稍后再试！", null);
            }
        } catch (Exception e) {
            log.error("批量员工账号申请失败, 参数:{}, 商户:{}", JSON.toJSONString(request), JSON.toJSONString(merchantUser), e);
            throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, e.getMessage(), e);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
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
        RLock lock = redissonClient.getFairLock(lockKey);
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
                //不支持非单个申请修改
                if (!apply.getApprovalType().equals(ApprovalType.SINGLE.getCode())) {
                    throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "不支持非单个申请修改", null);
                }
                // 判断金额是否超限
                MerchantCredit merchantCredit = merchantCreditService.getByMerCode(merchantUserInfo.getMerchantCode());
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
                apply.setMerAccountTypeName(request.getMerAccountTypeName());
                accountDepositApplyDao.saveOrUpdate(apply);
                List<AccountDepositApplyDetail> details = depositApplyDetailService.listByApplyCode(apply.getApplyCode());
                if (CollectionUtils.isNotEmpty(details)) {
                    AccountDepositApplyDetail detail = details.get(0);
                    if (accountService.getByAccountCode(detail.getAccountCode()) == null) {
                        throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "员工不存在！", null);
                    }
                    detail.setRechargeAmount(request.getInfo().getRechargeAmount());
                    detail.setUpdateUser(merchantUserInfo.getUserCode());
                    detail.setUpdateTime(new Date());
                    accountDepositApplyDetailDao.saveOrUpdate(detail);
                }
                return Long.valueOf(apply.getId());
            } else {
                throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "操作频繁稍后再试！", null);
            }
        } catch (Exception e) {
            log.error("修改员工账号申请失败, 参数:{}, 商户:{}", JSON.toJSONString(request), e);
            throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, e.getMessage(), e);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
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
        RLock lock = redissonClient.getFairLock(lockKey);
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
                //不支持非批量申请修改
                if (!apply.getApprovalType().equals(ApprovalType.BATCH.getCode())) {
                    throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "不支持非批量申请修改", null);
                }
                if (StringUtils.isNotBlank(fileId)) {
                    List<TempAccountDepositApplyDTO> temps = tempAccountDepositApplyService.listByFileIdExistAccount(fileId);
                    // 至少选一个员工
                    if (CollectionUtils.isEmpty(temps)) {
                        throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "至少重新上传一个员工", null);
                    }
                    // 判断金额是否超限
                    double sumAmoun = temps.stream().mapToDouble(value -> value.getRechargeAmount().doubleValue()).sum();
                    BigDecimal sumAmount = new BigDecimal(sumAmoun);
                    MerchantCredit merchantCredit = merchantCreditService.getByMerCode(merchantUserInfo.getMerchantCode());
                    // 修改充值明细表
                    if (merchantCredit.getRechargeLimit().compareTo(sumAmount) < 0) {
                        throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "商户充值额度不足！", null);
                    }
                    List<AccountDepositApplyDetail> details = assemblyAccountDepositApplyDetailList(apply, temps);
                    depositApplyDetailService.delByApplyCode(apply.getApplyCode());
                    accountDepositApplyDetailDao.saveBatch(details);
                    tempAccountDepositApplyService.delByFileId(fileId);
                    apply.setRechargeAmount(sumAmount);
                    apply.setRechargeNum(temps.size());
                }
                Date now = new Date();
                apply.setUpdateTime(now);
                apply.setUpdateUser(merchantUserInfo.getUserCode());
                apply.setApplyRemark(request.getApplyRemark());
                apply.setMerAccountTypeCode(request.getMerAccountTypeCode());
                apply.setMerAccountTypeName(request.getMerAccountTypeName());
                accountDepositApplyDao.saveOrUpdate(apply);
                return apply.getId();
            } else {
                throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "操作频繁稍后再试！", null);
            }
        } catch (Exception e) {
            log.error("批量修改员工账号申请失败, 参数:{}, 商户:{}", JSON.toJSONString(request), e);
            throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, e.getMessage(), e);
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
            return apply.getId();
        }
        String lockKey = RedisKeyConstant.buidKey(RedisKeyConstant.ACCOUNT_DEPOSIT_APPLY__ID, request.getId()+"");
        RLock lock = redissonClient.getFairLock(lockKey);
        try {
            boolean locked = lock.tryLock(4, TimeUnit.SECONDS);
            if (locked) {
                apply = accountDepositApplyDao.getById(request.getId());
                if (apply == null) {
                    throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, String.format("账号存款申请不存在[requestId:%s]", request.getId()), null);
                }
                //已经审批过了
                if (!apply.getApprovalStatus().equals(ApprovalStatus.AUDITING.getCode())) {
                    return apply.getId();
                }
                apply.setApprovalStatus(ApprovalStatus.getByCode(request.getApprovalStatus()).getCode());
                apply.setApprovalRemark(request.getApplyRemark());
                apply.setApprovalUser(request.getApprovalUser());
                apply.setApprovalTime(new Date());
                apply.setApprovalOpinion(request.getApprovalOpinion());
                // 修改明细
                List<AccountDepositApplyDetail> details = depositApplyDetailService.listByApplyCode(apply.getApplyCode());
                if (CollectionUtils.isEmpty(details)) {
                    log.info("账号额度申请无员工,applyCode:{}", apply.getApplyCode());
                    return apply.getId();
                }
                if (apply.getApprovalStatus().equals(ApprovalStatus.AUDIT_SUCCESS.getCode())) {
                    apply.setRechargeStatus(RechargeStatus.SUCCESS.getCode());
                    details.stream().forEach(detail -> {
                        detail.setRechargeStatus(RechargeStatus.SUCCESS.getCode());
                        detail.setUpdateUser(request.getApprovalUser());
                    });
                    // 如果审批通过，需要给员工增加余额；减少商户充值额度；保存商户充值额度变动明细
                    List<AccountDepositApplyDetail> existDetails = depositApplyDetailService
                            .listByApplyCodeIfAccountExist(apply.getApplyCode());
                    depositService.deposit(Deposit.of(apply, existDetails));
                }
                if (apply.getApprovalStatus().equals(ApprovalStatus.AUDIT_FAILED.getCode())) {
                    apply.setRechargeStatus(RechargeStatus.NO.getCode());
                    details.stream().forEach(detail -> {
                        detail.setRechargeStatus(RechargeStatus.NO.getCode());
                        detail.setUpdateUser(request.getApprovalUser());
                    });
                }
                // 修改申请主表
                accountDepositApplyDao.saveOrUpdate(apply);
                // 更新申请明细充值状态
                accountDepositApplyDetailDao.saveOrUpdateBatch(details);
                return apply.getId();
            } else {
                throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "操作频繁稍后再试！", null);
            }
        } catch (Exception e) {
            log.error("审批员工账号申请失败, 参数:{}, 商户:{}", JSON.toJSONString(request), e);
            throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, e.getMessage(), e);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    @Override
    public Page<AccountDepositApplyInfo> page(int currentPage, int pageSize, AccountDepositApplyQuery query) {
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
    public List<AccountDepositApplyExcelInfo> list(AccountDepositApplyQuery query) {
        List<AccountDepositApply> applies =  accountDepositApplyDao.getBaseMapper().selectList(QueryHelper.getWrapper(query));
        List<AccountDepositApplyExcelInfo> infos = depositApplyConverter.toInfoExcelList(applies);
        if (CollectionUtils.isNotEmpty(infos)) {
//            List<MerchantAccountType> accountTypes = accountTypeService.list(new MerchantAccountTypeReq());
//            Map<String, MerchantAccountType> accountTypeMap = new HashMap<>();
//            if (CollectionUtils.isNotEmpty(accountTypes)) {
//                accountTypeMap = accountTypes.stream().collect(Collectors.toMap(MerchantAccountType::getMerAccountTypeCode, MerchantAccountType->MerchantAccountType));
//            }
//            Map<String, MerchantAccountType> finalAccountTypeMap = accountTypeMap;
            infos.forEach(info -> {
                ApprovalStatus approvalStatus = ApprovalStatus.getByCode(info.getApprovalStatus());
                if (approvalStatus != null) {
                    info.setApprovalStatus(approvalStatus.getValue());
                    //info.setMerAccountTypeName(finalAccountTypeMap.get(info.getMerAccountTypeCode()).getMerAccountTypeName());
                }
            });
        }
        return infos;
    }

    @Override
    public AccountDepositApplyDetailInfo detail(Long id) {
        AccountDepositApplyDetailInfo detailInfo = new AccountDepositApplyDetailInfo();
        AccountDepositApply apply = accountDepositApplyDao.getById(id);
        if (apply == null) {
            throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "申请不存在", null);
        }
        AccountDepositApplyInfo info = depositApplyConverter.toInfo(apply);
        detailInfo.setMainInfo(info);
        if (apply.getApprovalType().equals(ApprovalType.SINGLE.getCode())) {
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
            item.setPhone(account.getPhone());
            detailInfo.setItems(Lists.newArrayList(item));
          }
        }
        return detailInfo;
    }

//    /**
//     * 给果审批通过，需要给员工增加余额；减少商户充值额度
//     * @param details
//     * @param apply
//     */
//    private void incrBalanceAndReduceRechargeLimit(List<AccountDepositApplyDetail> details, AccountDepositApply apply) {
//        if (CollectionUtils.isNotEmpty(details)) {
//            MerchantCredit merchantCredit = merchantCreditService.getByMerCode(apply.getMerCode());
//            if (merchantCredit == null) {
//                throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "商户额度不存在", null);
//            }
//            // 判断充值额度是否超过商户充值额度,减少商户充值额度
//            double sumAmount = details.stream().mapToDouble(value -> value.getRechargeAmount().doubleValue()).sum();
//            int success = merchantCreditService.decreaseRechargeLimit(new BigDecimal(sumAmount), merchantCredit.getId());
//            if (success > 0) {
//                // 增加员工福利余额及总余额
//                List<AccountAmountType> list = assemblyAccountAmountTypeList(details, apply);
//                accountAmountTypeService.batchSaveOrUpdate(list);
//                list.forEach(accountAmountType -> {
//                    accountService.increaseAccountBalance(accountAmountType.getAccountBalance(),apply.getApprovalUser(), accountAmountType.getAccountCode());
//                });
//            }
//        }
//    }
//
//    private List<AccountAmountType> assemblyAccountAmountTypeList(List<AccountDepositApplyDetail> details, AccountDepositApply apply){
//        List<AccountAmountType> list = new ArrayList<>();
//        AccountAmountType accountAmountType = null;
//        Date now = new Date();
//        for (AccountDepositApplyDetail detail: details) {
//            accountAmountType = new AccountAmountType();
//            accountAmountType.setAccountCode(detail.getAccountCode());
//            accountAmountType.setMerAccountTypeCode(apply.getMerAccountTypeCode());
//            accountAmountType.setAccountBalance(detail.getRechargeAmount());
//            accountAmountType.setDeleted(Boolean.FALSE);
//            accountAmountType.setCreateTime(now);
//            accountAmountType.setCreateUser(apply.getApprovalUser());
//            accountAmountType.setUpdateTime(now);
//            accountAmountType.setUpdateUser(apply.getApprovalUser());
//            accountAmountType.setVersion(0);
//            list.add(accountAmountType);
//        }
//        return list;
//    }

    private AccountDepositApplyDetail assemblyAccountDepositApplyDetailList(AccountDepositApply apply,AccountDepositRequest accountAmounts) {
        AccountDepositApplyDetail detail;
        detail = new AccountDepositApplyDetail();
        Account account = accountService.findByPhoneAndMerCode(accountAmounts.getPhone(), MerchantUserHolder.getMerchantUser().getMerchantCode());
        if (account == null) {
          throw new BusiException("员工不存在");
        }
        detail.setAccountCode(account.getAccountCode());
        detail.setApplyCode(apply.getApplyCode());
        detail.setRechargeAmount(accountAmounts.getRechargeAmount());
        detail.setRechargeStatus(RechargeStatus.INIT.getCode());
        detail.setDeleted(Boolean.FALSE);
        Date now = new Date();
        detail.setCreateTime(now);
        detail.setCreateUser(apply.getApprovalUser());
        detail.setUpdateTime(now);
        detail.setUpdateUser(apply.getApprovalUser());
        detail.setVersion(0);
        detail.setTransNo(sequenceService.nextNo(WelfareConstant.SequenceType.DEPOSIT.code()) + "");
        return detail;
    }

    private List<AccountDepositApplyDetail> assemblyAccountDepositApplyDetailList(AccountDepositApply apply,List<TempAccountDepositApplyDTO> temp) {
        List<AccountDepositApplyDetail> details = new ArrayList<>();
        AccountDepositApplyDetail detail = null;
        for (TempAccountDepositApplyDTO depositRequest : temp) {
            detail = new AccountDepositApplyDetail();
            detail.setAccountCode(depositRequest.getAccountCode());
            detail.setApplyCode(apply.getApplyCode());
            detail.setRechargeAmount(depositRequest.getRechargeAmount());
            detail.setRechargeStatus(RechargeStatus.INIT.getCode());
            detail.setDeleted(Boolean.FALSE);
            Date now = new Date();
            detail.setCreateTime(now);
            detail.setCreateUser(apply.getApprovalUser());
            detail.setUpdateTime(now);
            detail.setUpdateUser(apply.getApprovalUser());
            detail.setVersion(0);
            detail.setTransNo(sequenceService.nextNo(WelfareConstant.SequenceType.DEPOSIT.code()) + "");
            details.add(detail);
        }
        return details;
    }

    private void initAccountDepositApply(AccountDepositApply apply, DepositApplyRequest request, Merchant merchant, MerchantUserInfo merchantUser) {
        Date now = new Date();
        apply.setMerAccountTypeCode(request.getMerAccountTypeCode());
        apply.setApplyUser(merchantUser.getUsername());
        apply.setApplyTime(now);
        apply.setMerCode(merchantUser.getMerchantCode());
        apply.setApplyCode(sequenceService.nextNo(WelfareConstant.SequenceType.ACCOUNT_DEPOSIT_APPLY.code()) + "");
        apply.setRechargeStatus(RechargeStatus.INIT.getCode());
        apply.setApprovalStatus(ApprovalStatus.AUDITING.getCode());
        apply.setCreateUser(merchantUser.getUserCode());
        apply.setUpdateUser(merchantUser.getUserCode());
        apply.setChannel(WelfareConstant.Channel.PLATFORM.code());
        apply.setCreateTime(now);
        apply.setUpdateTime(now);
        apply.setVersion(0);
        apply.setDeleted(Boolean.FALSE);
    }

    private void validationParmas(DepositApplyRequest request,Merchant merchant,
                                 MerchantUserInfo merchantUser, BigDecimal amount){
        if (merchant == null) {
            throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "商户不存在！", null);
        }
        if (StringUtils.isBlank(merchant.getMerIdentity())) {
            throw new BusiException("商户没有设置属性！");
        }
        List<String > merIdentityList = Lists.newArrayList(merchant.getMerIdentity().split(","));
        if (!merIdentityList.contains(MerIdentityEnum.customer.getCode())) {
            throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "仅支持对属于[客户]的商户充值", null);
        }
        // 判断金额是否超限
        MerchantCredit merchantCredit = merchantCreditService.getByMerCode(merchantUser.getMerchantCode());
        if (merchantCredit.getRechargeLimit().compareTo(amount) < 0) {
            throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "商户充值额度不足！", null);
        }
    }
}