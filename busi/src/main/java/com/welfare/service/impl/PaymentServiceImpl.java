package com.welfare.service.impl;

import com.welfare.common.constants.WelfareConstant;
import com.welfare.common.exception.BusiException;
import com.welfare.common.exception.ExceptionCode;
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
import org.springframework.util.CollectionUtils;

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
    private final AccountConsumeSceneDao accountConsumeSceneDao;
    private final AccountConsumeSceneStoreRelationDao accountConsumeSceneStoreRelationDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<PaymentOperation> handlePayRequest(PaymentRequest paymentRequest) {
        Long accountCode = paymentRequest.calculateAccountCode();
        Account account = accountService.getByAccountCode(accountCode);
        //chargePaymentScene(paymentRequest, account);

        RLock merAccountLock = redissonClient.getFairLock(MER_ACCOUNT_TYPE_OPERATE + ":" + account.getMerCode());
        merAccountLock.lock();
        try {
            List<PaymentOperation> paymentOperations = decreaseAccount(paymentRequest, account);
            List<MerchantBillDetail> merchantBillDetails = paymentOperations.stream()
                    .flatMap(paymentOperation -> paymentOperation.getMerchantAccountOperations().stream())
                    .map(MerchantAccountOperation::getMerchantBillDetail)
                    .collect(Collectors.toList());
            merchantBillDetailDao.saveBatch(merchantBillDetails);

            paymentRequest.setPaymentStatus(WelfareConstant.AsyncStatus.SUCCEED.code());
            paymentRequest.setAccountName(account.getAccountName());
            paymentRequest.setAccountBalance(account.getAccountBalance());
            paymentRequest.setAccountCredit(account.getSurplusQuota());
            return paymentOperations;
        } finally {
            merAccountLock.unlock();
        }

    }

    private void chargePaymentScene(PaymentRequest paymentRequest, Account account) {
        String paymentScene = paymentRequest.calculatePaymentScene();
        AccountConsumeScene accountConsumeScene = accountConsumeSceneDao
                .getOneByAccountTypeAndMerCode(account.getAccountTypeCode(), account.getMerCode());
        AccountConsumeSceneStoreRelation sceneStoreRelation = accountConsumeSceneStoreRelationDao
                .getOneBySceneIdAndStoreNo(accountConsumeScene.getId(), paymentRequest.getStoreNo());
        List<String> sceneConsumeTypes = Arrays.asList(sceneStoreRelation.getSceneConsumType().split(","));
        if(!sceneConsumeTypes.contains(paymentScene)){
            throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS,"当前用户不支持此消费场景",null);
        }
    }

    @Override
    public PaymentRequest queryResult(String transNo) {
        List<AccountBillDetail> accountDeductionDetails = accountBillDetailDao.queryByTransNoAndTransType(
                transNo,
                WelfareConstant.TransType.CONSUME.code()
        );
        CardPaymentRequest paymentRequest = new CardPaymentRequest();
        paymentRequest.setTransNo(transNo);
        if(CollectionUtils.isEmpty(accountDeductionDetails)){
            paymentRequest.setPaymentStatus(WelfareConstant.AsyncStatus.FAILED.code());
        }
        paymentRequest.setPaymentStatus(WelfareConstant.AsyncStatus.SUCCEED.code());
        AccountBillDetail firstAccountBillDetail = accountDeductionDetails.get(0);
        paymentRequest.setStoreNo(firstAccountBillDetail.getStoreCode());
        paymentRequest.setAccountCode(firstAccountBillDetail.getAccountCode());
        paymentRequest.setMachineNo(firstAccountBillDetail.getPos());
        paymentRequest.setPaymentDate(firstAccountBillDetail.getTransTime());
        paymentRequest.setCardNo(firstAccountBillDetail.getCardId());
        BigDecimal amount = accountDeductionDetails.stream()
                .map(AccountBillDetail::getTransAmount)
                .reduce(BigDecimal.ZERO,BigDecimal::add);
        paymentRequest.setAmount(amount);

        Account account = accountService.getByAccountCode(firstAccountBillDetail.getAccountCode());
        paymentRequest.setAccountBalance(account.getAccountBalance());
        paymentRequest.setAccountName(account.getAccountName());
        paymentRequest.setAccountCredit(account.getSurplusQuota());
        return paymentRequest;
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
                if(BigDecimal.ZERO.compareTo(accountAmountDO.getAccountAmountType().getAccountBalance())==0){
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

        BigDecimal accountBalance = AccountAmountDO.calculateAccountBalance(accountTypes);
        BigDecimal accountCreditBalance = AccountAmountDO.calculateAccountCredit(accountTypes);
        account.setAccountBalance(accountBalance);
        account.setSurplusQuota(accountCreditBalance);
        accountDao.updateById(account);
        accountBillDetailDao.saveBatch(billDetails);
        accountDeductionDetailDao.saveBatch(deductionDetails);
        accountAmountTypeDao.saveOrUpdateBatch(accountTypes);
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
                paymentOperation,
                accountAmountDO.getAccount()
        );
        paymentOperation.setAccountDeductionDetail(accountDeductionDetail);
        return paymentOperation;

    }

    private AccountDeductionDetail decreaseMerchant(PaymentRequest paymentRequest,
                                                    AccountAmountType accountAmountType,
                                                    BigDecimal operatedAmount,
                                                    PaymentOperation paymentOperation,
                                                    Account account) {
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
                    account.getMerCode(),
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
        BigDecimal accountBalance = AccountAmountDO.calculateAccountBalance(accountAmountTypes);
        BigDecimal accountSurplusQuota = AccountAmountDO.calculateAccountCredit(accountAmountTypes);
        accountBillDetail.setAccountBalance(accountBalance);
        accountBillDetail.setSurplusQuota(accountSurplusQuota);
        return accountBillDetail;
    }
}
