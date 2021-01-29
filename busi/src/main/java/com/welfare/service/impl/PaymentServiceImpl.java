package com.welfare.service.impl;

import com.alibaba.fastjson.JSON;
import com.welfare.common.annotation.DistributedLock;
import com.welfare.common.constants.AccountStatus;
import com.welfare.common.constants.RedisKeyConstant;
import com.welfare.common.constants.WelfareConstant;
import com.welfare.common.enums.ConsumeTypeEnum;
import com.welfare.common.enums.EnableEnum;
import com.welfare.common.enums.SupplierStoreStatusEnum;
import com.welfare.common.exception.BusiException;
import com.welfare.common.exception.ExceptionCode;
import com.welfare.common.perf.PerfMonitor;
import com.welfare.common.util.DistributedLockUtil;
import com.welfare.persist.dao.*;
import com.welfare.persist.entity.*;
import com.welfare.service.*;
import com.welfare.service.async.AsyncNotificationService;
import com.welfare.service.dto.payment.CardPaymentRequest;
import com.welfare.service.dto.payment.PaymentRequest;
import com.welfare.service.operator.merchant.CurrentBalanceOperator;
import com.welfare.service.operator.merchant.domain.MerchantAccountOperation;
import com.welfare.service.operator.payment.domain.AccountAmountDO;
import com.welfare.service.operator.payment.domain.PaymentOperation;
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
import static com.welfare.common.constants.WelfareConstant.MerAccountTypeCode.SELF;

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
    private final MerchantCreditService merchantCreditService;
    private final AccountBillDetailDao accountBillDetailDao;
    private final AccountDeductionDetailDao accountDeductionDetailDao;
    private final AccountAmountTypeDao accountAmountTypeDao;
    private final AccountDao accountDao;
    private final CurrentBalanceOperator currentBalanceOperator;
    private final MerchantBillDetailDao merchantBillDetailDao;
    private final AccountConsumeSceneDao accountConsumeSceneDao;
    private final AccountConsumeSceneStoreRelationDao accountConsumeSceneStoreRelationDao;
    private final SupplierStoreService supplierStoreService;
    private final MerchantStoreRelationDao merchantStoreRelationDao;
    private final MerchantCreditDao merchantCreditDao;
    private final AsyncNotificationService asyncNotificationService;
    PerfMonitor paymentRequestPerfMonitor = new PerfMonitor("paymentRequest");

    @Override
    @Transactional(rollbackFor = Exception.class)
    @DistributedLock(lockPrefix = "e-welfare-payment::", lockKey = "#paymentRequest.transNo")
    public PaymentRequest paymentRequest(PaymentRequest paymentRequest) {
        paymentRequestPerfMonitor.start();
        PaymentRequest requestHandled = queryResult(paymentRequest.getTransNo());
        if (WelfareConstant.AsyncStatus.SUCCEED.code().equals(requestHandled.getPaymentStatus())
                || WelfareConstant.AsyncStatus.REVERSED.code().equals(requestHandled.getPaymentStatus())) {
            log.warn("重复的支付请求，直接返回已经处理完成的request{}", JSON.toJSONString(requestHandled));
            BeanUtils.copyProperties(requestHandled, paymentRequest);
            return requestHandled;
        }
        Long accountCode = paymentRequest.calculateAccountCode();
        Assert.notNull(accountCode,"账号不能为空。");
        SupplierStore supplierStore = supplierStoreService.getSupplierStoreByStoreCode(paymentRequest.getStoreNo());
        String lockKey = RedisKeyConstant.ACCOUNT_AMOUNT_TYPE_OPERATE + paymentRequest.calculateAccountCode();
        RLock accountLock = DistributedLockUtil.lockFairly(lockKey);
        try {
            Account account = accountService.getByAccountCode(accountCode);
            Assert.notNull(account,"未找到账号：" + accountCode);
            paymentRequest.setAccountMerCode(account.getMerCode());
            chargeBeforePay(paymentRequest, account, supplierStore);
            List<AccountAmountDO> accountAmountDOList = accountAmountTypeService.queryAccountAmountDO(account);
            RLock merAccountLock = DistributedLockUtil.lockFairly(MER_ACCOUNT_TYPE_OPERATE + ":" + account.getMerCode());
            List<MerchantBillDetail> merchantBillDetails;
            try {
                List<PaymentOperation> paymentOperations = decreaseAccount(paymentRequest, account,accountAmountDOList,supplierStore);
                merchantBillDetails = paymentOperations.stream()
                        .flatMap(paymentOperation -> paymentOperation.getMerchantAccountOperations().stream())
                        .map(MerchantAccountOperation::getMerchantBillDetail)
                        .collect(Collectors.toList());
            } finally {
                DistributedLockUtil.unlock(merAccountLock);
            }
            if (!CollectionUtils.isEmpty(merchantBillDetails)) {
                merchantBillDetailDao.saveBatch(merchantBillDetails);
            }

            paymentRequest.setPaymentStatus(WelfareConstant.AsyncStatus.SUCCEED.code());
            paymentRequest.setAccountName(account.getAccountName());
            paymentRequest.setAccountBalance(account.getAccountBalance());
            paymentRequest.setAccountCredit(account.getSurplusQuota());
            paymentRequest.setPhone(account.getPhone());
            return paymentRequest;
        } finally {
            DistributedLockUtil.unlock(accountLock);
            paymentRequestPerfMonitor.stop();
            if(ConsumeTypeEnum.SHOP_SHOPPING.getCode().equals(paymentRequest.getPaymentScene())){
                asyncNotificationService.paymentNotify(paymentRequest.getPhone(),paymentRequest.getAmount());
            }
        }

    }

    /**
     * 判断消费场景是否符合配置
     *  @param paymentRequest
     * @param account
     * @param supplierStore
     */
    private String chargeBeforePay(PaymentRequest paymentRequest, Account account, SupplierStore supplierStore) {
        Assert.isTrue(AccountStatus.ENABLE.getCode().equals(account.getAccountStatus()), "账户未启用");
        MerchantStoreRelation merStoreRelation = merchantStoreRelationDao.getOneByStoreCodeAndMerCode(paymentRequest.getStoreNo(), account.getMerCode());
        Assert.notNull(merStoreRelation,"用户所在组织（公司）不支持在该门店消费或配置已禁用");
        Assert.isTrue(EnableEnum.ENABLE.getCode().equals(merStoreRelation.getStatus()), "用户所在组织（公司）不支持在该门店消费或配置已禁用");
        Assert.isTrue(SupplierStoreStatusEnum.ACTIVATED.getCode().equals(supplierStore.getStatus()),
                "门店未激活:" + supplierStore.getStoreCode());
        String paymentScene = paymentRequest.calculatePaymentScene();
        List<AccountConsumeScene> accountConsumeScenes = accountConsumeSceneDao
                .getAccountTypeAndMerCode(account.getAccountTypeCode(), account.getMerCode());
        Assert.isTrue(!CollectionUtils.isEmpty(accountConsumeScenes), "未找到该账户的可用交易场景配置");
        List<Long> sceneIds = accountConsumeScenes.stream().map(AccountConsumeScene::getId).collect(Collectors.toList());
        List<AccountConsumeSceneStoreRelation> sceneStoreRelations = accountConsumeSceneStoreRelationDao
                .queryBySceneIdsAndStoreNo(sceneIds, paymentRequest.getStoreNo());
        Assert.isTrue(!CollectionUtils.isEmpty(sceneStoreRelations), "未找到该门店的可用交易场景配置");
        List<String> sceneConsumeTypes = sceneStoreRelations.stream().map(relation -> Arrays.asList(relation.getSceneConsumType().split(",")))
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());
        if (!sceneConsumeTypes.contains(paymentScene)) {
            throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "当前用户不支持此消费场景:" + paymentScene, null);
        }
        return paymentScene;
    }

    @Override
    public PaymentRequest queryResult(String transNo) {
        List<AccountBillDetail> accountDeductionDetails = accountBillDetailDao.queryByTransNoAndTransType(
                transNo,
                WelfareConstant.TransType.CONSUME.code()
        );
        List<AccountDeductionDetail> refundDeductionDetails = accountDeductionDetailDao.queryByRelatedTransNoAndTransType(
                transNo,
                WelfareConstant.TransType.REFUND.code()
        );
        CardPaymentRequest paymentRequest = new CardPaymentRequest();
        paymentRequest.setTransNo(transNo);
        if (CollectionUtils.isEmpty(accountDeductionDetails)) {
            paymentRequest.setPaymentStatus(WelfareConstant.AsyncStatus.FAILED.code());
        } else {
            if (CollectionUtils.isEmpty(refundDeductionDetails)) {
                paymentRequest.setPaymentStatus(WelfareConstant.AsyncStatus.SUCCEED.code());
            } else {
                paymentRequest.setPaymentStatus(WelfareConstant.AsyncStatus.REVERSED.code());
                paymentRequest.setRefundTransNo(refundDeductionDetails.get(0).getTransNo());
            }
            AccountBillDetail firstAccountBillDetail = accountDeductionDetails.get(0);
            paymentRequest.setStoreNo(firstAccountBillDetail.getStoreCode());
            paymentRequest.setAccountCode(firstAccountBillDetail.getAccountCode());
            paymentRequest.setMachineNo(firstAccountBillDetail.getPos());
            paymentRequest.setPaymentDate(firstAccountBillDetail.getTransTime());
            paymentRequest.setCardNo(firstAccountBillDetail.getCardId());
            BigDecimal amount = accountDeductionDetails.stream()
                    .map(AccountBillDetail::getTransAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            paymentRequest.setAmount(amount);

            Account account = accountService.getByAccountCode(firstAccountBillDetail.getAccountCode());
            paymentRequest.setAccountBalance(account.getAccountBalance());
            paymentRequest.setAccountName(account.getAccountName());
            paymentRequest.setAccountCredit(account.getSurplusQuota());
            paymentRequest.setPhone(account.getPhone());
        }

        return paymentRequest;
    }

    private List<PaymentOperation> decreaseAccount(PaymentRequest paymentRequest,
                                                   Account account, List<AccountAmountDO> accountAmountDOList, SupplierStore supplierStore) {
        BigDecimal usableAmount = account.getAccountBalance().add(account.getSurplusQuota());
        BigDecimal amount = paymentRequest.getAmount();
        boolean enough = usableAmount.subtract(amount).compareTo(BigDecimal.ZERO) >= 0;
        Assert.isTrue(enough, "用户账户总余额不足");
        BigDecimal allTypeBalance = accountAmountDOList.stream()
                .map(accountAmountDO -> accountAmountDO.getAccountAmountType().getAccountBalance())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        //判断子账户之和
        boolean accountTypesEnough = allTypeBalance.subtract(amount).compareTo(BigDecimal.ZERO) >= 0;
        Assert.isTrue(accountTypesEnough, "用户子账户余额总和不足,请确认员工账户总账和子账是否对应");
        accountAmountDOList.sort(Comparator.comparing(x -> x.getMerchantAccountType().getDeductionOrder()));
        List<PaymentOperation> paymentOperations = new ArrayList<>(4);
        List<AccountAmountType> accountAmountTypes = accountAmountDOList.stream().map(AccountAmountDO::getAccountAmountType)
                .collect(Collectors.toList());
        MerchantCredit merchantCredit = merchantCreditService.getByMerCode(account.getMerCode());
        for (AccountAmountDO accountAmountDO : accountAmountDOList) {
            if (BigDecimal.ZERO.compareTo(accountAmountDO.getAccountAmountType().getAccountBalance()) == 0) {
                //当前的accountType没钱，则继续下一个账户
                continue;
            }
            PaymentOperation currentOperation = decrease(accountAmountDO, amount, paymentRequest, accountAmountTypes,supplierStore,merchantCredit);
            amount = amount.subtract(currentOperation.getOperateAmount());
            paymentOperations.add(currentOperation);
            if (currentOperation.isEnough()) {
                break;
            }
        }
        //在循环里面已经对merchantCredit进行了更新
        merchantCreditDao.updateById(merchantCredit);
        saveDetails(paymentOperations, account, accountAmountTypes);
        return paymentOperations;
    }

    private void saveDetails(List<PaymentOperation> paymentOperations, Account account, List<AccountAmountType> accountAmountTypes) {
        List<AccountBillDetail> billDetails = paymentOperations.stream()
                .map(PaymentOperation::getAccountBillDetail)
                .collect(Collectors.toList());
        List<AccountDeductionDetail> deductionDetails = paymentOperations.stream()
                .map(PaymentOperation::getAccountDeductionDetail)
                .collect(Collectors.toList());
        List<AccountAmountType> accountTypes = paymentOperations.stream()
                .map(PaymentOperation::getAccountAmountType)
                .collect(Collectors.toList());

        BigDecimal accountBalance = AccountAmountDO.calculateAccountBalance(accountAmountTypes);
        BigDecimal accountCreditBalance = AccountAmountDO.calculateAccountCredit(accountAmountTypes);
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
                                      List<AccountAmountType> accountAmountTypes, SupplierStore supplierStore, MerchantCredit merchantCredit) {
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
        /**
         * 扣减商户账户
         */
        AccountDeductionDetail accountDeductionDetail = decreaseMerchant(
                paymentRequest,
                accountAmountType,
                operatedAmount,
                paymentOperation,
                accountAmountDO.getAccount(),
                supplierStore,
                merchantCredit
        );
        paymentOperation.setAccountDeductionDetail(accountDeductionDetail);
        return paymentOperation;

    }

    private AccountDeductionDetail decreaseMerchant(PaymentRequest paymentRequest,
                                                    AccountAmountType accountAmountType,
                                                    BigDecimal operatedAmount,
                                                    PaymentOperation paymentOperation,
                                                    Account account, SupplierStore supplierStore, MerchantCredit merchantCredit) {
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
        accountDeductionDetail.setReversedAmount(BigDecimal.ZERO);
        accountDeductionDetail.setTransTime(paymentRequest.getPaymentDate());
        accountDeductionDetail.setStoreCode(paymentRequest.getStoreNo());
        if (paymentRequest instanceof CardPaymentRequest) {
            accountDeductionDetail.setCardId(paymentRequest.getCardNo());
        }

        accountDeductionDetail.setSelfDeductionAmount(SELF.code().equals(accountAmountType.getMerAccountTypeCode()) ? operatedAmount : BigDecimal.ZERO);
        accountDeductionDetail.setAccountDeductionAmount(operatedAmount);
        //扣减商户金额

        Assert.notNull(supplierStore, "根据门店号没有找到门店");
        if (!Objects.equals(supplierStore.getMerCode(), account.getMerCode())) {
            List<MerchantAccountOperation> merchantAccountOperations = merchantCreditService.doOperateAccount(
                    merchantCredit,
                    operatedAmount,
                    paymentRequest.getTransNo(),
                    currentBalanceOperator, WelfareConstant.TransType.CONSUME.code());
            paymentOperation.setMerchantAccountOperations(merchantAccountOperations);
            Map<String, MerchantBillDetail> merBillDetailMap = merchantAccountOperations.stream().map(MerchantAccountOperation::getMerchantBillDetail)
                    .collect(Collectors.toMap(MerchantBillDetail::getBalanceType, merchantBillDetail -> merchantBillDetail));
            MerchantBillDetail currentBalanceDetail = merBillDetailMap.get(WelfareConstant.MerCreditType.CURRENT_BALANCE.code());
            MerchantBillDetail remainingLimitDetail = merBillDetailMap.get(WelfareConstant.MerCreditType.REMAINING_LIMIT.code());
            accountDeductionDetail.setMerDeductionAmount(currentBalanceDetail == null ? BigDecimal.ZERO : currentBalanceDetail.getTransAmount().abs());
            accountDeductionDetail.setMerDeductionCreditAmount(remainingLimitDetail == null ? BigDecimal.ZERO : remainingLimitDetail.getTransAmount().abs());
        } else {
            paymentOperation.setMerchantAccountOperations(Collections.emptyList());
            accountDeductionDetail.setMerDeductionAmount(BigDecimal.ZERO);
            accountDeductionDetail.setMerDeductionAmount(BigDecimal.ZERO);
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
        accountBillDetail.setOrderChannel(paymentRequest.getPaymentScene());
        BigDecimal accountBalance = AccountAmountDO.calculateAccountBalance(accountAmountTypes);
        BigDecimal accountSurplusQuota = AccountAmountDO.calculateAccountCredit(accountAmountTypes);
        accountBillDetail.setAccountBalance(accountBalance);
        accountBillDetail.setSurplusQuota(accountSurplusQuota);
        return accountBillDetail;
    }
}
