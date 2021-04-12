package com.welfare.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.ImmutableMap;
import com.welfare.common.annotation.DistributedLock;
import com.welfare.common.constants.AccountStatus;
import com.welfare.common.constants.RedisKeyConstant;
import com.welfare.common.constants.WelfareConstant;
import com.welfare.common.enums.ConsumeTypeEnum;
import com.welfare.common.enums.EnableEnum;
import com.welfare.common.enums.PaymentTypeEnum;
import com.welfare.common.enums.SupplierStoreStatusEnum;
import com.welfare.common.exception.BizException;
import com.welfare.common.exception.ExceptionCode;
import com.welfare.common.util.DistributedLockUtil;
import com.welfare.common.util.SpringBeanUtils;
import com.welfare.persist.dao.*;
import com.welfare.persist.entity.*;
import com.welfare.service.*;
import com.welfare.service.async.AsyncService;
import com.welfare.service.dto.ThirdPartyBarcodePaymentDTO;
import com.welfare.service.dto.payment.BarcodePaymentRequest;
import com.welfare.service.dto.payment.CardPaymentRequest;
import com.welfare.service.dto.payment.PaymentRequest;
import com.welfare.service.enums.PaymentChannelOperatorEnum;
import com.welfare.service.operator.merchant.domain.MerchantAccountOperation;
import com.welfare.service.operator.payment.domain.AccountAmountDO;
import com.welfare.service.operator.payment.domain.PaymentOperation;
import com.welfare.service.payment.IPaymentOperator;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
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
    private final MerchantBillDetailDao merchantBillDetailDao;
    private final AccountConsumeSceneDao accountConsumeSceneDao;
    private final AccountConsumeSceneStoreRelationDao accountConsumeSceneStoreRelationDao;
    private final SupplierStoreService supplierStoreService;
    private final MerchantStoreRelationDao merchantStoreRelationDao;
    private final MerchantCreditDao merchantCreditDao;
    private final AsyncService asyncService;
    private final PaymentChannelConfigDao paymentChannelConfigDao;
    private final SubAccountDao subAccountDao;
    private final ImmutableMap<String,List<String>> SPECIAL_STORE_ACCOUNT_TYPE_MAP =
            ImmutableMap.of("2189",Arrays.asList("12","28","39","40"));
    @Resource(name = "e-welfare-paymentQueryAsync")
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Override
    @Transactional(rollbackFor = Exception.class)
    @DistributedLock(lockPrefix = "e-welfare-payment::", lockKey = "#paymentRequest.transNo")
    public <T extends PaymentRequest>T paymentRequest(final T paymentRequest) {
        Future<? extends PaymentRequest> queryResultFuture = threadPoolTaskExecutor.submit(() -> queryResult(paymentRequest.getTransNo(),paymentRequest.getClass()));
        Long accountCode = paymentRequest.calculateAccountCode();
        Assert.notNull(accountCode, "账号不能为空。");
        String lockKey = RedisKeyConstant.ACCOUNT_AMOUNT_TYPE_OPERATE + accountCode;
        RLock accountLock = DistributedLockUtil.lockFairly(lockKey);
        try {
            Account account = accountService.getByAccountCode(accountCode);
            Assert.notNull(account, "未找到账号：" + accountCode);
            paymentRequest.setAccountMerCode(account.getMerCode());
            RLock merAccountLock = DistributedLockUtil.lockFairly(MER_ACCOUNT_TYPE_OPERATE + ":" + account.getMerCode());
            try {
                //整体异步查询
                Future<SupplierStore> supplierStoreFuture = threadPoolTaskExecutor.submit(() -> supplierStoreService.getSupplierStoreByStoreCode(paymentRequest.getStoreNo()));
                Future<String> paymentSceneFuture = threadPoolTaskExecutor.submit(paymentRequest::calculatePaymentScene);
                Future<List<AccountAmountDO>> accountAmountDoFuture =
                        threadPoolTaskExecutor.submit(() -> accountAmountTypeService.queryAccountAmountDO(account));
                Future<MerchantStoreRelation> merchantStoreRelationFuture = threadPoolTaskExecutor.submit(() ->
                        merchantStoreRelationDao.getOneByStoreCodeAndMerCodeCacheable(paymentRequest.getStoreNo(), account.getMerCode()));
                Future<MerchantCredit> merchantCreditFuture =
                        threadPoolTaskExecutor.submit(() -> merchantCreditService.getByMerCode(account.getMerCode()));
                Future<List<AccountConsumeSceneStoreRelation>> sceneStoreRelationsFuture = sceneStoreRelationFuture(paymentRequest, account);
                //获取异步结果
                PaymentRequest requestHandled = queryResultFuture.get();
                if (WelfareConstant.AsyncStatus.SUCCEED.code().equals(requestHandled.getPaymentStatus())
                        || WelfareConstant.AsyncStatus.REVERSED.code().equals(requestHandled.getPaymentStatus())) {
                    log.warn("重复的支付请求，直接返回已经处理完成的request{}", JSON.toJSONString(requestHandled));
                    BeanUtils.copyProperties(requestHandled, paymentRequest);
                    @SuppressWarnings("unchecked")
                    T paymentRequestHandled = (T) requestHandled;
                    return paymentRequestHandled;
                }
                SupplierStore supplierStore = supplierStoreFuture.get();
                List<AccountConsumeSceneStoreRelation> sceneStoreRelations = sceneStoreRelationsFuture.get();
                String paymentScene = paymentSceneFuture.get();
                MerchantStoreRelation merStoreRelation = merchantStoreRelationFuture.get();
                List<AccountAmountDO> accountAmountDOList = accountAmountDoFuture.get();
                MerchantCredit merchantCredit = merchantCreditFuture.get();
                //支付前的校验
                chargeBeforePay(paymentRequest, account, supplierStore, merStoreRelation, paymentScene, sceneStoreRelations);

                String paymentChannel = paymentRequest.getPaymentChannel();
                List<MerchantBillDetail> merchantBillDetails;
                PaymentChannelOperatorEnum paymentChannelOperatorEnum = PaymentChannelOperatorEnum.findByPaymentChannelStr(paymentChannel);
                IPaymentOperator paymentOperator = SpringBeanUtils.getBean(paymentChannelOperatorEnum.paymentOperator());

                List<PaymentOperation> paymentOperations = paymentOperator.pay(
                        paymentRequest,
                        account,
                        accountAmountDOList,
                        supplierStore,
                        merchantCredit
                );
                merchantBillDetails = paymentOperations.stream()
                        .flatMap(paymentOperation -> paymentOperation.getMerchantAccountOperations().stream())
                        .map(MerchantAccountOperation::getMerchantBillDetail)
                        .collect(Collectors.toList());
                //执行更新数据库
                List<AccountAmountType> accountAmountTypes = accountAmountDOList.stream().map(AccountAmountDO::getAccountAmountType)
                        .collect(Collectors.toList());
                //在循环里面已经对merchantCredit进行了更新
                merchantCreditDao.updateById(merchantCredit);
                //支付成功要将账户的离线模式启用
                account.setOfflineLock(WelfareConstant.AccountOfflineFlag.ENABLE.code());
                saveDetails(paymentOperations, account, accountAmountTypes);
                if (!CollectionUtils.isEmpty(merchantBillDetails)) {
                    merchantBillDetailDao.saveBatch(merchantBillDetails);
                }
                fillPaymentRequest(paymentRequest, account);
                if (ConsumeTypeEnum.SHOP_SHOPPING.getCode().equals(paymentRequest.getPaymentScene())) {
                    asyncService.paymentNotify(paymentRequest.getPhone(), paymentRequest.getAmount());
                }
            } finally {
                DistributedLockUtil.unlock(merAccountLock);
            }
            return paymentRequest;
        } catch (InterruptedException | ExecutionException e) {
            log.error("异步执行查询异常", e);
            throw new BizException(ExceptionCode.UNKNOWON_EXCEPTION, e.getCause().getMessage(), e);
        } finally {
            DistributedLockUtil.unlock(accountLock);
        }

    }

    private <T extends PaymentRequest> Future<List<AccountConsumeSceneStoreRelation>> sceneStoreRelationFuture(T paymentRequest, Account account) {
        return threadPoolTaskExecutor.submit(() -> {
            List<AccountConsumeScene> accountConsumeScenes = accountConsumeSceneDao
                    .getAccountTypeAndMerCode(account.getAccountTypeCode(), account.getMerCode());
            Assert.isTrue(!CollectionUtils.isEmpty(accountConsumeScenes), "未找到该账户的可用交易场景配置");
            List<Long> sceneIds = accountConsumeScenes.stream().map(AccountConsumeScene::getId).collect(Collectors.toList());
            return accountConsumeSceneStoreRelationDao
                    .queryBySceneIdsAndStoreNo(sceneIds, paymentRequest.getStoreNo());
        });
    }

    /**
     * 填充paymentRequest
     * @param <T> 付款请求类型
     * @param paymentRequest 付款请求
     * @param account 账户
     */
    private <T extends PaymentRequest> void fillPaymentRequest(T paymentRequest, Account account) {
        paymentRequest.setPaymentStatus(WelfareConstant.AsyncStatus.SUCCEED.code());
        paymentRequest.setAccountName(account.getAccountName());
        paymentRequest.setAccountBalance(account.getAccountBalance());
        paymentRequest.setAccountCredit(account.getSurplusQuota().add(account.getSurplusQuotaOverpay()));
        paymentRequest.setPhone(account.getPhone());
    }


    /**
     * 判断消费场景是否符合配置
     *
     * @param paymentRequest 付款请求
     * @param account 账户
     * @param supplierStore 门店
     * @param paymentScene 支付场景
     * @param sceneStoreRelations 消费场景与门店关联列表
     */
    private void chargeBeforePay(PaymentRequest paymentRequest, Account account, SupplierStore supplierStore, MerchantStoreRelation merStoreRelation, String paymentScene, List<AccountConsumeSceneStoreRelation> sceneStoreRelations) {
        Assert.isTrue(AccountStatus.ENABLE.getCode().equals(account.getAccountStatus()), "账户未启用");
        Assert.notNull(merStoreRelation, "用户所在组织（公司）不支持在该门店消费或配置已禁用");
        Assert.isTrue(EnableEnum.ENABLE.getCode().equals(merStoreRelation.getStatus()), "用户所在组织（公司）不支持在该门店消费或配置已禁用");
        Assert.isTrue(SupplierStoreStatusEnum.ACTIVATED.getCode().equals(supplierStore.getStatus()),
                "门店未激活:" + supplierStore.getStoreCode());

        //写死的逻辑，紧急上线，没有时间重新设计逻辑，支持店中店模式
        if(ConsumeTypeEnum.SHOP_SHOPPING.getCode().equals(paymentScene)
                && SPECIAL_STORE_ACCOUNT_TYPE_MAP.containsKey(paymentRequest.getStoreNo())
                && SPECIAL_STORE_ACCOUNT_TYPE_MAP.get(paymentRequest.getStoreNo()).contains(account.getAccountTypeCode())){
            Assert.isTrue("9001".equals(paymentRequest.getMachineNo()),"当前用户仅允许在9001收银机消费");
        }
        Assert.isTrue(!CollectionUtils.isEmpty(sceneStoreRelations), "未找到该门店的可用交易场景配置");
        List<String> sceneConsumeTypes = sceneStoreRelations.stream().map(relation -> Arrays.asList(relation.getSceneConsumType().split(",")))
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());
        if (!sceneConsumeTypes.contains(paymentScene)) {
            throw new BizException(ExceptionCode.ILLEGALITY_ARGURMENTS, "当前用户不支持此消费场景:" + ConsumeTypeEnum.getByType(paymentScene).getDesc(), null);
        }
    }

    @Override
    @SneakyThrows
    public <T extends PaymentRequest> T queryResult(String transNo,Class<T> clazz) {
        List<AccountBillDetail> accountDeductionDetails = accountBillDetailDao.queryByTransNoAndTransType(
                transNo,
                WelfareConstant.TransType.CONSUME.code()
        );
        List<AccountDeductionDetail> refundDeductionDetails = accountDeductionDetailDao.queryByRelatedTransNoAndTransType(
                transNo,
                WelfareConstant.TransType.REFUND.code()
        );
        T paymentRequest = clazz.newInstance();
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
            if(clazz.equals(CardPaymentRequest.class)){
                ((CardPaymentRequest)paymentRequest).setCardNo(firstAccountBillDetail.getCardId());
            }else if(clazz.equals(BarcodePaymentRequest.class)){
                ((BarcodePaymentRequest)paymentRequest).setBarcode(firstAccountBillDetail.getPaymentTypeInfo());
            }
            @SuppressWarnings("duplicate")
            BigDecimal amount = accountDeductionDetails.stream()
                    .map(AccountBillDetail::getTransAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            paymentRequest.setAmount(amount);
            Account account = accountService.getByAccountCode(firstAccountBillDetail.getAccountCode());
            paymentRequest.setAccountMerCode(account.getMerCode());
            paymentRequest.setAccountBalance(account.getAccountBalance());
            paymentRequest.setAccountName(account.getAccountName());
            paymentRequest.setAccountCredit(account.getSurplusQuota());
            paymentRequest.setPhone(account.getPhone());
        }

        return paymentRequest;
    }


    private void saveDetails(List<PaymentOperation> paymentOperations, Account account, List<AccountAmountType> accountAmountTypes) {
        List<AccountBillDetail> billDetails = paymentOperations.stream()
                .map(PaymentOperation::getAccountBillDetail)
                .collect(Collectors.toList());
        List<AccountDeductionDetail> deductionDetails = paymentOperations.stream()
                .map(PaymentOperation::getAccountDeductionDetail)
                .collect(Collectors.toList());
        List<AccountAmountType> accountTypes = paymentOperations.stream()
                .map(PaymentOperation::getAccountAmountType).filter(Objects::nonNull)
                .collect(Collectors.toList());
        AccountAmountDO.updateAccountAfterOperated(account, accountAmountTypes);
        accountDao.updateById(account);
        accountBillDetailDao.saveBatch(billDetails);
        accountDeductionDetailDao.saveBatch(deductionDetails);
        if(!CollectionUtils.isEmpty(accountTypes)){
            //联通沃支付，没有修改accountTypes，所以if判断
            accountAmountTypeDao.saveOrUpdateBatch(accountTypes);
        }
    }


    @Override
    public ThirdPartyBarcodePaymentDTO thirdPartyBarcodePaymentSceneCheck(
        BarcodePaymentRequest paymentRequest) {
        try {
            Long accountCode = paymentRequest.calculateAccountCode();
            Assert.notNull(accountCode, "账号不能为空。");
            Account account = accountService.getByAccountCode(accountCode);
            Assert.notNull(account, "未找到账号：" + accountCode);
            Future<SupplierStore> supplierStoreFuture = threadPoolTaskExecutor.submit(
                () -> supplierStoreService
                    .getSupplierStoreByStoreCode(paymentRequest.getStoreNo()));
            Future<MerchantStoreRelation> merchantStoreRelationFuture = threadPoolTaskExecutor
                .submit(() ->
                    merchantStoreRelationDao
                        .getOneByStoreCodeAndMerCodeCacheable(paymentRequest.getStoreNo(),
                            account.getMerCode()));
            Future<List<AccountConsumeSceneStoreRelation>> sceneStoreRelationsFuture = sceneStoreRelationFuture(paymentRequest, account);
            Future<String> paymentSceneFuture = threadPoolTaskExecutor
                .submit(paymentRequest::calculatePaymentScene);
            String merCode = account.getMerCode();
            String storeNo = paymentRequest.getStoreNo();
            String paymentScene = paymentSceneFuture.get();
            String paymentChannel = paymentRequest.getPaymentChannel();
            Future<List<PaymentChannelConfig>> paymentChannelConfigListFuture = threadPoolTaskExecutor.submit(() -> paymentChannelConfigDao
                .getBaseMapper().selectList(
                    Wrappers.<PaymentChannelConfig>lambdaQuery()
                        .eq(PaymentChannelConfig::getMerCode, merCode)
                        .eq(PaymentChannelConfig::getStoreCode, storeNo)
                        .eq(PaymentChannelConfig::getConsumeType, paymentScene)
                        .eq(PaymentChannelConfig::getPaymentChannelCode, paymentChannel)));
            Future<SubAccount> subAccountFuture = threadPoolTaskExecutor.submit(() -> subAccountDao.getBaseMapper().selectOne(
                Wrappers.<SubAccount>lambdaQuery().eq(SubAccount::getAccountCode, accountCode)
                    .eq(SubAccount::getSubAccountType, paymentChannel)));

            SupplierStore supplierStore = supplierStoreFuture.get();
            List<AccountConsumeSceneStoreRelation> sceneStoreRelations = sceneStoreRelationsFuture
                .get();

            MerchantStoreRelation merStoreRelation = merchantStoreRelationFuture.get();
            //检查门店消费场景
            chargeBeforePay(paymentRequest, account, supplierStore, merStoreRelation, paymentScene,
                sceneStoreRelations);
            //检查支付渠道消费场景
            List<PaymentChannelConfig> paymentChannelConfigList = paymentChannelConfigListFuture
                .get();
            if(CollectionUtils.isEmpty(paymentChannelConfigList)) {
                throw new BizException(ExceptionCode.ILLEGALITY_ARGURMENTS, "当前用户不支持此消费场景:" + ConsumeTypeEnum.getByType(paymentScene).getDesc(), null);
            }
            SubAccount subAccount = subAccountFuture.get();
            ThirdPartyBarcodePaymentDTO thirdPartyBarcodePaymentDTO = new ThirdPartyBarcodePaymentDTO();
            thirdPartyBarcodePaymentDTO.setAccountCode(accountCode);
            thirdPartyBarcodePaymentDTO.setAccountName(account.getAccountName());
            thirdPartyBarcodePaymentDTO.setPhone(account.getPhone());
            thirdPartyBarcodePaymentDTO.setSurplusQuota(account.getSurplusQuota());
            thirdPartyBarcodePaymentDTO.setAccountBalance(account.getAccountBalance());
            if(subAccount != null) {
                thirdPartyBarcodePaymentDTO.setPasswordFreeSignature(subAccount.getPasswordFreeSignature());
            }
            return thirdPartyBarcodePaymentDTO;
        } catch (InterruptedException | ExecutionException e) {
            log.error(StrUtil.format("查询检查第三方支付码异步执行异常, paymentRequest: {}", JSON.toJSON(paymentRequest)), e);
            throw new BizException(ExceptionCode.UNKNOWON_EXCEPTION, "系统异常", e);
        }
    }

}
