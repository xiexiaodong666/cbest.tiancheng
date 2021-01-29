package com.welfare.service.impl;

import com.alibaba.fastjson.JSON;
import com.welfare.common.annotation.DistributedLock;
import com.welfare.common.constants.RedisKeyConstant;
import com.welfare.common.constants.WelfareConstant;
import com.welfare.common.exception.BusiException;
import com.welfare.common.exception.ExceptionCode;
import com.welfare.common.util.DistributedLockUtil;
import com.welfare.persist.dao.*;
import com.welfare.persist.entity.*;
import com.welfare.service.*;
import com.welfare.service.dto.RefundRequest;
import com.welfare.service.operator.merchant.domain.MerchantAccountOperation;
import com.welfare.service.operator.payment.domain.AccountAmountDO;
import com.welfare.service.operator.payment.domain.RefundOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.welfare.common.constants.RedisKeyConstant.MER_ACCOUNT_TYPE_OPERATE;
import static com.welfare.common.constants.WelfareConstant.MerAccountTypeCode.SURPLUS_QUOTA;

/**
 * Description: 退款
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 1/15/2021
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class RefundServiceImpl implements RefundService {
    private final AccountDeductionDetailDao accountDeductionDetailDao;
    private final AccountBillDetailDao accountBillDetailDao;
    private final AccountService accountService;
    private final AccountAmountTypeDao accountAmountTypeDao;
    private final AccountDao accountDao;
    private final MerchantCreditService merchantCreditService;
    private final AccountAmountTypeService accountAmountTypeService;
    private final SupplierStoreService supplierStoreService;
    @Override
    @Transactional(rollbackFor = Exception.class)
    @DistributedLock(lockPrefix = "e-welfare-refund::", lockKey = "#refundRequest.originalTransNo")
    public void handleRefundRequest(RefundRequest refundRequest) {
        String originalTransNo = refundRequest.getOriginalTransNo();
        List<AccountDeductionDetail> refundDeductionDetailInDb = accountDeductionDetailDao
                .queryByRelatedTransNoAndTransType(refundRequest.getOriginalTransNo(), WelfareConstant.TransType.REFUND.code());
        if (hasRefunded(refundRequest, refundDeductionDetailInDb)) {
            return;
        }
        List<AccountDeductionDetail> accountDeductionDetails = accountDeductionDetailDao.queryByTransNoAndTransType(
                originalTransNo,
                WelfareConstant.TransType.CONSUME.code()
        );
        Assert.isTrue(!CollectionUtils.isEmpty(accountDeductionDetails), "未找到正向支付流水");
        AccountDeductionDetail first = accountDeductionDetails.get(0);
        String lockKey = RedisKeyConstant.ACCOUNT_AMOUNT_TYPE_OPERATE + first.getAccountCode();
        RLock accountLock = DistributedLockUtil.lockFairly(lockKey);
        try {
            Account account = accountService.getByAccountCode(first.getAccountCode());
            refundRequest.setAccountMerCode(account.getMerCode());
            log.error("accountInfo:{}",account);
            List<AccountAmountDO> accountAmountDOList = accountAmountTypeService.queryAccountAmountDO(account);
            //按照deductionOrder逆序
            accountAmountDOList.sort(Comparator.comparing(x -> -1 * x.getMerchantAccountType().getDeductionOrder()));
            List<AccountAmountType> accountAmountTypes = accountAmountDOList.stream().map(AccountAmountDO::getAccountAmountType)
                    .collect(Collectors.toList());
            RLock merAccountLock = DistributedLockUtil.lockFairly(MER_ACCOUNT_TYPE_OPERATE + ":" + account.getMerCode());
            try {
                refund(refundRequest, accountDeductionDetails, refundDeductionDetailInDb, account, accountAmountTypes);
            } finally {
                DistributedLockUtil.unlock(merAccountLock);
            }
        } finally {
            DistributedLockUtil.unlock(accountLock);
        }

    }

    private boolean hasRefunded(RefundRequest refundRequest, List<AccountDeductionDetail> refundDeductionDetailInDb) {
        if (!CollectionUtils.isEmpty(refundDeductionDetailInDb)) {
            String transNoInDb = refundDeductionDetailInDb.get(0).getTransNo();
            BigDecimal refundedAmount = refundDeductionDetailInDb.stream()
                    .map(AccountDeductionDetail::getTransAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            List<AccountDeductionDetail> paidDetails = accountDeductionDetailDao
                    .queryByTransNoAndTransType(refundRequest.getOriginalTransNo(), WelfareConstant.TransType.CONSUME.code());
            BigDecimal paidAmount = paidDetails.stream()
                    .map(AccountDeductionDetail::getTransAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            if (refundedAmount.add(refundRequest.getAmount()).compareTo(paidAmount) > 0) {
                throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "退款总额不能大于已付款金额", null);
            }
            if (refundRequest.getTransNo().equals(transNoInDb)) {
                RefundRequest refundRequestInDb = queryResult(transNoInDb);
                log.warn("交易已经处理过，直接返回处理结果:{}", JSON.toJSONString(refundRequestInDb));
                BeanUtils.copyProperties(refundRequestInDb, refundRequest);
                return true;
            }

        }
        return false;
    }

    private void refund(RefundRequest refundRequest,
                        List<AccountDeductionDetail> paidDeductionDetails,
                        List<AccountDeductionDetail> refundDeductionDetail,
                        Account account,
                        List<AccountAmountType> accountAmountTypes) {
        Map<String, AccountAmountType> accountAmountTypeMap = accountAmountTypes.stream()
                .collect(Collectors.toMap(AccountAmountType::getMerAccountTypeCode, type -> type));
        BigDecimal paidAmount = paidDeductionDetails.stream()
                .map(AccountDeductionDetail::getTransAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalToRefundAmount = refundRequest.getAmount();
        int compareTo = paidAmount.compareTo(totalToRefundAmount);
        List<RefundOperation> refundOperations;
        if (compareTo < 0) {
            throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "退款金额大于付款金额:" + totalToRefundAmount + ":" + paidAmount, null);
        } else if (compareTo == 0) {
            refundOperations = fullyRefund(refundRequest, paidDeductionDetails, account, accountAmountTypes, accountAmountTypeMap);
        } else {
            refundOperations = partlyRefund(paidDeductionDetails, accountAmountTypes, refundRequest, account);
        }
        accountDeductionDetailDao.updateBatchById(paidDeductionDetails);
        saveDetails(refundOperations, accountAmountTypes, account);
        refundRequest.setAccountBalance(account.getAccountBalance());
        refundRequest.setAccountCredit(account.getSurplusQuota());
        refundRequest.setAccountCode(account.getAccountCode());
        refundRequest.setAccountName(account.getAccountName());
        refundRequest.setPhone(account.getPhone());
        refundRequest.setMerCode(account.getMerCode());
        refundRequest.setRefundStatus(WelfareConstant.AsyncStatus.SUCCEED.code());
    }

    private List<RefundOperation> partlyRefund(List<AccountDeductionDetail> accountDeductionDetails, List<AccountAmountType> accountAmountTypes, RefundRequest refundRequest, Account account) {
        List<RefundOperation> refundOperations = new ArrayList<>();
        BigDecimal remainingRefundAmount = refundRequest.getAmount();
        Map<String, AccountDeductionDetail> groupedPaidDetail = accountDeductionDetails.stream()
                //已经全额退款的，不再处理退款
                .filter(paidDetail -> paidDetail.getTransAmount().compareTo(paidDetail.getReversedAmount()) > 0)
                .collect(Collectors.toMap(AccountDeductionDetail::getMerAccountType, detail -> detail));
        BigDecimal refundedAmount = BigDecimal.ZERO;
        AccountBillDetail tmpAccountBillDetail = accountBillDetailDao.getOneByTransNoAndTransType(refundRequest.getOriginalTransNo(), WelfareConstant.TransType.CONSUME.code());
        Assert.notNull(tmpAccountBillDetail,"未找到正向支付明细");
        for (AccountAmountType accountAmountType : accountAmountTypes) {
            AccountDeductionDetail accountDeductionDetail = groupedPaidDetail.get(accountAmountType.getMerAccountTypeCode());
            if (accountDeductionDetail == null || remainingRefundAmount.compareTo(BigDecimal.ZERO) == 0) {
                //这个子账户在支付的时候没有扣款金额
                continue;
            }
            BigDecimal thisTypeMaxRefundAmount = accountDeductionDetail.getTransAmount().subtract(accountDeductionDetail.getReversedAmount());
            BigDecimal subtract = thisTypeMaxRefundAmount.subtract(remainingRefundAmount);
            BigDecimal thisAccountTypeRefundAmount;
            if (subtract.compareTo(BigDecimal.ZERO) > 0) {
                thisAccountTypeRefundAmount = remainingRefundAmount;
                remainingRefundAmount = BigDecimal.ZERO;
            } else {
                thisAccountTypeRefundAmount = thisTypeMaxRefundAmount;
                remainingRefundAmount = remainingRefundAmount.subtract(thisAccountTypeRefundAmount);
            }
            accountAmountType.setAccountBalance(accountAmountType.getAccountBalance().add(thisAccountTypeRefundAmount));
            refundedAmount = refundedAmount.add(thisAccountTypeRefundAmount);
            AccountDeductionDetail refundDeductionDetail = toRefundDeductionDetail(accountDeductionDetail, refundRequest, thisAccountTypeRefundAmount);
            AccountBillDetail refundBillDetail = toRefundBillDetail(refundDeductionDetail, accountAmountTypes,tmpAccountBillDetail.getOrderChannel());

            operateMerchantCredit(account, refundDeductionDetail);
            RefundOperation refundOperation = RefundOperation.of(refundBillDetail, refundDeductionDetail);
            refundOperations.add(refundOperation);
            int refundCompare = refundedAmount.compareTo(remainingRefundAmount);
            if (refundCompare == 0) {
                break;
            }
        }
        if (remainingRefundAmount.compareTo(BigDecimal.ZERO) != 0) {
            throw new BusiException(ExceptionCode.UNKNOWON_EXCEPTION, "系统异常，退款金额计算错误", null);
        }
        return refundOperations;
    }

    private List<RefundOperation> fullyRefund(RefundRequest refundRequest, List<AccountDeductionDetail> accountDeductionDetails, Account account, List<AccountAmountType> accountAmountTypes, Map<String, AccountAmountType> accountAmountTypeMap) {
        List<RefundOperation> refundOperations;
        AccountBillDetail tmpAccountBillDetail = accountBillDetailDao.getOneByTransNoAndTransType(refundRequest.getOriginalTransNo(), WelfareConstant.TransType.CONSUME.code());
        Assert.notNull(tmpAccountBillDetail,"未找到正向支付明细");
        refundOperations = accountDeductionDetails.stream()
                .map(paymentDeductionDetail -> toRefundDeductionDetail(
                        paymentDeductionDetail,
                        refundRequest,
                        paymentDeductionDetail.getTransAmount())
                ).map(refundDeductionDetail -> {
                    AccountAmountType accountAmountType = accountAmountTypeMap.get(refundDeductionDetail.getMerAccountType());
                    accountAmountType.setAccountBalance(accountAmountType.getAccountBalance().add(refundDeductionDetail.getTransAmount()));
                    AccountBillDetail refundBillDetail = toRefundBillDetail(refundDeductionDetail, accountAmountTypes, tmpAccountBillDetail.getOrderChannel());
                    operateMerchantCredit(account, refundDeductionDetail);
                    return RefundOperation.of(refundBillDetail, refundDeductionDetail);
                }).collect(Collectors.toList());
        return refundOperations;
    }

    private void operateMerchantCredit(Account account, AccountDeductionDetail refundDeductionDetail) {
        String storeCode = refundDeductionDetail.getStoreCode();
        SupplierStore supplierStore = supplierStoreService.getSupplierStoreByStoreCode(storeCode);
        if(Objects.equals(account.getMerCode(),supplierStore.getMerCode())){
            //用户所属商户和门店的商户是同一个，表示是在自营消费的退款，不操作商家
            return;
        }
        List<MerchantAccountOperation> merchantAccountOperations = merchantCreditService.increaseAccountType(
                account.getMerCode(),
                WelfareConstant.MerCreditType.REMAINING_LIMIT,
                refundDeductionDetail.getTransAmount(),
                refundDeductionDetail.getTransNo(),
                WelfareConstant.TransType.REFUND.code()
        );
        BigDecimal currentBalanceOperated = merchantAccountOperations.stream().map(MerchantAccountOperation::getMerchantBillDetail)
                .filter(detail -> WelfareConstant.MerCreditType.CURRENT_BALANCE.code().equals(detail.getBalanceType()))
                .map(MerchantBillDetail::getTransAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal remainingLimitOperated = merchantAccountOperations.stream().map(MerchantAccountOperation::getMerchantBillDetail)
                .filter(detail -> WelfareConstant.MerCreditType.REMAINING_LIMIT.code().equals(detail.getBalanceType()))
                .map(MerchantBillDetail::getTransAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

        refundDeductionDetail.setMerDeductionAmount(currentBalanceOperated);
        refundDeductionDetail.setMerDeductionCreditAmount(remainingLimitOperated);
    }

    @Override
    public RefundRequest queryResult(String transNo) {
        List<AccountDeductionDetail> accountDeductionDetails = accountDeductionDetailDao.queryByTransNoAndTransType(
                transNo,
                WelfareConstant.TransType.REFUND.code());
        RefundRequest refundRequest = new RefundRequest();
        refundRequest.setTransNo(transNo);
        if (CollectionUtils.isEmpty(accountDeductionDetails)) {
            refundRequest.setRefundStatus(WelfareConstant.AsyncStatus.FAILED.code());
        } else {
            AccountDeductionDetail detail = accountDeductionDetails.get(0);
            refundRequest.setRefundStatus(WelfareConstant.AsyncStatus.SUCCEED.code());
            refundRequest.setRefundDate(detail.getTransTime());
            refundRequest.setOriginalTransNo(detail.getRelatedTransNo());
            BigDecimal amount = accountDeductionDetails.stream()
                    .map(AccountDeductionDetail::getTransAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            refundRequest.setAmount(amount);
            Account account = accountService.getByAccountCode(detail.getAccountCode());
            refundRequest.setAccountMerCode(account.getMerCode());
            refundRequest.setAccountBalance(account.getAccountBalance());
            refundRequest.setAccountCredit(account.getSurplusQuota());
            refundRequest.setAccountCode(account.getAccountCode());
            refundRequest.setAccountName(account.getAccountName());
            refundRequest.setPhone(account.getPhone());
            refundRequest.setMerCode(account.getMerCode());
        }
        return refundRequest;
    }

    private void saveDetails(List<RefundOperation> refundOperations,
                             List<AccountAmountType> accountAmountTypes,
                             Account account) {
        List<AccountBillDetail> refundBillDetails = refundOperations.stream()
                .map(RefundOperation::getRefundBillDetail)
                .collect(Collectors.toList());
        List<AccountDeductionDetail> refundDeductionDetails = refundOperations.stream()
                .map(RefundOperation::getRefundDeductionDetail)
                .collect(Collectors.toList());
        BigDecimal accountBalance = AccountAmountDO.calculateAccountBalance(accountAmountTypes);
        BigDecimal accountCreditBalance = AccountAmountDO.calculateAccountCredit(accountAmountTypes);
        account.setAccountBalance(accountBalance);
        account.setSurplusQuota(accountCreditBalance);
        accountDao.updateById(account);
        accountBillDetailDao.saveBatch(refundBillDetails);
        accountAmountTypeDao.updateBatchById(accountAmountTypes);
        accountDeductionDetailDao.saveBatch(refundDeductionDetails);
    }


    private AccountBillDetail toRefundBillDetail(AccountDeductionDetail refundDeductionDetail,
                                                 List<AccountAmountType> accountAmountTypes, String orderChannel) {
        AccountBillDetail refundDetail = new AccountBillDetail();
        refundDetail.setTransType(WelfareConstant.TransType.REFUND.code());
        refundDetail.setTransTime(refundDeductionDetail.getTransTime());
        refundDetail.setTransNo(refundDeductionDetail.getTransNo());
        refundDetail.setChannel(refundDeductionDetail.getChanel());
        refundDetail.setStoreCode(refundDeductionDetail.getStoreCode());
        refundDetail.setPos(refundDeductionDetail.getPos());
        refundDetail.setOrderChannel(orderChannel);
        refundDetail.setTransAmount(refundDeductionDetail.getTransAmount());
        refundDetail.setAccountCode(refundDeductionDetail.getAccountCode());
        BigDecimal accountBalance = accountAmountTypes.stream()
                .filter(accountAmountType -> !SURPLUS_QUOTA.code().equals(accountAmountType.getMerAccountTypeCode()))
                .map(AccountAmountType::getAccountBalance).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal accountCreditBalance = accountAmountTypes.stream()
                .filter(accountAmountType -> SURPLUS_QUOTA.code().equals(accountAmountType.getMerAccountTypeCode()))
                .map(AccountAmountType::getAccountBalance).reduce(BigDecimal.ZERO, BigDecimal::add);
        refundDetail.setAccountBalance(accountBalance);
        refundDetail.setSurplusQuota(accountCreditBalance);
        return refundDetail;
    }


    /**
     * 单条退款
     *
     * @param accountDeductionDetail
     * @param refundRequest
     * @param refundAmountForThis    这条退款金额多少
     * @return
     */
    private AccountDeductionDetail toRefundDeductionDetail(AccountDeductionDetail accountDeductionDetail,
                                                           RefundRequest refundRequest, BigDecimal refundAmountForThis) {
        AccountDeductionDetail refundDeductionDetail = new AccountDeductionDetail();
        BeanUtils.copyProperties(accountDeductionDetail, refundDeductionDetail);
        refundDeductionDetail.setTransAmount(refundAmountForThis);
        //支付的流水已退款金额
        accountDeductionDetail.setReversedAmount(accountDeductionDetail.getReversedAmount().add(refundAmountForThis));
        refundDeductionDetail.setReversedAmount(BigDecimal.ZERO);
        refundDeductionDetail.setTransType(WelfareConstant.TransType.REFUND.code());
        refundDeductionDetail.setTransTime(refundRequest.getRefundDate());
        refundDeductionDetail.setTransNo(refundRequest.getTransNo());
        refundDeductionDetail.setRelatedTransNo(accountDeductionDetail.getTransNo());
        //保存时自动赋值
        refundDeductionDetail.setId(null);
        refundDeductionDetail.setVersion(0);
        return refundDeductionDetail;
    }
}
