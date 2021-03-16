package com.welfare.service.wolife.impl;

import com.welfare.common.annotation.DistributedLock;
import com.welfare.common.constants.RedisKeyConstant;
import com.welfare.common.constants.WelfareConstant;
import com.welfare.common.enums.PaymentTypeEnum;
import com.welfare.common.util.DistributedLockUtil;
import com.welfare.persist.dao.AccountBillDetailDao;
import com.welfare.persist.dao.AccountDao;
import com.welfare.persist.dao.AccountDeductionDetailDao;
import com.welfare.persist.entity.*;
import com.welfare.service.MerchantCreditService;
import com.welfare.service.dto.RefundRequest;
import com.welfare.service.dto.payment.BarcodePaymentRequest;
import com.welfare.service.dto.payment.PaymentRequest;
import com.welfare.service.operator.merchant.CreditLimitOperator;
import com.welfare.service.operator.merchant.domain.MerchantAccountOperation;
import com.welfare.service.operator.payment.domain.AccountAmountDO;
import com.welfare.service.operator.payment.domain.PaymentOperation;
import com.welfare.service.operator.payment.domain.RefundOperation;
import com.welfare.service.remote.WoLifeFeignClient;
import com.welfare.service.remote.entity.request.WoLifeAccountDeductionRequest;
import com.welfare.service.remote.entity.request.WoLifeRefundWriteOffRequest;
import com.welfare.service.remote.entity.response.WoLifeAccountDeductionResponse;
import com.welfare.service.remote.entity.response.WoLifeBasicResponse;
import com.welfare.service.wolife.WoLifePaymentService;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.welfare.common.constants.RedisKeyConstant.MER_ACCOUNT_TYPE_OPERATE;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 3/15/2021
 */
@RequiredArgsConstructor
@Service
public class WoLifePaymentServiceImpl implements WoLifePaymentService {
    @Autowired(required = false)
    private WoLifeFeignClient woLifeFeignClient;
    private final MerchantCreditService merchantCreditService;
    private final AccountDao accountDao;
    private final AccountBillDetailDao accountBillDetailDao;
    private final AccountDeductionDetailDao accountDeductionDetailDao;

    @Override
    @DistributedLock(lockPrefix = "wo-life-pay", lockKey = "#paymentRequest.transNo")
    public List<PaymentOperation> pay(PaymentRequest paymentRequest, Account account, List<AccountAmountDO> accountAmountDOList, MerchantCredit merchantCredit, SupplierStore supplierStore) {
        List<AccountAmountType> accountAmountTypes = accountAmountDOList.stream().map(AccountAmountDO::getAccountAmountType)
                .collect(Collectors.toList());
        WoLifeBasicResponse<WoLifeAccountDeductionResponse> basicResponse =
                woLifeFeignClient.accountDeduction(paymentRequest.getPhone(),WoLifeAccountDeductionRequest.of(paymentRequest));
        Assert.isTrue(basicResponse.isSuccess(), basicResponse.getResponseMessage());
        PaymentOperation paymentOperation = new PaymentOperation();
        BigDecimal paymentAmount = paymentRequest.getAmount();
        AccountBillDetail accountBillDetail = AccountAmountDO.generateAccountBillDetail(paymentRequest, paymentAmount, accountAmountTypes);
        paymentOperation.setAccountBillDetail(accountBillDetail);
        paymentOperation.setTransNo(paymentRequest.getTransNo());
        AccountDeductionDetail accountDeductionDetail = AccountAmountDO.decreaseMerchant(paymentRequest,
                null,
                paymentAmount,
                paymentOperation,
                account,
                supplierStore,
                merchantCredit,
                null);
        paymentOperation.setAccountDeductionDetail(accountDeductionDetail);
        paymentOperation.setOperateAmount(paymentAmount);
        paymentOperation.setMerchantAccountType(null);
        return Collections.singletonList(paymentOperation);
    }

    @Override
    public void refund(RefundRequest refundRequest, List<AccountDeductionDetail> paidDeductionDetails, Long accountCode) {
        String lockKey = RedisKeyConstant.ACCOUNT_AMOUNT_TYPE_OPERATE + accountCode;
        RLock accountLock = DistributedLockUtil.lockFairly(lockKey);
        try {
            //沃生活馆支付只会有一条记录
            AccountDeductionDetail thePaidDeductionDetail = paidDeductionDetails.get(0);
            AccountBillDetail thePaidBilDetail = accountBillDetailDao.getOneByTransNoAndTransType(
                    refundRequest.getOriginalTransNo(),
                    WelfareConstant.TransType.CONSUME.code()
            );
            BigDecimal paidAmount = paidDeductionDetails.stream()
                    .map(AccountDeductionDetail::getTransAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            Assert.isTrue(paidAmount.subtract(refundRequest.getAmount())
                    .compareTo(BigDecimal.ZERO) == 0, "沃支付的退款必须全额退款");
            Account account = accountDao.queryByAccountCode(accountCode);
            refundRequest.setPhone(account.getPhone());

            WoLifeBasicResponse woLifeBasicResponse = woLifeFeignClient.refundWriteOff(refundRequest.getPhone(), WoLifeRefundWriteOffRequest.of(refundRequest));
            Assert.isTrue(woLifeBasicResponse.isSuccess(),woLifeBasicResponse.getResponseMessage());

            AccountDeductionDetail refundDeductionDetail = toRefundDeductionDetail(thePaidDeductionDetail);
            AccountBillDetail refundBillDetail = toRefundBillDetail(thePaidBilDetail);
            RLock merAccountLock = DistributedLockUtil.lockFairly(MER_ACCOUNT_TYPE_OPERATE + ":" + account.getMerCode());
            try {
                merchantCreditService.increaseAccountType(
                        account.getMerCode(),
                        WelfareConstant.MerCreditType.CREDIT_LIMIT,
                        refundRequest.getAmount(),
                        refundRequest.getTransNo(),
                        WelfareConstant.TransType.REFUND.code());
            } finally {
                DistributedLockUtil.unlock(merAccountLock);
            }

            accountBillDetailDao.saveOrUpdateBatch(Arrays.asList(refundBillDetail, thePaidBilDetail));
            accountDeductionDetailDao.saveOrUpdateBatch(Arrays.asList(refundDeductionDetail, thePaidDeductionDetail));
        } finally {
            DistributedLockUtil.unlock(accountLock);
        }
    }

    private AccountDeductionDetail toRefundDeductionDetail(AccountDeductionDetail thePayDeductionDetail) {
        AccountDeductionDetail refundDeductionDetail = new AccountDeductionDetail();
        BeanUtils.copyProperties(thePayDeductionDetail, refundDeductionDetail);
        refundDeductionDetail.setTransType(WelfareConstant.TransType.REFUND.code());
        refundDeductionDetail.setId(null);
        refundDeductionDetail.setVersion(0);

        thePayDeductionDetail.setReversedAmount(thePayDeductionDetail.getTransAmount());
        return refundDeductionDetail;
    }

    private AccountBillDetail toRefundBillDetail(AccountBillDetail thePayBillDetail) {
        AccountBillDetail refundBillDetail = new AccountBillDetail();
        BeanUtils.copyProperties(thePayBillDetail, refundBillDetail);
        refundBillDetail.setTransType(WelfareConstant.TransType.REFUND.code());
        refundBillDetail.setId(null);
        refundBillDetail.setVersion(0);
        return refundBillDetail;
    }
}
