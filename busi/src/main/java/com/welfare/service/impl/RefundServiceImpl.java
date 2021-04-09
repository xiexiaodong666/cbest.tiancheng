package com.welfare.service.impl;

import com.alibaba.fastjson.JSON;
import com.welfare.common.annotation.DistributedLock;
import com.welfare.common.constants.WelfareConstant;
import com.welfare.common.exception.BizException;
import com.welfare.common.exception.ExceptionCode;
import com.welfare.common.util.SpringBeanUtils;
import com.welfare.persist.dao.AccountDeductionDetailDao;
import com.welfare.persist.entity.Account;
import com.welfare.persist.entity.AccountDeductionDetail;
import com.welfare.service.AccountService;
import com.welfare.service.RefundService;
import com.welfare.service.dto.RefundRequest;
import com.welfare.service.enums.PaymentChannelOperatorEnum;
import com.welfare.service.payment.IRefundOperator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

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
    private final AccountService accountService;
    @Override
    @Transactional(rollbackFor = Exception.class)
    @DistributedLock(lockPrefix = "e-welfare-refund::", lockKey = "#refundRequest.originalTransNo")
    public void handleRefundRequest(RefundRequest refundRequest) {
        String originalTransNo = refundRequest.getOriginalTransNo();
        List<AccountDeductionDetail> paymentDeductionDetailInDb = accountDeductionDetailDao
                .queryByTransNoAndTransType(refundRequest.getTransNo(), WelfareConstant.TransType.CONSUME.code());
        Assert.isTrue(CollectionUtils.isEmpty(paymentDeductionDetailInDb),"退款交易单号不能和付款的交易单号一样。");

        List<AccountDeductionDetail> accountDeductionDetails = accountDeductionDetailDao.queryByTransNoAndTransType(
                originalTransNo,
                WelfareConstant.TransType.CONSUME.code()
        );
        Assert.isTrue(!CollectionUtils.isEmpty(accountDeductionDetails), "未找到正向支付流水");
        AccountDeductionDetail first = accountDeductionDetails.get(0);
        Long accountCode = first.getAccountCode();
        //找到支付渠道对应的退款operator,执行退款
        PaymentChannelOperatorEnum paymentChannelOperatorEnum = PaymentChannelOperatorEnum.findByPaymentChannelStr(first.getPaymentChannel());
        IRefundOperator refundOperator = SpringBeanUtils.getBean(paymentChannelOperatorEnum.refundOperator());

        List<AccountDeductionDetail> refundDeductionDetailsInDb = accountDeductionDetailDao
                .queryByRelatedTransNoAndTransType(refundRequest.getOriginalTransNo(), WelfareConstant.TransType.REFUND.code());
        if (hasRefunded(refundRequest, refundDeductionDetailsInDb)) {
            return;
        }
        refundOperator.refund(refundRequest,refundDeductionDetailsInDb,accountDeductionDetails,accountCode);
    }

    /**
     * 是否已经处理过退款
     * @param refundRequest
     * @param refundDeductionDetailInDb
     * @return
     */
    private boolean hasRefunded(RefundRequest refundRequest, List<AccountDeductionDetail> refundDeductionDetailInDb) {
        if (!CollectionUtils.isEmpty(refundDeductionDetailInDb)) {
            List<String> transNoInDbs = refundDeductionDetailInDb.stream().map(AccountDeductionDetail::getTransNo).collect(Collectors.toList());
            BigDecimal refundedAmount = refundDeductionDetailInDb.stream()
                    .map(AccountDeductionDetail::getTransAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            List<AccountDeductionDetail> paidDetails = accountDeductionDetailDao
                    .queryByTransNoAndTransType(refundRequest.getOriginalTransNo(), WelfareConstant.TransType.CONSUME.code());
            BigDecimal paidAmount = paidDetails.stream()
                    .map(AccountDeductionDetail::getTransAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            if (refundedAmount.add(refundRequest.getAmount()).compareTo(paidAmount) > 0) {
                throw new BizException(ExceptionCode.ILLEGALITY_ARGURMENTS, "退款总额不能大于已付款金额", null);
            }
            if (transNoInDbs.contains(refundRequest.getTransNo())) {
                RefundRequest refundRequestInDb = queryResult(refundRequest.getTransNo());
                log.warn("交易已经处理过，直接返回处理结果:{}", JSON.toJSONString(refundRequestInDb));
                BeanUtils.copyProperties(refundRequestInDb, refundRequest);
                return true;
            }

        }
        return false;
    }

    /**
     * 查询退款结果
     * @param transNo
     * @return
     */
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
}
