package com.welfare.service.impl;

import com.welfare.common.constants.WelfareConstant;
import com.welfare.common.constants.WelfareConstant.MerAccountTypeCode;
import com.welfare.persist.dao.*;
import com.welfare.persist.entity.*;
import com.welfare.service.*;
import com.welfare.service.dto.payment.CardPaymentRequest;
import com.welfare.service.dto.payment.PaymentRequest;
import com.welfare.service.operator.merchant.CurrentBalanceOperator;
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
import java.util.*;
import java.util.stream.Collectors;

import static com.welfare.common.constants.RedisKeyConstant.MER_ACCOUNT_TYPE_OPERATE;
import static com.welfare.common.constants.WelfareConstant.MerAccountTypeCode.SELF;
import static com.welfare.common.constants.WelfareConstant.MerAccountTypeCode.SURPLUS_QUOTA;

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
    private final AccountAmountTypeDao accountAmountTypeDao;
    private final AccountDao accountDao;
    private final CurrentBalanceOperator currentBalanceOperator;
    private final MerchantBillDetailDao merchantBillDetailDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<PaymentOperation> handlePayRequest(PaymentRequest paymentRequest) {
        Long accountCode = paymentRequest.calculateAccountCode();

        Account account = accountService.getByAccountCode(accountCode);
        RLock merAccountLock = redissonClient.getFairLock(MER_ACCOUNT_TYPE_OPERATE + ":" + account.getMerCode());
        merAccountLock.lock();
        try {
            List<PaymentOperation> paymentOperations = decreaseAccount(paymentRequest, account);
            List<MerchantBillDetail> merchantBillDetails = paymentOperations.stream()
                    .flatMap(paymentOperation -> paymentOperation.getMerchantAccountOperations().stream())
                    .map(MerchantAccountOperation::getMerchantBillDetail)
                    .collect(Collectors.toList());
            merchantBillDetailDao.saveBatch(merchantBillDetails);
            return paymentOperations;
        } finally {
            merAccountLock.unlock();
        }

    }

    private List<PaymentOperation> decreaseAccount(PaymentRequest paymentRequest,
                                                   Account account) {
        String lockKey = "account:" + paymentRequest.calculateAccountCode();
        RLock accountLock = redissonClient.getFairLock(lockKey);
        try {
            BigDecimal usableAmount = account.getAccountBalance().add(account.getSurplusQuota());
            BigDecimal amount = paymentRequest.getAmount();
            boolean enough = usableAmount.subtract(amount).compareTo(BigDecimal.ZERO) >= 0;
            Assert.isTrue(enough, "余额不足");

            List<AccountAmountDO> accountAmountDOList = accountAmountTypeService.queryAccountAmountDO(account);
            accountAmountDOList.sort(Comparator.comparing(x -> x.getMerchantAccountType().getDeductionOrder()));
            List<PaymentOperation> paymentOperations = new ArrayList<>(4);
            List<AccountAmountType> accountAmountTypes = accountAmountDOList.stream().map(AccountAmountDO::getAccountAmountType)
                    .collect(Collectors.toList());
            for (AccountAmountDO accountAmountDO : accountAmountDOList) {
                if(BigDecimal.ZERO.equals(accountAmountDO.getAccountAmountType().getAccountBalance())){
                    //当前的accountType没钱，则继续下一个账户
                    continue;
                }
                PaymentOperation currentOperation = decrease(accountAmountDO, amount, paymentRequest, accountAmountTypes);
                amount = amount.subtract(currentOperation.getOperateAmount());
                paymentOperations.add(currentOperation);
                if (currentOperation.isEnough()) {
                    break;
                }
            }
            saveDetails(paymentOperations, account);
            return paymentOperations;
        } finally {
            accountLock.unlock();
        }
    }

    private void saveDetails(List<PaymentOperation> paymentOperations, Account account) {
        List<AccountBillDetail> billDetails = paymentOperations.stream()
                .map(PaymentOperation::getAccountBillDetail)
                .collect(Collectors.toList());
        List<AccountDeductionDetail> deductionDetails = paymentOperations.stream()
                .map(PaymentOperation::getAccountDeductionDetail)
                .collect(Collectors.toList());
        List<AccountAmountType> accountTypes = paymentOperations.stream()
                .map(PaymentOperation::getAccountAmountType)
                .collect(Collectors.toList());
        accountBillDetailDao.saveBatch(billDetails);
        accountDeductionDetailDao.saveBatch(deductionDetails);
        accountAmountTypeDao.updateBatchById(accountTypes);
        BigDecimal balanceSum = accountAmountTypeService.sumBalanceExceptSurplusQuota(account.getAccountCode());
        AccountAmountType accountAmountType = accountAmountTypeService.queryOne(account.getAccountCode(), SURPLUS_QUOTA.code());
        account.setAccountBalance(balanceSum);
        account.setSurplusQuota(accountAmountType.getAccountBalance());
        accountDao.updateById(account);
    }

    private PaymentOperation decrease(AccountAmountDO accountAmountDO,
                                      BigDecimal toOperateAmount,
                                      PaymentRequest paymentRequest,
                                      List<AccountAmountType> accountAmountTypes) {
        AccountAmountType accountAmountType = accountAmountDO.getAccountAmountType();
        MerchantAccountType merchantAccountType = accountAmountDO.getMerchantAccountType();
        BigDecimal accountBalance = accountAmountType.getAccountBalance();
        BigDecimal subtract = accountBalance.subtract(toOperateAmount);

        BigDecimal operatedAmount;
        /**
         * 扣减个人账户
         */
        boolean isCurrentEnough = subtract.compareTo(BigDecimal.ZERO) >= 0;
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
        AccountBillDetail accountBillDetail = generateAccountBillDetail(paymentRequest, operatedAmount, accountAmountTypes);
        paymentOperation.setAccountBillDetail(accountBillDetail);
        paymentOperation.setEnough(isCurrentEnough);
        AccountDeductionDetail accountDeductionDetail = decreaseMerchant(
                paymentRequest,
                accountAmountType,
                operatedAmount,
                paymentOperation
        );
        paymentOperation.setAccountDeductionDetail(accountDeductionDetail);
        return paymentOperation;

    }

    private AccountDeductionDetail decreaseMerchant(PaymentRequest paymentRequest,
                                                    AccountAmountType accountAmountType,
                                                    BigDecimal operatedAmount, PaymentOperation paymentOperation) {
        AccountDeductionDetail accountDeductionDetail = new AccountDeductionDetail();
        accountDeductionDetail.setAccountCode(paymentRequest.calculateAccountCode());
        accountDeductionDetail.setAccountDeductionAmount(operatedAmount);
        accountDeductionDetail.setAccountAmountTypeBalance(accountAmountType.getAccountBalance());
        accountDeductionDetail.setMerAccountType(accountAmountType.getMerAccountTypeCode());
        accountDeductionDetail.setPos(paymentRequest.getMachineNo());
        accountDeductionDetail.setTransNo(paymentRequest.getTransNo());
        accountDeductionDetail.setPayCode(WelfareConstant.PayCode.WELFARE_CARD.code());
        accountDeductionDetail.setTransType(WelfareConstant.TransType.CONSUME.code());
        accountDeductionDetail.setTransAmount(operatedAmount);
        accountDeductionDetail.setTransTime(paymentRequest.getPaymentDate());
        accountDeductionDetail.setStoreCode(paymentRequest.getStoreNo());
        if(paymentRequest instanceof CardPaymentRequest){
            accountDeductionDetail.setCardId(paymentRequest.getCardNo());
        }
        if (SELF.code().equals(accountAmountType.getMerAccountTypeCode())) {
            accountDeductionDetail.setSelfDeductionAmount(operatedAmount);
            accountDeductionDetail.setMerDeductionAmount(BigDecimal.ZERO);
            accountDeductionDetail.setMerDeductionCreditAmount(BigDecimal.ZERO);
            paymentOperation.setMerchantAccountOperations(Collections.emptyList());
        } else {
            accountDeductionDetail.setSelfDeductionAmount(BigDecimal.ZERO);
            accountDeductionDetail.setAccountDeductionAmount(operatedAmount);
            //非自主充值，需要扣减商户账户
            List<MerchantAccountOperation> merchantAccountOperations = merchantCreditService.doOperateAccount(
                    paymentRequest.getMerCode(),
                    operatedAmount,
                    paymentRequest.getTransNo(),
                    currentBalanceOperator);
            paymentOperation.setMerchantAccountOperations(merchantAccountOperations);
            Map<String, MerchantBillDetail> merBillDetailMap = merchantAccountOperations.stream().map(MerchantAccountOperation::getMerchantBillDetail)
                    .collect(Collectors.toMap(MerchantBillDetail::getBalanceType, merchantBillDetail -> merchantBillDetail));
            MerchantBillDetail currentBalanceDetail = merBillDetailMap.get(WelfareConstant.MerCreditType.CURRENT_BALANCE.code());
            MerchantBillDetail remainingLimitDetail = merBillDetailMap.get(WelfareConstant.MerCreditType.REMAINING_LIMIT.code());
            accountDeductionDetail.setMerDeductionAmount(currentBalanceDetail == null ? BigDecimal.ZERO : currentBalanceDetail.getTransAmount());
            accountDeductionDetail.setMerDeductionCreditAmount(remainingLimitDetail == null ? BigDecimal.ZERO : remainingLimitDetail.getTransAmount());
        }


        return accountDeductionDetail;
    }

    private AccountBillDetail generateAccountBillDetail(PaymentRequest paymentRequest, BigDecimal operatedAmount, List<AccountAmountType> accountAmountTypes) {
        AccountBillDetail accountBillDetail = new AccountBillDetail();
        accountBillDetail.setAccountCode(paymentRequest.calculateAccountCode());
        accountBillDetail.setTransType(WelfareConstant.TransType.CONSUME.code());
        accountBillDetail.setTransTime(paymentRequest.getPaymentDate());
        accountBillDetail.setTransNo(paymentRequest.getTransNo());
        accountBillDetail.setPos(paymentRequest.getMachineNo());
        accountBillDetail.setTransAmount(operatedAmount);
        accountBillDetail.setStoreCode(paymentRequest.getStoreNo());
        accountBillDetail.setCardId(paymentRequest.getCardNo());
        BigDecimal accountBalance = accountAmountTypes.stream()
                .filter(
                        type -> !SELF.code().equals(type.getMerAccountTypeCode())
                                && !SURPLUS_QUOTA.code().equals(type.getMerAccountTypeCode())
                )
                .map(AccountAmountType::getAccountBalance)
                .reduce(BigDecimal.ZERO,BigDecimal::add);
        BigDecimal accountSurplusQuota =accountAmountTypes.stream()
                .filter(
                        type -> SURPLUS_QUOTA.code().equals(type.getMerAccountTypeCode())
                )
                .map(AccountAmountType::getAccountBalance)
                .reduce(BigDecimal.ZERO,BigDecimal::add);
        accountBillDetail.setAccountBalance(accountBalance);
        accountBillDetail.setSurplusQuota(accountSurplusQuota);
        return accountBillDetail;
    }
}
