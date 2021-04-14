package com.welfare.service.impl;

import com.welfare.common.constants.WelfareConstant;
import com.welfare.common.exception.BizAssert;
import com.welfare.common.exception.ExceptionCode;
import com.welfare.persist.dao.AccountAmountTypeDao;
import com.welfare.persist.dao.AccountAmountTypeGroupDao;
import com.welfare.persist.dao.AccountBillDetailDao;
import com.welfare.persist.dao.AccountDeductionDetailDao;
import com.welfare.persist.entity.*;
import com.welfare.persist.dao.AccountAmountTypeGroupDao;
import com.welfare.persist.dao.AccountDao;
import com.welfare.persist.entity.Account;
import com.welfare.persist.entity.AccountAmountType;
import com.welfare.persist.entity.AccountAmountTypeGroup;
import com.welfare.service.AccountAmountTypeGroupService;
import com.welfare.service.AccountAmountTypeService;
import com.welfare.service.AccountService;
import com.welfare.service.SequenceService;
import com.welfare.service.dto.BatchSequence;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import java.util.Objects;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/4/13 2:46 下午
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AccountAmountTypeGroupServiceImpl implements AccountAmountTypeGroupService {

    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountAmountTypeService accountAmountTypeService;
    @Autowired
    private AccountAmountTypeDao accountAmountTypeDao;
    @Autowired
    private AccountAmountTypeGroupDao accountAmountTypeGroupDao;
    @Autowired
    private SequenceService sequenceService;
    @Autowired
    private AccountBillDetailDao accountBillDetailDao;
    @Autowired
    private AccountDeductionDetailDao accountDeductionDetailDao;

    @Override
    public boolean removeByAccountCode(Long accountCode, String merAccountTypeCode) {
        Account account = accountService.getByAccountCode(accountCode);
        BizAssert.notNull(account, ExceptionCode.ILLEGALITY_ARGURMENTS, "员工不存在");
        AccountAmountType accountAmountType = accountAmountTypeService.queryOne(accountCode, merAccountTypeCode);
        BizAssert.notNull(accountAmountType, ExceptionCode.ILLEGALITY_ARGURMENTS, "福利类型为空");
        BizAssert.isTrue(accountAmountType.getJoinedGroup() != null && accountAmountType.getJoinedGroup()
                        && Objects.nonNull(accountAmountType.getAccountAmountTypeGroupId()),
                ExceptionCode.ILLEGALITY_ARGURMENTS, "该员工没加入任何组");
        accountAmountType.setJoinedGroup(Boolean.FALSE);
        long groupId = accountAmountType.getAccountAmountTypeGroupId();
        accountAmountType.setAccountAmountTypeGroupId(null);
        // 记录流水
        String transNo = sequenceService.nextFullNo(WelfareConstant.SequenceType.DEPOSIT.code());
        AccountBillDetail accountBillDetail = assemblyAccountBillDetail(
                account,
                accountAmountType,
                transNo,
                WelfareConstant.TransType.LEAVE_GROUP,
                groupId);
        AccountDeductionDetail accountDeductionDetail = assemblyAccountDeductionDetail(
                account,
                transNo,
                WelfareConstant.TransType.LEAVE_GROUP,
                accountAmountType,
                groupId);
        accountBillDetailDao.save(accountBillDetail);
        accountDeductionDetailDao.save(accountDeductionDetail);
        return accountAmountTypeDao.updateById(accountAmountType);
    }

    @Override
    public boolean addByAccountCodeAndMerAccountTypeCode(Long joinAccountCode, Long groupAccountCode, String merAccountTypeCode) {
        return false;
    }

    @Override
    public AccountAmountTypeGroup queryByAccountCode(Long accountCode) {
        AccountAmountType accountAmountType = accountAmountTypeDao.queryByAccountCodeAndAmountType(
                accountCode,
                WelfareConstant.MerAccountTypeCode.MALL_POINT.code()
        );
        if (Objects.nonNull(accountAmountType) && accountAmountType.getJoinedGroup()) {
            return accountAmountTypeGroupDao.getById(accountAmountType.getAccountAmountTypeGroupId());
        }else{
            return null;
        }
    }
    @Override
    public boolean addByAccountCodeAndMerAccountTypeCode(Long joinAccountCode, Long groupAccountCode, String merAccountTypeCode) {
        Account joinAccount = accountService.getByAccountCode(joinAccountCode);
        BizAssert.notNull(joinAccount, ExceptionCode.ILLEGALITY_ARGURMENTS, "员工不存在");
        Account groupAccount = accountService.getByAccountCode(groupAccountCode);
        BizAssert.notNull(groupAccount, ExceptionCode.ILLEGALITY_ARGURMENTS, "家庭员工不存在");
        Map<Long, Account> accountMap = new HashMap<>();
        accountMap.put(joinAccount.getAccountCode(), joinAccount);
        accountMap.put(groupAccount.getAccountCode(), groupAccount);
        BizAssert.isTrue(joinAccount.getMerCode().equals(groupAccount.getMerCode()), ExceptionCode.ILLEGALITY_ARGURMENTS, "家庭员工不存在");
        AccountAmountType joinAccountAmountType = accountAmountTypeService.queryOne(joinAccountCode, merAccountTypeCode);
        AccountAmountType groupAccountAmountType = accountAmountTypeService.queryOne(groupAccountCode, merAccountTypeCode);
        BizAssert.isTrue(Objects.nonNull(joinAccountAmountType) && Objects.nonNull(groupAccountAmountType),
                ExceptionCode.ILLEGALITY_ARGURMENTS, "福利账户不存在");
        AccountAmountTypeGroup amountTypeGroup;
        List<AccountAmountType> updateAccountAmountTypes = new ArrayList<>();

        if (groupAccountAmountType.getJoinedGroup() && Objects.nonNull(groupAccountAmountType.getAccountAmountTypeGroupId())) {
            amountTypeGroup = accountAmountTypeGroupDao.getById(groupAccountAmountType.getAccountAmountTypeGroupId());
            BizAssert.notNull(amountTypeGroup, ExceptionCode.ILLEGALITY_ARGURMENTS, "用户组不存在");
            amountTypeGroup.setBalance(amountTypeGroup.getBalance().add(joinAccountAmountType.getAccountBalance()));
            updateAccountAmountTypes.add(joinAccountAmountType);
        } else {
            amountTypeGroup = new AccountAmountTypeGroup();
            amountTypeGroup.setBalance(joinAccountAmountType.getAccountBalance().add(groupAccountAmountType.getAccountBalance()));
            amountTypeGroup.setMerAccountTypeCode(merAccountTypeCode);
            amountTypeGroup.setGroupCode(sequenceService.nextFullNo(WelfareConstant.SequenceType.ACCOUNT_AMOUNT_TYPE_GROUP_CODE.code()));
            updateAccountAmountTypes.add(joinAccountAmountType);
            updateAccountAmountTypes.add(groupAccountAmountType);
        }
        // 记录流水
        List<AccountBillDetail> accountBillDetails = new ArrayList<>();
        List<AccountDeductionDetail> accountDeductionDetails = new ArrayList<>();
        BatchSequence batchSequence = sequenceService.batchGenerate(WelfareConstant.SequenceType.DEPOSIT.code(), updateAccountAmountTypes.size());
        AtomicInteger index = new AtomicInteger();
        updateAccountAmountTypes.forEach(accountAmountType -> {
            String transNo = batchSequence.getSequences().get(index.getAndIncrement()).getSequenceNo() + "";
            AccountBillDetail accountBillDetail = assemblyAccountBillDetail(
                    accountMap.get(accountAmountType.getAccountCode()),
                    accountAmountType,
                    transNo,
                    WelfareConstant.TransType.JOINED_GROUP,
                    amountTypeGroup.getId());
            accountBillDetails.add(accountBillDetail);
            AccountDeductionDetail accountDeductionDetail = assemblyAccountDeductionDetail(
                    accountMap.get(accountAmountType.getAccountCode()),
                    transNo,
                    WelfareConstant.TransType.JOINED_GROUP,
                    accountAmountType,
                    amountTypeGroup.getId());
            accountDeductionDetails.add(accountDeductionDetail);
        });
        accountBillDetailDao.saveBatch(accountBillDetails);
        accountDeductionDetailDao.saveBatch(accountDeductionDetails);
        // 创建或更新组
        boolean flag1 = accountAmountTypeGroupDao.saveOrUpdate(amountTypeGroup);
        joinAccountAmountType.setJoinedGroup(Boolean.TRUE);
        joinAccountAmountType.setAccountAmountTypeGroupId(amountTypeGroup.getId());
        joinAccountAmountType.setAccountBalance(BigDecimal.ZERO);
        groupAccountAmountType.setJoinedGroup(Boolean.TRUE);
        groupAccountAmountType.setAccountAmountTypeGroupId(amountTypeGroup.getId());
        groupAccountAmountType.setAccountBalance(BigDecimal.ZERO);
        // 组成员福利账户金额清零
        boolean flag2 = accountAmountTypeDao.updateBatchById(updateAccountAmountTypes);
        return flag1 && flag2;
    }

    private AccountBillDetail assemblyAccountBillDetail(Account account, AccountAmountType accountAmountType, String transNo, WelfareConstant.TransType transType,
                                                        Long accountAmountTypeGroupId) {
        AccountBillDetail accountBillDetail = new AccountBillDetail();
        accountBillDetail.setAccountCode(account.getAccountCode());
        accountBillDetail.setAccountBalance(account.getAccountBalance());
        accountBillDetail.setChannel(WelfareConstant.Channel.PLATFORM.code());
        accountBillDetail.setTransNo(transNo);
        accountBillDetail.setTransAmount(accountAmountType.getAccountBalance());
        accountBillDetail.setTransTime(Calendar.getInstance().getTime());
        accountBillDetail.setSurplusQuota(account.getSurplusQuota());
        accountBillDetail.setSurplusQuotaOverpay(account.getSurplusQuotaOverpay());
        accountBillDetail.setTransType(transType.code());
        accountBillDetail.setAccountAmountTypeGroupId(accountAmountTypeGroupId);
        return accountBillDetail;
    }

    private AccountDeductionDetail assemblyAccountDeductionDetail(Account account, String transNo,
                                                                  WelfareConstant.TransType transType,
                                                                  AccountAmountType accountAmountType,
                                                                  Long accountAmountTypeGroupId) {
        AccountDeductionDetail accountDeductionDetail = new AccountDeductionDetail();
        accountDeductionDetail.setAccountCode(account.getAccountCode());
        accountDeductionDetail.setAccountDeductionAmount(accountAmountType.getAccountBalance().abs());
        accountDeductionDetail.setAccountAmountTypeBalance(BigDecimal.ZERO);
        accountDeductionDetail.setMerAccountType(accountAmountType.getMerAccountTypeCode());
        accountDeductionDetail.setTransNo(transNo);
        accountDeductionDetail.setTransType(transType.code());
        accountDeductionDetail.setTransAmount(accountAmountType.getAccountBalance().abs());
        accountDeductionDetail.setReversedAmount(BigDecimal.ZERO);
        accountDeductionDetail.setTransTime(Calendar.getInstance().getTime());
        accountDeductionDetail.setMerDeductionCreditAmount(BigDecimal.ZERO);
        accountDeductionDetail.setMerDeductionAmount(BigDecimal.ZERO);
        accountDeductionDetail.setChanel(WelfareConstant.Channel.PLATFORM.code());
        accountDeductionDetail.setSelfDeductionAmount(BigDecimal.ZERO);
        accountDeductionDetail.setAccountAmountTypeGroupId(accountAmountTypeGroupId);
        return accountDeductionDetail;
    }
}
