package com.welfare.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.welfare.common.constants.AccountBindStatus;
import com.welfare.common.constants.AccountChangeType;
import com.welfare.common.constants.AccountStatus;
import com.welfare.common.constants.WelfareConstant;
import com.welfare.common.enums.ShoppingActionTypeEnum;
import com.welfare.common.exception.BizAssert;
import com.welfare.common.exception.BizException;
import com.welfare.common.exception.ExceptionCode;
import com.welfare.common.util.MerchantUserHolder;
import com.welfare.persist.dao.*;
import com.welfare.persist.entity.*;
import com.welfare.service.*;
import com.welfare.service.dto.AccountReq;
import com.welfare.service.dto.DepartmentTree;
import com.welfare.service.dto.nhc.*;
import com.welfare.service.sync.event.AccountEvt;
import com.welfare.service.utils.AccountUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/4/13 10:29 上午
 */
@Service
@Slf4j
public class NhcServiceImpl implements NhcService {

    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountDao accountDao;
    @Autowired
    private SequenceService sequenceService;
    @Autowired
    private MerchantService merchantService;
    @Autowired
    private AccountAmountTypeDao accountAmountTypeDao;
    @Autowired
    private AccountTypeDao accountTypeDao;
    @Autowired
    private SubAccountDao subAccountDao;
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private AccountAmountTypeGroupService accountAmountTypeGroupService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private AccountChangeEventRecordService accountChangeEventRecordService;

    @Override
    public String saveOrUpdateUser(NhcUserReq userReq) {
        Account account;
        Merchant merchant = merchantService.getMerchantByMerCode(userReq.getMerCode());
        BizAssert.notNull(merchant,
                ExceptionCode.ILLEGALITY_ARGURMENTS, "商户不存在");
        boolean joinGroup = false;
        if (StringUtils.isNoneBlank(userReq.getAccountCode())) {
            // 修改
            account = accountService.getByAccountCode(Long.parseLong(userReq.getAccountCode()));
            BizAssert.notNull(account, ExceptionCode.ILLEGALITY_ARGURMENTS, String.format("员工不存在[%s]", userReq.getAccountCode()));
            BizAssert.isTrue(userReq.getMerCode().equals(account.getMerCode()), ExceptionCode.ILLEGALITY_ARGURMENTS, "无权限操作！");
            account.setAccountName(userReq.getUserName());
            account.setPhone(userReq.getPhone());
        } else {
            // 新增
            account = assemblyUser(NhcAccountReq.of(userReq), merchant);
            if (StringUtils.isNoneBlank(userReq.getFamilyUserCode())) {
                joinGroup = true;
            }
        }
        BizAssert.isTrue(accountDao.saveOrUpdate(account));
        // 加入家庭
        if (joinGroup) {
            boolean success = accountAmountTypeGroupService.addByAccountCodeAndMerAccountTypeCode(
                    Long.parseLong(userReq.getAccountCode()),
                    Long.parseLong(userReq.getFamilyUserCode()),
                    WelfareConstant.MerAccountTypeCode.MALL_POINT.code());
            BizAssert.isTrue(success);
        }
        // 同步商户
        applicationContext.publishEvent(AccountEvt.builder()
                .typeEnum(StringUtils.isNoneBlank(userReq.getAccountCode()) ? ShoppingActionTypeEnum.UPDATE : ShoppingActionTypeEnum.ADD)
                .accountList(Collections.singletonList(account)).build());
        return String.valueOf(account.getAccountCode());
    }

    private Account assemblyUser(NhcAccountReq accountReq, Merchant merchant) {
        Account account = new Account();
        Long accountCode = sequenceService.nextNo(WelfareConstant.SequenceType.ACCOUNT_CODE.code());
        account.setCreateUser(MerchantUserHolder.getMerchantUser().getUsername());
        account.setAccountCode(accountCode);
        account.setAccountName(accountReq.getAccountName());
        account.setMerCode(accountReq.getMerCode());
        account.setPhone(accountReq.getPhone());
        account.setAccountStatus(AccountStatus.ENABLE.getCode());
        account.setOfflineLock(WelfareConstant.AccountOfflineFlag.ENABLE.code());
        account.setBinding(AccountBindStatus.NO_BIND.getCode());
        List<AccountType> accountTypes = accountTypeDao.getByMerCode(accountReq.getMerCode());
        BizAssert.notEmpty(accountTypes, ExceptionCode.ILLEGALITY_ARGURMENTS, "商户没有员工类型");
        account.setAccountTypeCode(accountTypes.get(0).getTypeCode());
        if (StringUtils.isBlank(accountReq.getDepartmentCode())) {
            List<DepartmentTree> departmentTrees = departmentService.tree(accountReq.getMerCode());
            account.setDepartment(departmentTrees.get(0).getDepartmentCode());
        } else {
            account.setDepartment(accountReq.getDepartmentCode());
        }
        // 创建甜橙卡子账户
        SubAccount subAccount = new SubAccount();
        subAccount.setSubAccountType(WelfareConstant.PaymentChannel.WELFARE.code());
        subAccount.setAccountCode(account.getAccountCode());
        BizAssert.isTrue(subAccountDao.save(subAccount));
        // 创建积分福利账户
        AccountAmountType accountAmountType1 = new AccountAmountType();
        accountAmountType1.setAccountCode(accountCode);
        accountAmountType1.setMerAccountTypeCode(WelfareConstant.MerAccountTypeCode.MALL_POINT.code());
        accountAmountType1.setJoinedGroup(false);
        AccountAmountType accountAmountType2 = new AccountAmountType();
        accountAmountType2.setAccountCode(accountCode);
        accountAmountType2.setMerAccountTypeCode(WelfareConstant.MerAccountTypeCode.SURPLUS_QUOTA.code());
        AccountAmountType accountAmountType3 = new AccountAmountType();
        accountAmountType3.setAccountCode(accountCode);
        accountAmountType3.setMerAccountTypeCode(WelfareConstant.MerAccountTypeCode.SURPLUS_QUOTA_OVERPAY.code());
        accountAmountTypeDao.saveBatch(Lists.newArrayList(accountAmountType1,accountAmountType2,accountAmountType3));

        AccountChangeEventRecord accountChangeEventRecord = AccountUtils
                .assemableChangeEvent(AccountChangeType.ACCOUNT_NEW, account.getAccountCode(),
                        account.getCreateUser());
        accountChangeEventRecordService.save(accountChangeEventRecord);
        account.setChangeEventId(accountChangeEventRecord.getId());
        return account;
    }

    @Override
    public NhcUserInfoDTO getUserInfo(NhcQueryUserReq queryUserReq) {
        return null;
    }

    @Override
    public Boolean rechargeMallPoint(NhcUserPointRechargeReq pointRechargeReq) {
        return null;
    }

    @Override
    public Page<NhcAccountBillDetailDTO> getUserBillPage(NhcUserPageReq userPageReq) {
        return null;
    }

    @Override
    public Boolean leaveFamily(String userCode) {
        return null;
    }

    @Override
    public String saveOrUpdateAccount(NhcAccountReq nhcAccountReq) {
        return null;
    }

    @Override
    public NhcAccountInfoDTO getAccountInfo(String accountCode) {
        return null;
    }

    @Override
    public Page<NhcAccountBillDetailDTO> getAccountBillPage(NhcUserPageReq nhcUserPageReq) {
        return null;
    }

    @Override
    public NhcFamilyMemberDTO getFamilyInfo(String userCode) {
        return null;
    }
}
