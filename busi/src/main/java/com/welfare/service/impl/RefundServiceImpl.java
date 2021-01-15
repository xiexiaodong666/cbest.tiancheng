package com.welfare.service.impl;

import com.welfare.common.constants.WelfareConstant;
import com.welfare.common.exception.BusiException;
import com.welfare.common.exception.ExceptionCode;
import com.welfare.persist.dao.AccountAmountTypeDao;
import com.welfare.persist.dao.AccountBillDetailDao;
import com.welfare.persist.dao.AccountDao;
import com.welfare.persist.dao.AccountDeductionDetailDao;
import com.welfare.persist.entity.Account;
import com.welfare.persist.entity.AccountAmountType;
import com.welfare.persist.entity.AccountBillDetail;
import com.welfare.persist.entity.AccountDeductionDetail;
import com.welfare.service.*;
import com.welfare.service.dto.RefundRequest;
import com.welfare.service.operator.payment.domain.AccountAmountDO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.welfare.common.constants.RedisKeyConstant.MER_ACCOUNT_TYPE_OPERATE;
import static com.welfare.common.constants.WelfareConstant.MerAccountTypeCode.SURPLUS_QUOTA;

/**
 * Description:
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
    private final RedissonClient redissonClient;
    private final AccountService accountService;
    private final AccountAmountTypeDao accountAmountTypeDao;
    private final AccountDao accountDao;
    private final AccountAmountTypeService accountAmountTypeService;
    private final MerchantCreditService merchantCreditService;
    @Override
    public void handleRefundRequest(RefundRequest refundRequest) {
        String originalTransNo = refundRequest.getOriginalTransNo();
        List<AccountDeductionDetail> accountDeductionDetails = accountDeductionDetailDao.queryByTransNo(originalTransNo);
        Assert.isTrue(!CollectionUtils.isEmpty(accountDeductionDetails),"未找到正向支付流水");
        AccountDeductionDetail first = accountDeductionDetails.get(0);
        Account account = accountService.getByAccountCode(first.getAccountCode());
        List<AccountBillDetail> accountBillDetails = accountBillDetailDao.queryByTransNo(originalTransNo);
        List<AccountAmountType> accountAmountTypes = accountAmountTypeDao.queryByAccountCode(account.getAccountCode());
        RLock merAccountLock = redissonClient.getFairLock(MER_ACCOUNT_TYPE_OPERATE + ":" + account.getMerCode());
        merAccountLock.lock();
        try{
            refund(refundRequest, accountDeductionDetails, account, accountBillDetails, accountAmountTypes);
        }finally {
            merAccountLock.unlock();
        }
    }

    private void refund(RefundRequest refundRequest, List<AccountDeductionDetail> accountDeductionDetails, Account account, List<AccountBillDetail> accountBillDetails, List<AccountAmountType> accountAmountTypes) {
        String lockKey = "account:" + account.getAccountCode();
        RLock accountLock = redissonClient.getFairLock(lockKey);
        accountLock.lock();
        try{
            List<AccountDeductionDetail> refundDeductionDetails = accountDeductionDetails.stream()
                    .map(deduction -> {
                        merchantCreditService.increaseAccountType(
                                account.getMerCode(),
                                WelfareConstant.MerCreditType.REMAINING_LIMIT,
                                deduction.getTransAmount(),
                                deduction.getTransNo()
                        );
                        return toRefundDeductionDetail(deduction, refundRequest);
                    })
                    .collect(Collectors.toList());
            List<AccountBillDetail> refundBillDetails = accountBillDetails.stream()
                    .map(detail -> toRefundBillDetail(detail, refundRequest))
                    .collect(Collectors.toList());
            Map<String, List<AccountDeductionDetail>> refundDeductionMap = refundDeductionDetails.stream()
                    .collect(Collectors.groupingBy(AccountDeductionDetail::getMerAccountType));
            accountAmountTypes.forEach(accountAmountType -> {
                List<AccountDeductionDetail> deductionDetails = refundDeductionMap.get(accountAmountType.getMerAccountTypeCode());
                BigDecimal refundAmount = deductionDetails.stream()
                        .map(AccountDeductionDetail::getTransAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
                accountAmountType.setAccountBalance(accountAmountType.getAccountBalance().add(refundAmount));
            });
            saveDetails(refundDeductionDetails,refundBillDetails, accountAmountTypes, account);
        }finally {
            accountLock.unlock();
        }
    }

    @Override
    public RefundRequest queryByTransNo(String transNo) {
        return null;
    }

    private void saveDetails(List<AccountDeductionDetail> refundDeductionDetails,
                             List<AccountBillDetail> refundBillDetails,
                             List<AccountAmountType> accountAmountTypes,
                             Account account) {
        accountBillDetailDao.saveBatch(refundBillDetails);
        accountAmountTypeDao.updateBatchById(accountAmountTypes);
        accountDeductionDetailDao.saveBatch(refundDeductionDetails);
        BigDecimal balanceSum = accountAmountTypeService.sumBalanceExceptSurplusQuota(account.getAccountCode());
        AccountAmountType accountAmountType = accountAmountTypeService.queryOne(account.getAccountCode(), SURPLUS_QUOTA.code());
        account.setAccountBalance(balanceSum);
        account.setSurplusQuota(accountAmountType.getAccountBalance());
        accountDao.updateById(account);

    }



    private AccountBillDetail toRefundBillDetail(AccountBillDetail detail, RefundRequest refundRequest) {
        AccountBillDetail refundDetail = new AccountBillDetail();
        BeanUtils.copyProperties(detail,refundDetail);
        refundDetail.setId(null);
        refundDetail.setVersion(0);
        refundDetail.setTransType(WelfareConstant.TransType.REFUND.code());
        refundDetail.setTransTime(refundRequest.getRefundDate());
        refundDetail.setTransNo(refundRequest.getTransNo());
        return refundDetail;
    }



    private AccountDeductionDetail toRefundDeductionDetail(AccountDeductionDetail accountDeductionDetail,
                                                           RefundRequest refundRequest){
        AccountDeductionDetail refundDeductionDetail = new AccountDeductionDetail();
        BeanUtils.copyProperties(accountDeductionDetail,refundDeductionDetail);
        refundDeductionDetail.setTransType(WelfareConstant.TransType.REFUND.code());
        refundDeductionDetail.setTransTime(refundRequest.getRefundDate());
        refundDeductionDetail.setTransNo(refundRequest.getTransNo());
        refundDeductionDetail.setRelatedTransNo(accountDeductionDetail.getTransNo());
        //保存时自动赋值
        refundDeductionDetail.setCardId(null);
        refundDeductionDetail.setVersion(0);
        return refundDeductionDetail;
    }
}
