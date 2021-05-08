package com.welfare.service.payment;

import com.alibaba.excel.util.CollectionUtils;
import com.welfare.common.constants.RedisKeyConstant;
import com.welfare.common.constants.WelfareConstant;
import com.welfare.common.exception.BizAssert;
import com.welfare.common.exception.BizException;
import com.welfare.common.exception.ExceptionCode;
import com.welfare.common.util.DistributedLockUtil;
import com.welfare.persist.dao.*;
import com.welfare.persist.entity.*;
import com.welfare.service.AccountAmountTypeService;
import com.welfare.service.AccountService;
import com.welfare.service.MerchantCreditService;
import com.welfare.service.SupplierStoreService;
import com.welfare.service.dto.RefundRequest;
import com.welfare.service.operator.merchant.domain.MerchantAccountOperation;
import com.welfare.service.operator.payment.domain.AccountAmountDO;
import com.welfare.service.operator.payment.domain.RefundOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.welfare.common.constants.RedisKeyConstant.MER_ACCOUNT_TYPE_OPERATE;
import static com.welfare.common.constants.WelfareConstant.MerAccountTypeCode.SURPLUS_QUOTA;
import static com.welfare.common.constants.WelfareConstant.MerAccountTypeCode.SURPLUS_QUOTA_OVERPAY;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 3/26/2021
 */
@RequiredArgsConstructor
@Slf4j
@Component
public class WelfareRefundOperator implements IRefundOperator{
    private final AccountDeductionDetailDao accountDeductionDetailDao;
    private final AccountBillDetailDao accountBillDetailDao;
    private final AccountService accountService;
    private final AccountAmountTypeDao accountAmountTypeDao;
    private final AccountDao accountDao;
    private final MerchantCreditService merchantCreditService;
    private final AccountAmountTypeService accountAmountTypeService;
    private final SupplierStoreService supplierStoreService;
    private final AccountAmountTypeGroupDao accountAmountTypeGroupDao;

    @Override
    public void refund(RefundRequest refundRequest, List<AccountDeductionDetail> refundDeductionDetailInDb, List<AccountDeductionDetail> accountDeductionDetails,Long accountCode) {
        String lockKey = RedisKeyConstant.ACCOUNT_AMOUNT_TYPE_OPERATE + accountCode;
        RLock accountLock = DistributedLockUtil.lockFairly(lockKey);
        try {
            Account account = accountService.getByAccountCode(accountCode);
            refundRequest.setAccountMerCode(account.getMerCode());
            log.error("accountInfo:{}",account);
            List<AccountAmountDO> accountAmountDOList = accountAmountTypeService.queryAccountAmountDO(account);
            Assert.notEmpty(accountAmountDOList,"员工没有配置福利类型");
            //按照deductionOrder逆序
            accountAmountDOList.sort(Comparator.comparing(x -> -1 * x.getMerchantAccountType().getDeductionOrder()));
            List<AccountAmountType> accountAmountTypes = accountAmountDOList.stream().map(AccountAmountDO::getAccountAmountType)
                    .collect(Collectors.toList());
            RLock merAccountLock = DistributedLockUtil.lockFairly(MER_ACCOUNT_TYPE_OPERATE + ":" + account.getMerCode());
            try {
                doRefund(refundRequest, accountDeductionDetails, account, accountAmountTypes);
            } finally {
                DistributedLockUtil.unlock(merAccountLock);
            }
        } finally {
            DistributedLockUtil.unlock(accountLock);
        }
    }



    private void doRefund(RefundRequest refundRequest,
                          List<AccountDeductionDetail> paidDeductionDetails,
                          Account account,
                          List<AccountAmountType> accountAmountTypes) {
        BigDecimal paidAmount = paidDeductionDetails.stream()
                .map(AccountDeductionDetail::getTransAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalToRefundAmount = refundRequest.getAmount();
        int compareTo = paidAmount.compareTo(totalToRefundAmount);
        List<RefundOperation> refundOperations;
        if (compareTo < 0) {
            throw new BizException(ExceptionCode.ILLEGALITY_ARGUMENTS, "退款金额大于付款金额:" + totalToRefundAmount + ":" + paidAmount, null);
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
        BizAssert.notEmpty(groupedPaidDetail.entrySet(),ExceptionCode.REFUND_MORE_THAN_PAID);
        BigDecimal refundedAmount = BigDecimal.ZERO;
        BigDecimal remainingRefundAmount = refundRequest.getAmount();
        AccountBillDetail tmpAccountBillDetail = accountBillDetailDao.getOneByTransNoAndTransTypeAndOrderNo(refundRequest.getOriginalTransNo(), WelfareConstant.TransType.CONSUME.code(), refundRequest.getOrderNo());
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
            //最多允许退款到授信的金额
            BigDecimal maxRefundToSurplusQuota = maxQuota.subtract(currentSurplusQuota);
            //本应该退款到授信的金额(依赖于请求的退款金额和个人授信在付款的时候扣款金额综合得到)
            BigDecimal shouldRefundToSurplusQuotaAmount = refundRequest.getAmount()
                    .subtract(surPlusQuotaDeductionDetail.getTransAmount()).subtract(surPlusQuotaDeductionDetail.getReversedAmount()).compareTo(BigDecimal.ZERO)>0
                    ?surPlusQuotaDeductionDetail.getTransAmount().subtract(surPlusQuotaDeductionDetail.getReversedAmount())
                    :refundRequest.getAmount();
            //超出部分溢缴款金额
            BigDecimal surplusQuotaOverpayAmount = shouldRefundToSurplusQuotaAmount.subtract(maxRefundToSurplusQuota);
            if(surplusQuotaOverpayAmount.compareTo(BigDecimal.ZERO)>0){
                if(maxRefundToSurplusQuota.compareTo(BigDecimal.ZERO)>0){
                    //有需要退款到授信额度的，才走授信额度退款
                    AccountDeductionDetail surplusRefundDeductionDetail = toRefundDeductionDetail(surPlusQuotaDeductionDetail, refundRequest, maxRefundToSurplusQuota,surplusType);
                    //设置退款金额为需要退款到授信额度的值
                    surplusRefundDeductionDetail.setTransAmount(maxRefundToSurplusQuota);
                    refundedAmount = refundedAmount.add(maxRefundToSurplusQuota);
                    remainingRefundAmount = remainingRefundAmount.subtract(maxRefundToSurplusQuota);
                    AccountBillDetail refundBillDetail = toRefundBillDetail(surplusRefundDeductionDetail, accountAmountTypes, tmpAccountBillDetail.getOrderChannel());
                    surplusType.setAccountBalance(surplusType.getAccountBalance().add(maxRefundToSurplusQuota));
                    Assert.isTrue(surplusType.getAccountBalance().compareTo(maxQuota) == 0,"退款金额异常，请联系管理员。");
                    operateMerchantCredit(account, surplusRefundDeductionDetail);
                    RefundOperation refundOperation = RefundOperation.of(refundBillDetail, surplusRefundDeductionDetail,null);
                    refundOperations.add(refundOperation);
                }
                Assert.notNull(surplusOverpayType,"该用户不存在溢缴款账户,请检查配置");
                AccountDeductionDetail surplusOverpayRefundDeductionDetail = toRefundDeductionDetail(surPlusQuotaDeductionDetail, refundRequest, surplusQuotaOverpayAmount, surplusOverpayType);
                AccountBillDetail overpayRefundBillDetail = toRefundBillDetail(surplusOverpayRefundDeductionDetail, accountAmountTypes, tmpAccountBillDetail.getOrderChannel());
                surplusOverpayType.setAccountBalance(surplusOverpayType.getAccountBalance().add(surplusQuotaOverpayAmount));
                refundedAmount = refundedAmount.add(surplusQuotaOverpayAmount);
                remainingRefundAmount = remainingRefundAmount.subtract(surplusQuotaOverpayAmount);
                operateMerchantCredit(account,surplusOverpayRefundDeductionDetail);
                RefundOperation overpayRefundOperation = RefundOperation.of(overpayRefundBillDetail, surplusOverpayRefundDeductionDetail,null);
                refundOperations.add(overpayRefundOperation);
                isSurplusHandled = true;
            }
        }

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
     * @param accountAmountTypes 福利类型列表
     * @param refundRequest 退款请求
     * @param account 账户
     * @param refundOperations 退款操作列表
     * @param remainingRefundAmount 剩余需要退款的金额
     * @param groupedPaidDetail 分组后的付款明细
     * @param refundedAmount 已经退款的金额
     * @param tmpAccountBillDetail 第一条付款明细
     * @param isSurplusHandled 是否已经处理了授信退款
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
            AccountAmountTypeGroup accountAmountTypeGroup = null;
            if(accountAmountType.getJoinedGroup()){
                //如果福利账户有分组，则需要退款到分组账户
                accountAmountTypeGroup = accountAmountTypeGroupDao.getById(accountAmountType.getAccountAmountTypeGroupId());
                accountAmountTypeGroup.setBalance(accountAmountTypeGroup.getBalance().add(thisAccountTypeRefundAmount));
            }else{
                accountAmountType.setAccountBalance(accountAmountType.getAccountBalance().add(thisAccountTypeRefundAmount));
            }
            refundedAmount = refundedAmount.add(thisAccountTypeRefundAmount);
            AccountDeductionDetail refundDeductionDetail = toRefundDeductionDetail(accountDeductionDetail, refundRequest, thisAccountTypeRefundAmount,accountAmountType);
            AccountBillDetail refundBillDetail = toRefundBillDetail(refundDeductionDetail, accountAmountTypes, tmpAccountBillDetail.getOrderChannel());
            operateMerchantCredit(account, refundDeductionDetail);
            RefundOperation refundOperation = RefundOperation.of(refundBillDetail, refundDeductionDetail,accountAmountTypeGroup);
            refundOperations.add(refundOperation);
            int refundCompare = refundedAmount.compareTo(refundRequest.getAmount());
            if (refundCompare == 0) {
                break;
            }
          }
        if (remainingRefundAmount.compareTo(BigDecimal.ZERO) != 0) {
            throw new BizException(ExceptionCode.UNKNOWN_EXCEPTION, "系统异常，退款金额计算错误", null);
        }
    }


    private void operateMerchantCredit(Account account, AccountDeductionDetail refundDeductionDetail) {
        String storeCode = refundDeductionDetail.getStoreCode();
        SupplierStore supplierStore = supplierStoreService.getSupplierStoreByStoreCode(storeCode);
        if(Objects.equals(account.getMerCode(),supplierStore.getMerCode())){
            //用户所属商户和门店的商户是同一个，表示是在自营消费的退款，不操作商家
            return;
        }
        WelfareConstant.MerCreditType merCreditType;
        if(refundDeductionDetail.getMerAccountType().equals(WelfareConstant.MerAccountTypeCode.WHOLESALE_PROCUREMENT.code())){
            merCreditType = WelfareConstant.MerCreditType.WHOLESALE_CREDIT;
        }else{
            merCreditType = WelfareConstant.MerCreditType.REMAINING_LIMIT;
        }
        List<MerchantAccountOperation> merchantAccountOperations = merchantCreditService.increaseAccountType(
                account.getMerCode(),
                merCreditType,
                refundDeductionDetail.getTransAmount(),
                refundDeductionDetail.getTransNo(),
                WelfareConstant.TransType.REFUND.code()
        );
        refundDeductionDetail.setMerDeductionAmount(MerchantAccountOperation.getCurrentBalanceOperated(merchantAccountOperations));
        refundDeductionDetail.setMerDeductionCreditAmount(MerchantAccountOperation.getRemainingLimitOperated(merchantAccountOperations));
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
        List<AccountAmountTypeGroup> accountAmountTypeGroups = refundOperations.stream()
                .map(RefundOperation::getAccountAmountTypeGroup)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        if(!CollectionUtils.isEmpty(accountAmountTypeGroups)){
            accountAmountTypeGroupDao.updateBatchById(accountAmountTypeGroups);
        }
        AccountAmountDO.updateAccountAfterOperated(account,accountAmountTypes);
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
        refundDetail.setPaymentChannel(refundDeductionDetail.getPaymentChannel());
        BigDecimal accountBalance = accountAmountTypes.stream()
                .filter(accountAmountType -> !(SURPLUS_QUOTA.code().equals(accountAmountType.getMerAccountTypeCode())
                        || SURPLUS_QUOTA_OVERPAY.code().equals(accountAmountType.getMerAccountTypeCode())))
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
     * @return 退款的明细
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
