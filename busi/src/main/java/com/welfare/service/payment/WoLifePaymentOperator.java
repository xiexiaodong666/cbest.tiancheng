package com.welfare.service.payment;

import com.welfare.common.annotation.DistributedLock;
import com.welfare.common.constants.RedisKeyConstant;
import com.welfare.common.constants.WelfareConstant;
import com.welfare.common.exception.BizException;
import com.welfare.common.util.DistributedLockUtil;
import com.welfare.persist.entity.*;
import com.welfare.service.ThirdPartyPaymentRequestService;
import com.welfare.service.dto.RefundRequest;
import com.welfare.service.dto.payment.PaymentRequest;
import com.welfare.service.operator.merchant.RemainingLimitOperator;
import com.welfare.service.operator.payment.domain.AccountAmountDO;
import com.welfare.service.operator.payment.domain.PaymentOperation;
import com.welfare.service.remote.entity.request.WoLifeAccountDeductionDataRequest;
import com.welfare.service.remote.entity.request.WoLifeRefundWriteOffDataRequest;
import com.welfare.service.remote.entity.response.WoLifeAccountDeductionResponse;
import com.welfare.service.remote.entity.response.WoLifeBasicResponse;
import com.welfare.service.remote.service.WoLifeFeignService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 3/26/2021
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class WoLifePaymentOperator implements IPaymentOperator, IRefundOperator{
    private final WoLifeFeignService woLifeFeignService;
    private final RemainingLimitOperator remainingLimitOperator;
    private final ThirdPartyPaymentRequestService thirdPartyPaymentRequestService;

    @Override
    @DistributedLock(lockPrefix = "wo-life-pay", lockKey = "#paymentRequest.transNo")
    public List<PaymentOperation> pay(PaymentRequest paymentRequest, Account account, List<AccountAmountDO> accountAmountDOList, SupplierStore supplierStore, MerchantCredit merchantCredit) {
        PaymentOperation paymentOperation = doPay(paymentRequest, account, accountAmountDOList, supplierStore, merchantCredit, remainingLimitOperator);

        ThirdPartyPaymentRequest thirdPartyPaymentRequest = thirdPartyPaymentRequestService.generateHandling(paymentRequest);
        try {
            WoLifeBasicResponse<WoLifeAccountDeductionResponse> basicResponse =
                    woLifeFeignService.accountDeduction(paymentRequest.getPhone(), WoLifeAccountDeductionDataRequest.of(paymentRequest));
            if (basicResponse.isSuccess()) {
                thirdPartyPaymentRequestService.updateResult(thirdPartyPaymentRequest, WelfareConstant.AsyncStatus.SUCCEED,basicResponse,null);
            }else{
                thirdPartyPaymentRequestService.updateResult(thirdPartyPaymentRequest, WelfareConstant.AsyncStatus.FAILED, basicResponse, null);
                throw new BizException("[沃生活馆]支付异常:"+basicResponse.getResponseMessage());
            }
        } catch (Exception e){
            thirdPartyPaymentRequestService.updateResult(thirdPartyPaymentRequest, WelfareConstant.AsyncStatus.FAILED,null,e.getMessage());
            log.error("沃生活馆系统调用异常:",e);
            throw e;
        }

        return Collections.singletonList(paymentOperation);
    }




    @Override
    public void refund(RefundRequest refundRequest, List<AccountDeductionDetail> refundDeductionInDbs, List<AccountDeductionDetail> paidDeductionDetails, Long accountCode) {
        String lockKey = RedisKeyConstant.ACCOUNT_AMOUNT_TYPE_OPERATE + accountCode;
        RLock accountLock = DistributedLockUtil.lockFairly(lockKey);
        try {
            doRefund(refundRequest, paidDeductionDetails, accountCode);

            ThirdPartyPaymentRequest thirdPartyPaymentRequest = thirdPartyPaymentRequestService.generateRefundHandling(refundRequest);
            try{
                WoLifeBasicResponse woLifeBasicResponse = woLifeFeignService.refundWriteOff(refundRequest.getPhone(), WoLifeRefundWriteOffDataRequest.of(refundRequest, paidDeductionDetails.get(0).getOrderChannel()));
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
        } finally {
            DistributedLockUtil.unlock(accountLock);
        }
    }
}
