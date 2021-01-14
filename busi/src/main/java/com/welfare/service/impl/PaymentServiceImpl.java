package com.welfare.service.impl;

import com.welfare.common.constants.WelfareConstant;
import com.welfare.persist.dao.AccountBillDetailDao;
import com.welfare.persist.dao.AccountDeductionDetailDao;
import com.welfare.persist.entity.*;
import com.welfare.service.*;
import com.welfare.service.dto.payment.CardPaymentRequest;
import com.welfare.service.dto.payment.PaymentRequest;
import com.welfare.service.operator.merchant.domain.MerchantAccountOperation;
import com.welfare.service.operator.payment.domain.AccountAmountDO;
import com.welfare.service.operator.payment.domain.PaymentOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.welfare.common.constants.RedisKeyConstant.MER_ACCOUNT_TYPE_OPERATE;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 1/8/2021
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {
    private final AccountAmountTypeService accountAmountTypeService;
    private final AccountService accountService;
    private final RedissonClient redissonClient;
    private final MerchantCreditService merchantCreditService;
    private final AccountBillDetailDao accountBillDetailDao;
    private final AccountDeductionDetailDao accountDeductionDetailDao;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<PaymentOperation> handlePayRequest(PaymentRequest paymentRequest) {
        Long accountCode = paymentRequest.calculateAccountCode();
        BigDecimal amount = paymentRequest.getAmount();

        Account account = accountService.getByAccountCode(accountCode);
        RLock merAccountLock = redissonClient.getFairLock(MER_ACCOUNT_TYPE_OPERATE + ":" + account.getMerCode());
        merAccountLock.lock();
        try {
            /**
             * 扣减商户余额
             */
            List<MerchantAccountOperation> merchantAccountOperations = merchantCreditService.decreaseAccountType(
                    account.getMerCode(),
                    WelfareConstant.MerCreditType.SELF_DEPOSIT,
                    amount,
                    paymentRequest.getTransNo()
            );
            Map<String, List<MerchantBillDetail>> merBillDetailMap = merchantAccountOperations.stream()
                    .map(MerchantAccountOperation::getMerchantBillDetail)
                    .collect(Collectors.groupingBy(MerchantBillDetail::getTransNo));
            return decreaseAccount(paymentRequest, account, merBillDetailMap);
        } finally {
            merAccountLock.unlock();
        }

    }

    private List<PaymentOperation> decreaseAccount(PaymentRequest paymentRequest,
                                                   Account account,
                                                   Map<String, List<MerchantBillDetail>> merBillDetailMap) {
        String lockKey = "account:" + paymentRequest.calculateAccountCode();
        RLock accountLock = redissonClient.getFairLock(lockKey);
        try {
            BigDecimal usableAmount = account.getAccountBalance().add(account.getSurplusQuota());
            BigDecimal amount = paymentRequest.getAmount();
            boolean enough = usableAmount.subtract(amount).compareTo(BigDecimal.ZERO) > 0;
            Assert.isTrue(enough, "余额不足");

            List<AccountAmountDO> accountAmountDOList = accountAmountTypeService.queryAccountAmountDO(account);
            accountAmountDOList.sort(Comparator.comparing(x -> x.getMerchantAccountType().getDeductionOrder()));
            List<PaymentOperation> paymentOperations = new ArrayList<>(4);
            for (AccountAmountDO accountAmountDO : accountAmountDOList) {
                PaymentOperation currentOperation = decrease(accountAmountDO, amount, paymentRequest, merBillDetailMap);
                amount = amount.subtract(currentOperation.getOperateAmount());
                paymentOperations.add(currentOperation);
                if (currentOperation.isEnough()) {
                    break;
                }
            }
            saveDetails(paymentOperations);
            return paymentOperations;
        } finally {
            accountLock.unlock();
        }
    }

    private void saveDetails(List<PaymentOperation> paymentOperations) {
        List<AccountBillDetail> billDetails = paymentOperations.stream()
                .map(PaymentOperation::getAccountBillDetail)
                .collect(Collectors.toList());
        List<AccountDeductionDetail> deductionDetails = paymentOperations.stream()
                .map(PaymentOperation::getAccountDeductionDetail)
                .collect(Collectors.toList());
        accountBillDetailDao.saveBatch(billDetails);
        accountDeductionDetailDao.saveBatch(deductionDetails);
    }

    private PaymentOperation decrease(AccountAmountDO accountAmountDO,
                                      BigDecimal toOperateAmount,
                                      PaymentRequest paymentRequest,
                                      Map<String, List<MerchantBillDetail>> merBillDetailMap) {
        AccountAmountType accountAmountType = accountAmountDO.getAccountAmountType();
        MerchantAccountType merchantAccountType = accountAmountDO.getMerchantAccountType();
        BigDecimal accountBalance = accountAmountType.getAccountBalance();
        BigDecimal subtract = accountBalance.subtract(toOperateAmount);

        BigDecimal operatedAmount;
        /**
         * 扣减个人账户
         */
        boolean isCurrentEnough = subtract.compareTo(BigDecimal.ZERO) > 0;
        if (isCurrentEnough) {
            operatedAmount = toOperateAmount;
            accountAmountType.setAccountBalance(subtract);
        } else {
            operatedAmount = accountBalance;
            accountAmountType.setAccountBalance(BigDecimal.ZERO);
        }
        PaymentOperation paymentOperation = new PaymentOperation();
        paymentOperation.setOperateAmount(operatedAmount);
        paymentOperation.setAccountAmountType(accountAmountType);
        paymentOperation.setMerchantAccountType(merchantAccountType);
        paymentOperation.setTransNo(paymentRequest.getTransNo());
        AccountBillDetail accountBillDetail = generateAccountBillDetail(paymentRequest, operatedAmount);
        paymentOperation.setAccountBillDetail(accountBillDetail);
        paymentOperation.setEnough(isCurrentEnough);
        AccountDeductionDetail accountDeductionDetail = generateDecutionDetail(
                paymentRequest,
                merBillDetailMap,
                accountAmountType,
                operatedAmount
        );
        paymentOperation.setAccountDeductionDetail(accountDeductionDetail);
        return paymentOperation;

    }

    private AccountDeductionDetail generateDecutionDetail(PaymentRequest paymentRequest, Map<String, List<MerchantBillDetail>> merBillDetailMap, AccountAmountType accountAmountType, BigDecimal operatedAmount) {
        AccountDeductionDetail accountDeductionDetail = new AccountDeductionDetail();
        accountDeductionDetail.setAccountCode(paymentRequest.calculateAccountCode());
        accountDeductionDetail.setAccountDeductionAmount(operatedAmount);
        accountDeductionDetail.setAccountDeductionBalance(accountAmountType.getAccountBalance());
        accountDeductionDetail.setMerAccountType(accountAmountType.getMerAccountTypeCode());
        accountDeductionDetail.setPos(paymentRequest.getMachineNo());
        accountDeductionDetail.setTransNo(paymentRequest.getTransNo());
        accountDeductionDetail.setPayCode(WelfareConstant.PayCode.WELFARE_CARD.code());
        accountDeductionDetail.setTransType(WelfareConstant.TransType.CONSUME.code());
        List<MerchantBillDetail> merchantBillDetails = merBillDetailMap.get(accountDeductionDetail.getTransNo());
        Map<String, MerchantBillDetail> balanceTypedDetailMap = merchantBillDetails.stream().
                collect(Collectors.toMap(MerchantBillDetail::getBalanceType, detail -> detail));
        MerchantBillDetail selfDepositDetail = balanceTypedDetailMap.get(WelfareConstant.MerCreditType.SELF_DEPOSIT.code());
        MerchantBillDetail currentBalanceDetail = balanceTypedDetailMap.get(WelfareConstant.MerCreditType.CURRENT_BALANCE.code());
        MerchantBillDetail remainingCreditDetail = balanceTypedDetailMap.get(WelfareConstant.MerCreditType.REMAINING_LIMIT.code());
        accountDeductionDetail.setSelfDeductionAmount(
                selfDepositDetail == null ? BigDecimal.ZERO : selfDepositDetail.getTransAmount()
        );
        accountDeductionDetail.setMerDeductionAmount(
                currentBalanceDetail == null ? BigDecimal.ZERO : currentBalanceDetail.getCurrentBalance()
        );
        accountDeductionDetail.setMerDeductionCreditAmount(
                remainingCreditDetail == null? BigDecimal.ZERO:remainingCreditDetail.getTransAmount()
        );
        if (paymentRequest instanceof CardPaymentRequest) {
            accountDeductionDetail.setCardId(((CardPaymentRequest) paymentRequest).getCardNo());
        }
        return accountDeductionDetail;
    }

    private AccountBillDetail generateAccountBillDetail(PaymentRequest paymentRequest, BigDecimal operatedAmount) {
        AccountBillDetail accountBillDetail = new AccountBillDetail();
        accountBillDetail.setAccountCode(paymentRequest.calculateAccountCode());
        accountBillDetail.setTransType(WelfareConstant.TransType.CONSUME.code());
        accountBillDetail.setTransTime(paymentRequest.getPaymentDate());
        accountBillDetail.setTransNo(paymentRequest.getTransNo());
        accountBillDetail.setPos(paymentRequest.getMachineNo());
        accountBillDetail.setTransAmount(operatedAmount);
        AccountAmountType surplusQuota = accountAmountTypeService.querySurplusQuota(paymentRequest.calculateAccountCode());
        accountBillDetail.setSurplusQuotaBalance(surplusQuota.getAccountBalance());
        return accountBillDetail;
    }
}
