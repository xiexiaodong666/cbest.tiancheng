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
import static com.welfare.common.constants.WelfareConstant.MerAccountTypeCode.SURPLUS_QUOTA_OVERPAY;

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
        List<AccountDeductionDetail> paymentDeductionDetailInDb = accountDeductionDetailDao
                .queryByRelatedTransNoAndTransType(refundRequest.getOriginalTransNo(), WelfareConstant.TransType.CONSUME.code());
        Assert.isTrue(CollectionUtils.isEmpty(paymentDeductionDetailInDb),"退款交易单号不能和付款的交易单号一样。");
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
        Map<String, AccountDeductionDetail> groupedPaidDetail = accountDeductionDetails.stream()
                //已经全额退款的，不再处理退款
                .filter(paidDetail -> paidDetail.getTransAmount().compareTo(paidDetail.getReversedAmount()) > 0)
                .collect(Collectors.toMap(AccountDeductionDetail::getMerAccountType, detail -> detail));
        BigDecimal refundedAmount = BigDecimal.ZERO;
        AccountBillDetail tmpAccountBillDetail = accountBillDetailDao.getOneByTransNoAndTransType(refundRequest.getOriginalTransNo(), WelfareConstant.TransType.CONSUME.code());
        Assert.notNull(tmpAccountBillDetail,"未找到正向支付明细");
        //单独处理个人授信的退款逻辑
        AccountAmountType surplusType = accountAmountTypes.stream()
                .filter(accountAmountType -> SURPLUS_QUOTA.code().equals(accountAmountType.getMerAccountTypeCode()))
                .findFirst().orElse(null);
        AccountAmountType surplusOverpayType = accountAmountTypes.stream()
                .filter(accountAmountType -> SURPLUS_QUOTA_OVERPAY.code().equals(accountAmountType.getMerAccountTypeCode()))
                .findFirst().orElse(null);

        AccountDeductionDetail surPlusQuotaDeductionDetail = groupedPaidDetail.get(SURPLUS_QUOTA.code());
        boolean isSurplusHandled = false;
        if(!Objects.isNull(surplusType) && !Objects.isNull(surPlusQuotaDeductionDetail)){
            BigDecimal currentSurplusQuota = account.getSurplusQuota();
            BigDecimal maxQuota = account.getMaxQuota();
            BigDecimal maxRefundToSurplusQuota = maxQuota.subtract(currentSurplusQuota);
            //应该退款的金额
            BigDecimal shouldRefundAmount = surPlusQuotaDeductionDetail.getTransAmount();
            //溢缴款金额
            BigDecimal surplusQuotaOverpayAmount = shouldRefundAmount.subtract(maxRefundToSurplusQuota);
            if(surplusQuotaOverpayAmount.compareTo(BigDecimal.ZERO)>0){
                surplusType.setAccountBalance(surplusType.getAccountBalance().add(maxRefundToSurplusQuota));
                Assert.isTrue(surplusType.getAccountBalance().compareTo(maxQuota) == 0,"退款金额异常，请联系管理员。");
                if(maxRefundToSurplusQuota.compareTo(BigDecimal.ZERO)>0){
                    //有需要退款到授信额度的，才走授信额度退款
                    AccountDeductionDetail surplusRefundDeductionDetail = toRefundDeductionDetail(surPlusQuotaDeductionDetail, refundRequest, maxRefundToSurplusQuota,surplusType);
                    //设置退款金额为需要退款到授信额度的值
                    surplusRefundDeductionDetail.setTransAmount(maxRefundToSurplusQuota);
                    AccountBillDetail refundBillDetail = toRefundBillDetail(surplusRefundDeductionDetail, accountAmountTypes, tmpAccountBillDetail.getOrderChannel());
                    operateMerchantCredit(account, surplusRefundDeductionDetail);
                    RefundOperation refundOperation = RefundOperation.of(refundBillDetail, surplusRefundDeductionDetail);
                    refundOperations.add(refundOperation);
                }
                AccountDeductionDetail surplusOverpayRefundDeductionDetail = toRefundDeductionDetail(surPlusQuotaDeductionDetail, refundRequest, surplusQuotaOverpayAmount, surplusOverpayType);
                AccountBillDetail overpayRefundBillDetail = toRefundBillDetail(surplusOverpayRefundDeductionDetail, accountAmountTypes, tmpAccountBillDetail.getOrderChannel());
                operateMerchantCredit(account,surplusOverpayRefundDeductionDetail);
                RefundOperation overpayRefundOperation = RefundOperation.of(overpayRefundBillDetail, surplusOverpayRefundDeductionDetail);
                refundOperations.add(overpayRefundOperation);
                isSurplusHandled = true;
            }
        }

        BigDecimal remainingRefundAmount = refundRequest.getAmount();
        handlePartlyRefundCommon(accountAmountTypes,
                refundRequest,
                account,
                refundOperations,
                remainingRefundAmount,
                groupedPaidDetail, refundedAmount, tmpAccountBillDetail,isSurplusHandled);
        return refundOperations;
    }

    /**
     * 除了个人授信的退款逻辑
     * @param accountAmountTypes
     * @param refundRequest
     * @param account
     * @param refundOperations
     * @param remainingRefundAmount
     * @param groupedPaidDetail
     * @param refundedAmount
     * @param tmpAccountBillDetail
     * @param isSurplusHandled
     */
    private void handlePartlyRefundCommon(List<AccountAmountType> accountAmountTypes,
                                          RefundRequest refundRequest,
                                          Account account,
                                          List<RefundOperation> refundOperations,
                                          BigDecimal remainingRefundAmount,
                                          Map<String, AccountDeductionDetail> groupedPaidDetail,
                                          BigDecimal refundedAmount,
                                          AccountBillDetail tmpAccountBillDetail, boolean isSurplusHandled) {
        for (AccountAmountType accountAmountType : accountAmountTypes) {
            if(SURPLUS_QUOTA.code().equals(accountAmountType.getMerAccountTypeCode()) && isSurplusHandled){
                //个人授信已经处理过（适用于有溢缴款的逻辑）,则跳过，否则个人授信额度也按照通用逻辑处理（没有溢缴款）
                continue;
            }
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
            AccountDeductionDetail refundDeductionDetail = toRefundDeductionDetail(accountDeductionDetail, refundRequest, thisAccountTypeRefundAmount,accountAmountType);
            AccountBillDetail refundBillDetail = toRefundBillDetail(refundDeductionDetail, accountAmountTypes, tmpAccountBillDetail.getOrderChannel());
            operateMerchantCredit(account, refundDeductionDetail);
            RefundOperation refundOperation = RefundOperation.of(refundBillDetail, refundDeductionDetail);
            refundOperations.add(refundOperation);
            int refundCompare = refundedAmount.compareTo(remainingRefundAmount);
            if (refundCompare == 0) {
                break;
            }
        }
/*        if (remainingRefundAmount.compareTo(BigDecimal.ZERO) != 0) {
            throw new BusiException(ExceptionCode.UNKNOWON_EXCEPTION, "系统异常，退款金额计算错误", null);
        }*/
    }

    @Deprecated
    /**
     * deprecated，部分退款已经覆盖全额退款的逻辑。且全额退款不适用于溢缴款的逻辑。
     */
    private List<RefundOperation> fullyRefund(RefundRequest refundRequest, List<AccountDeductionDetail> accountDeductionDetails, Account account, List<AccountAmountType> accountAmountTypes, Map<String, AccountAmountType> accountAmountTypeMap) {
        List<RefundOperation> refundOperations;
        AccountBillDetail tmpAccountBillDetail = accountBillDetailDao.getOneByTransNoAndTransType(refundRequest.getOriginalTransNo(), WelfareConstant.TransType.CONSUME.code());
        Assert.notNull(tmpAccountBillDetail,"未找到正向支付明细");
        refundOperations = accountDeductionDetails.stream()
                .map(paymentDeductionDetail -> toRefundDeductionDetail(
                        paymentDeductionDetail,
                        refundRequest,
                        paymentDeductionDetail.getTransAmount(), accountAmountTypeMap.get(paymentDeductionDetail.getMerAccountType()))
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
            refundRequest.setAccountCredit(account.getSurplusQuota().add(account.getSurplusQuotaOverpay()));
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
        BigDecimal accountCreditOverpayBalance = AccountAmountDO.calculateAccountCreditOverpay(accountAmountTypes);
        account.setAccountBalance(accountBalance);
        account.setSurplusQuota(accountCreditBalance);
        account.setSurplusQuotaOverpay(accountCreditOverpayBalance);
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
        refundDetail.setOrderChannel(refundDeductionDetail.getOrderChannel());
        BigDecimal accountBalance = accountAmountTypes.stream()
                .filter(accountAmountType -> !SURPLUS_QUOTA.code().equals(accountAmountType.getMerAccountTypeCode()))
                .map(AccountAmountType::getAccountBalance).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal accountCreditBalance = accountAmountTypes.stream()
                .filter(accountAmountType -> SURPLUS_QUOTA.code().equals(accountAmountType.getMerAccountTypeCode()))
                .map(AccountAmountType::getAccountBalance).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal accountCreditOverpayBalance = accountAmountTypes.stream()
                .filter(accountAmountType -> SURPLUS_QUOTA_OVERPAY.code().equals(accountAmountType.getMerAccountTypeCode()))
                .map(AccountAmountType::getAccountBalance).reduce(BigDecimal.ZERO, BigDecimal::add);
        refundDetail.setAccountBalance(accountBalance);
        refundDetail.setSurplusQuota(accountCreditBalance);
        refundDetail.setSurplusQuotaOverpay(accountCreditOverpayBalance);
        return refundDetail;
    }


    /**
     * 单条退款
     *
     * @param accountDeductionDetail 支付的时候交易明细
     * @param refundRequest 退款请求
     * @param refundAmountForThis    这条退款金额多少
     * @param accountAmountType 操作的福利类型
     * @return
     */
    private AccountDeductionDetail toRefundDeductionDetail(AccountDeductionDetail accountDeductionDetail,
                                                           RefundRequest refundRequest, BigDecimal refundAmountForThis, AccountAmountType accountAmountType) {
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
        refundDeductionDetail.setAccountAmountTypeBalance(accountAmountType.getAccountBalance());
        refundDeductionDetail.setMerAccountType(accountAmountType.getMerAccountTypeCode());
        //保存时自动赋值
        refundDeductionDetail.setId(null);
        refundDeductionDetail.setVersion(0);
        return refundDeductionDetail;
    }
}
