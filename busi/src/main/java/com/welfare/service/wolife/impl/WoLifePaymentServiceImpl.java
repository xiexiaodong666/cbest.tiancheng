package com.welfare.service.wolife.impl;

import com.alibaba.fastjson.JSON;
import com.welfare.common.annotation.DistributedLock;
import com.welfare.common.constants.RedisKeyConstant;
import com.welfare.common.constants.WelfareConstant;
import com.welfare.common.enums.PaymentTypeEnum;
import com.welfare.common.exception.BusiException;
import com.welfare.common.util.DistributedLockUtil;
import com.welfare.persist.dao.AccountBillDetailDao;
import com.welfare.persist.dao.AccountDao;
import com.welfare.persist.dao.AccountDeductionDetailDao;
import com.welfare.persist.dao.SupplierStoreDao;
import com.welfare.persist.dto.ThirdPartyPaymentRequestDao;
import com.welfare.persist.entity.*;
import com.welfare.service.MerchantCreditService;
import com.welfare.service.ThirdPartyPaymentRequestService;
import com.welfare.service.dto.RefundRequest;
import com.welfare.service.dto.payment.BarcodePaymentRequest;
import com.welfare.service.dto.payment.PaymentRequest;
import com.welfare.service.operator.merchant.CreditLimitOperator;
import com.welfare.service.operator.merchant.RemainingLimitOperator;
import com.welfare.service.operator.merchant.domain.MerchantAccountOperation;
import com.welfare.service.operator.payment.domain.AccountAmountDO;
import com.welfare.service.operator.payment.domain.PaymentOperation;
import com.welfare.service.operator.payment.domain.RefundOperation;
import com.welfare.service.remote.WoLifeFeignClient;
import com.welfare.service.remote.entity.request.WoLifeAccountDeductionDataRequest;
import com.welfare.service.remote.entity.request.WoLifeAccountDeductionRequest;
import com.welfare.service.remote.entity.request.WoLifeRefundWriteOffDataRequest;
import com.welfare.service.remote.entity.request.WoLifeRefundWriteOffRequest;
import com.welfare.service.remote.entity.response.WoLifeAccountDeductionResponse;
import com.welfare.service.remote.entity.response.WoLifeBasicResponse;
import com.welfare.service.remote.service.WoLifeFeignService;
import com.welfare.service.wolife.WoLifePaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
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
@Slf4j
public class WoLifePaymentServiceImpl implements WoLifePaymentService {
    private final WoLifeFeignService woLifeFeignService;
    private final MerchantCreditService merchantCreditService;
    private final AccountDao accountDao;
    private final AccountBillDetailDao accountBillDetailDao;
    private final AccountDeductionDetailDao accountDeductionDetailDao;
    private final ThirdPartyPaymentRequestDao thirdPartyPaymentRequestDao;
    private final SupplierStoreDao supplierStoreDao;
    private final RemainingLimitOperator remainingLimitOperator;
    private final ThirdPartyPaymentRequestService thirdPartyPaymentRequestService;

    @Override
    @DistributedLock(lockPrefix = "wo-life-pay", lockKey = "#paymentRequest.transNo")
    public List<PaymentOperation> pay(PaymentRequest paymentRequest, Account account, List<AccountAmountDO> accountAmountDOList, MerchantCredit merchantCredit, SupplierStore supplierStore) {
        paymentRequest.setPhone(account.getPhone());
        List<AccountAmountType> accountAmountTypes = accountAmountDOList.stream().map(AccountAmountDO::getAccountAmountType)
                .collect(Collectors.toList());
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
                remainingLimitOperator);
        paymentOperation.setAccountDeductionDetail(accountDeductionDetail);
        paymentOperation.setOperateAmount(paymentAmount);
        paymentOperation.setMerchantAccountType(null);

        ThirdPartyPaymentRequest thirdPartyPaymentRequest = thirdPartyPaymentRequestService.generateHandling(paymentRequest);
        try {
            WoLifeBasicResponse<WoLifeAccountDeductionResponse> basicResponse =
                    woLifeFeignService.accountDeduction(paymentRequest.getPhone(), WoLifeAccountDeductionDataRequest.of(paymentRequest));
            if (basicResponse.isSuccess()) {
                thirdPartyPaymentRequestService.updateResult(thirdPartyPaymentRequest, WelfareConstant.AsyncStatus.SUCCEED,basicResponse,null);
            }else{
                thirdPartyPaymentRequestService.updateResult(thirdPartyPaymentRequest, WelfareConstant.AsyncStatus.FAILED, basicResponse, null);
                throw new BusiException("[沃生活馆]支付异常:"+basicResponse.getResponseMessage());
            }
        } catch (Exception e){
            thirdPartyPaymentRequestService.updateResult(thirdPartyPaymentRequest, WelfareConstant.AsyncStatus.FAILED,null,e.getMessage());
            log.error("沃生活馆系统调用异常:",e);
            throw e;
        }

        return Collections.singletonList(paymentOperation);
    }







    @Override
    public void refund(RefundRequest refundRequest, List<AccountDeductionDetail> paidDeductionDetails, Long accountCode) {
        String lockKey = RedisKeyConstant.ACCOUNT_AMOUNT_TYPE_OPERATE + accountCode;
        RLock accountLock = DistributedLockUtil.lockFairly(lockKey);
        try {
            ThirdPartyPaymentRequest thirdPartyPaymentRequest = thirdPartyPaymentRequestService.generateRefundHandling(refundRequest);
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
            try{
                WoLifeBasicResponse woLifeBasicResponse = woLifeFeignService.refundWriteOff(refundRequest.getPhone(), WoLifeRefundWriteOffDataRequest.of(refundRequest));
                if(woLifeBasicResponse.isSuccess()){
                    thirdPartyPaymentRequestService.updateResult(thirdPartyPaymentRequest, WelfareConstant.AsyncStatus.SUCCEED,woLifeBasicResponse,null);
                }else{
                    thirdPartyPaymentRequestService.updateResult(thirdPartyPaymentRequest, WelfareConstant.AsyncStatus.FAILED,woLifeBasicResponse,null);
                    throw new RuntimeException("[沃生活馆]退款异常:"+woLifeBasicResponse.getResponseMessage());
                }
            }catch (Exception e){
                log.error("沃生活馆退款系统调用异常:",e);
                thirdPartyPaymentRequestService.updateResult(thirdPartyPaymentRequest, WelfareConstant.AsyncStatus.FAILED,null,e.getMessage());
                throw e;
            }
            AccountDeductionDetail refundDeductionDetail = toRefundDeductionDetail(thePaidDeductionDetail,refundRequest);
            AccountBillDetail refundBillDetail = toRefundBillDetail(thePaidBilDetail,refundRequest);
            SupplierStore supplierStore = supplierStoreDao.getOneByCode(refundBillDetail.getStoreCode());
            if(!Objects.equals(account.getMerCode(),supplierStore.getMerCode())){
                //非自营才有退回商户操作,和扣款时保持一致
                operateMerchant(refundRequest, account);
            }

            accountBillDetailDao.saveOrUpdateBatch(Arrays.asList(refundBillDetail, thePaidBilDetail));
            accountDeductionDetailDao.saveOrUpdateBatch(Arrays.asList(refundDeductionDetail, thePaidDeductionDetail));
            refundRequest.setRefundStatus(WelfareConstant.AsyncStatus.SUCCEED.code());
        } finally {
            DistributedLockUtil.unlock(accountLock);
        }
    }

    private void operateMerchant(RefundRequest refundRequest, Account account) {
        RLock merAccountLock = DistributedLockUtil.lockFairly(MER_ACCOUNT_TYPE_OPERATE + ":" + account.getMerCode());
        try {
            merchantCreditService.increaseAccountType(
                    account.getMerCode(),
                    WelfareConstant.MerCreditType.REMAINING_LIMIT,
                    refundRequest.getAmount(),
                    refundRequest.getTransNo(),
                    WelfareConstant.TransType.REFUND.code());
        } finally {
            DistributedLockUtil.unlock(merAccountLock);
        }
    }

    private AccountDeductionDetail toRefundDeductionDetail(AccountDeductionDetail thePayDeductionDetail, RefundRequest refundRequest) {
        AccountDeductionDetail refundDeductionDetail = new AccountDeductionDetail();
        BeanUtils.copyProperties(thePayDeductionDetail, refundDeductionDetail);
        refundDeductionDetail.setTransNo(refundRequest.getTransNo());
        refundDeductionDetail.setRelatedTransNo(refundRequest.getOriginalTransNo());
        refundDeductionDetail.setTransType(WelfareConstant.TransType.REFUND.code());
        refundDeductionDetail.setId(null);
        refundDeductionDetail.setVersion(0);

        thePayDeductionDetail.setReversedAmount(thePayDeductionDetail.getTransAmount());
        return refundDeductionDetail;
    }

    private AccountBillDetail toRefundBillDetail(AccountBillDetail thePayBillDetail, RefundRequest refundRequest) {
        AccountBillDetail refundBillDetail = new AccountBillDetail();
        BeanUtils.copyProperties(thePayBillDetail, refundBillDetail);
        refundBillDetail.setTransNo(refundRequest.getTransNo());
        refundBillDetail.setTransType(WelfareConstant.TransType.REFUND.code());
        refundBillDetail.setId(null);
        refundBillDetail.setVersion(0);
        return refundBillDetail;
    }
}
