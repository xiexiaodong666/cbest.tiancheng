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
import com.welfare.common.enums.SupplierStoreStatusEnum;
import com.welfare.common.exception.BizAssert;
import com.welfare.common.exception.BizException;
import com.welfare.common.exception.ExceptionCode;
import com.welfare.common.util.DistributedLockUtil;
import com.welfare.common.util.SpringBeanUtils;
import com.welfare.persist.dao.*;
import com.welfare.persist.entity.*;
import com.welfare.service.*;
import com.welfare.service.async.AsyncService;
import com.welfare.service.dto.ThirdPartyBarcodePaymentDTO;
import com.welfare.service.dto.payment.*;
import com.welfare.service.enums.PaymentChannelOperatorEnum;
import com.welfare.service.operator.merchant.domain.MerchantAccountOperation;
import com.welfare.service.operator.payment.domain.AccountAmountDO;
import com.welfare.service.operator.payment.domain.PaymentOperation;
import com.welfare.service.payment.IPaymentOperator;
import com.welfare.service.remote.entity.request.WoLifeAccountDeductionRowsRequest;
import com.welfare.service.sync.event.PayDeductionDetailEvt;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
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
public class PaymentServiceImpl implements PaymentService, ApplicationContextAware {
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
    private final MerAccountTypeConsumeSceneConfigDao merAccountTypeConsumeSceneConfigDao;
    private final AccountAmountTypeGroupDao accountAmountTypeGroupDao;
    private final ImmutableMap<String, List<String>> SPECIAL_STORE_ACCOUNT_TYPE_MAP =
            ImmutableMap.of("2189", Arrays.asList("12", "28", "39", "40"));
    @Resource(name = "e-welfare-paymentQueryAsync")
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    private ApplicationContext applicationContext;
    @Override
    @Transactional(rollbackFor = Exception.class)
    @DistributedLock(lockPrefix = "e-welfare-payment::", lockKey = "#paymentRequest.transNo")
    public <T extends PaymentRequest> T paymentRequest(final T paymentRequest) {
        Future<? extends PaymentRequest> queryResultFuture = threadPoolTaskExecutor.submit(() ->
                queryResult(paymentRequest.getTransNo(), paymentRequest.getClass(), paymentRequest.getOrderNo()));
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
                Future<String> paymentSceneFuture = threadPoolTaskExecutor.submit(paymentRequest::calculatePaymentScene);
                Future<MerchantStoreRelation> merchantStoreRelationFuture = threadPoolTaskExecutor.submit(() ->
                        merchantStoreRelationDao.getOneByStoreCodeAndMerCodeCacheable(paymentRequest.getStoreNo(), account.getMerCode()));
                String paymentScene = paymentSceneFuture.get();
                Future<List<MerAccountTypeConsumeSceneConfig>> merAccountTypeConsumeSceneConfigFuture = threadPoolTaskExecutor.submit(() ->
                        merAccountTypeConsumeSceneConfigDao.query(account.getMerCode(), paymentRequest.getStoreNo(), paymentScene));
                Future<List<AccountConsumeSceneStoreRelation>> sceneStoreRelationsFuture = sceneStoreRelationFuture(paymentRequest, account);
                Future<List<PaymentChannelConfig>> paymentChannelConfigListFuture = threadPoolTaskExecutor.submit(() ->
                        getPaymentChannelConfigs(paymentRequest, account, paymentScene));
                //检查支付渠道消费场景
                List<PaymentChannelConfig> paymentChannelConfigs = paymentChannelConfigListFuture.get();
                T paymentRequestHandled = queryPaymentRequestHandled(paymentRequest, queryResultFuture);
                if (paymentRequestHandled != null) {
                    return paymentRequestHandled;
                }
                SupplierStore supplierStore = threadPoolTaskExecutor.submit(() -> supplierStoreService.getSupplierStoreByStoreCode(paymentRequest.getStoreNo())).get();
                List<AccountConsumeSceneStoreRelation> sceneStoreRelations = sceneStoreRelationsFuture.get();
                MerchantStoreRelation merStoreRelation = merchantStoreRelationFuture.get();
                List<MerAccountTypeConsumeSceneConfig> merAccountTypeConsumeSceneConfigs = merAccountTypeConsumeSceneConfigFuture.get();
                BizAssert.notEmpty(merAccountTypeConsumeSceneConfigs, ExceptionCode.NO_AVAILABLE_MER_ACCOUNT_TYPE_CONSUME_SCENE_CONFIG);

                MerchantCredit merchantCredit = merchantCreditService.getByMerCode(account.getMerCode());
                //支付前的校验
                chargeBeforePay(paymentRequest, account, supplierStore, merStoreRelation, sceneStoreRelations, paymentChannelConfigs);
                List<AccountAmountDO> accountAmountDOList = accountAmountTypeService.queryAccountAmountDO(account);
                accountAmountDOList = filterAvailable(accountAmountDOList, merAccountTypeConsumeSceneConfigs, paymentRequest.bizType());
                IPaymentOperator paymentOperator = getPaymentOperator(paymentRequest.getPaymentChannel());
                List<PaymentOperation> paymentOperations = paymentOperator.pay(
                        paymentRequest,
                        account,
                        accountAmountDOList,
                        supplierStore,
                        merchantCredit
                );
                List<MerchantBillDetail> merchantBillDetails = paymentOperations.stream()
                        .flatMap(paymentOperation -> paymentOperation.getMerchantAccountOperations().stream())
                        .map(MerchantAccountOperation::getMerchantBillDetail)
                        .collect(Collectors.toList());

                //支付成功要将账户的离线模式启用
                account.setOfflineLock(WelfareConstant.AccountOfflineFlag.ENABLE.code());
                List<AccountAmountType> accountAmountTypes = accountAmountDOList.stream().map(AccountAmountDO::getAccountAmountType)
                        .collect(Collectors.toList());
                //在循环里面已经对merchantCredit进行了金额重新赋值
                merchantCreditDao.updateById(merchantCredit);
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
            throw new BizException(ExceptionCode.UNKNOWN_EXCEPTION, e.getCause().getMessage(), e);
        } finally {
            DistributedLockUtil.unlock(accountLock);
        }
    }

    private IPaymentOperator getPaymentOperator(String paymentChannel) {
        PaymentChannelOperatorEnum paymentChannelOperatorEnum = PaymentChannelOperatorEnum.findByPaymentChannelStr(paymentChannel);
        return SpringBeanUtils.getBean(paymentChannelOperatorEnum.paymentOperator());
    }

    private <T extends PaymentRequest> List<PaymentChannelConfig> getPaymentChannelConfigs(T paymentRequest, Account account, String paymentScene) {
        return paymentChannelConfigDao.getBaseMapper().selectList(
                Wrappers.<PaymentChannelConfig>lambdaQuery()
                        .eq(PaymentChannelConfig::getMerCode, account.getMerCode())
                        .eq(PaymentChannelConfig::getStoreCode, paymentRequest.getStoreNo())
                        .eq(PaymentChannelConfig::getConsumeType, paymentScene)
                        .eq(PaymentChannelConfig::getPaymentChannelCode, paymentRequest.getPaymentChannel()));
    }

    private <T extends PaymentRequest> T queryPaymentRequestHandled(T paymentRequest, Future<? extends PaymentRequest> queryResultFuture) throws InterruptedException, ExecutionException {
        PaymentRequest requestHandled = queryResultFuture.get();
        if (WelfareConstant.AsyncStatus.SUCCEED.code().equals(requestHandled.getPaymentStatus())
                || WelfareConstant.AsyncStatus.REVERSED.code().equals(requestHandled.getPaymentStatus())) {
            log.warn("重复的支付请求，直接返回已经处理完成的request{}", JSON.toJSONString(requestHandled));
            BeanUtils.copyProperties(requestHandled, paymentRequest);
            @SuppressWarnings("unchecked")
            T paymentRequestHandled = (T) requestHandled;
            return paymentRequestHandled;
        }
        return null;
    }

    private List<AccountAmountDO> filterAvailable(List<AccountAmountDO> accountAmountDOList, List<MerAccountTypeConsumeSceneConfig> merAccountTypeConsumeSceneConfigs, WelfareConstant.PaymentBizType bizType) {
        List<String> configs = merAccountTypeConsumeSceneConfigs.stream()
                .map(MerAccountTypeConsumeSceneConfig::getMerAccountTypeCode)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(configs)) {
            return Collections.emptyList();
        }
        if (WelfareConstant.PaymentBizType.HOSPITAL_POINTS.equals(bizType)) {
            accountAmountDOList = accountAmountDOList.stream()
                    .filter(amountDO ->
                            WelfareConstant.MerAccountTypeCode.MALL_POINT.code()
                                    .equals(amountDO.getAccountAmountType().getMerAccountTypeCode()))
                    .collect(Collectors.toList());
        } else {
            accountAmountDOList = accountAmountDOList.stream()
                    .filter(amountDO -> configs.contains(amountDO.getAccountAmountType().getMerAccountTypeCode())
                            && !WelfareConstant.MerAccountTypeCode.MALL_POINT.code()
                            .equals(amountDO.getAccountAmountType().getMerAccountTypeCode()))
                    .collect(Collectors.toList());
        }

        return accountAmountDOList;
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
     *
     * @param <T>            付款请求类型
     * @param paymentRequest 付款请求
     * @param account        账户
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
     *  @param paymentRequest        付款请求
     * @param account               账户
     * @param supplierStore         门店
     * @param sceneStoreRelations   消费场景与门店关联列表
     * @param paymentChannelConfigs 支付渠道配置
     */
    private void chargeBeforePay(PaymentRequest paymentRequest, Account account, SupplierStore supplierStore, MerchantStoreRelation merStoreRelation, List<AccountConsumeSceneStoreRelation> sceneStoreRelations, List<PaymentChannelConfig> paymentChannelConfigs) {
        Assert.isTrue(AccountStatus.ENABLE.getCode().equals(account.getAccountStatus()), "账户未启用");
        Assert.notNull(merStoreRelation, "用户所在组织（公司）不支持在该门店消费或配置已禁用");
        Assert.isTrue(EnableEnum.ENABLE.getCode().equals(merStoreRelation.getStatus()), "用户所在组织（公司）不支持在该门店消费或配置已禁用");
        Assert.isTrue(SupplierStoreStatusEnum.ACTIVATED.getCode().equals(supplierStore.getStatus()),
                "门店未激活:" + supplierStore.getStoreCode());
        //写死的逻辑，紧急上线，没有时间重新设计逻辑，支持店中店模式
        if (ConsumeTypeEnum.SHOP_SHOPPING.getCode().equals(paymentRequest.getPaymentScene())
                && SPECIAL_STORE_ACCOUNT_TYPE_MAP.containsKey(paymentRequest.getStoreNo())
                && SPECIAL_STORE_ACCOUNT_TYPE_MAP.get(paymentRequest.getStoreNo()).contains(account.getAccountTypeCode())) {
            Assert.isTrue("9001".equals(paymentRequest.getMachineNo()), "当前用户仅允许在9001收银机消费");
        }
        Assert.isTrue(!CollectionUtils.isEmpty(sceneStoreRelations), "未找到该门店的可用交易场景配置");
        List<String> sceneConsumeTypes = sceneStoreRelations.stream().map(relation -> Arrays.asList(relation.getSceneConsumType().split(",")))
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());
        String paymentScene = paymentRequest.getPaymentScene();
        if (!sceneConsumeTypes.contains(paymentScene)) {
            log.error("当前用户不支持此消费场景:" + ConsumeTypeEnum.getByType(paymentScene).getDesc());
            throw new BizException(ExceptionCode.ILLEGALITY_ARGUMENTS, "当前门店不支持该支付方式", null);
        }
        if (CollectionUtils.isEmpty(paymentChannelConfigs)) {
            log.error("当前用户不支持此支付渠道:" + ConsumeTypeEnum.getByType(paymentScene).getDesc());
            throw new BizException(ExceptionCode.ILLEGALITY_ARGUMENTS, "当前门店不支持该支付方式");
        }

        List<WoLifeAccountDeductionRowsRequest> saleRows = paymentRequest.getSaleRows();
        if (!CollectionUtils.isEmpty(saleRows) && WelfareConstant.PaymentChannel.WO_LIFE.code().equals(paymentRequest.getPaymentChannel())) {
            // 线上沃生活check 订单总金额和商品行金额的和是否相等
            BigDecimal amount = paymentRequest.getAmount();
            BigDecimal rowsAmount = saleRows.stream()
                    .map(row -> row.getPrice().multiply(new BigDecimal(row.getCount())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            Assert.isTrue(amount.compareTo(rowsAmount) == 0, "订单总金额与商品合计金额不相等,请检查上游服务");
        }

    }

    @Override
    @SneakyThrows
    public <T extends PaymentRequest> T queryResult(String transNo, Class<T> clazz, String orderNo) {
        List<AccountBillDetail> accountDeductionDetails = accountBillDetailDao.queryByTransNoOrderNoAndTransType(
                transNo, orderNo,
                WelfareConstant.TransType.CONSUME.code());
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
            if (clazz.equals(CardPaymentRequest.class)) {
                ((CardPaymentRequest) paymentRequest).setCardNo(firstAccountBillDetail.getCardId());
            } else if (clazz.equals(BarcodePaymentRequest.class)) {
                ((BarcodePaymentRequest) paymentRequest).setBarcode(firstAccountBillDetail.getPaymentTypeInfo());
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
        List<AccountBillDetail> billDetails = paymentOperations.stream().map(PaymentOperation::getAccountBillDetail)
                .collect(Collectors.toList());
        List<AccountDeductionDetail> deductionDetails = paymentOperations.stream().map(PaymentOperation::getAccountDeductionDetail)
                .collect(Collectors.toList());
        List<AccountAmountType> accountTypes = paymentOperations.stream().map(PaymentOperation::getAccountAmountType)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        List<AccountAmountTypeGroup> accountAmountTypeGroups = paymentOperations.stream().map(PaymentOperation::getAccountAmountTypeGroup)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        AccountAmountDO.updateAccountAfterOperated(account, accountAmountTypes);
        accountDao.updateById(account);
        accountBillDetailDao.saveBatch(billDetails);
        accountDeductionDetailDao.saveBatch(deductionDetails);
        if (!CollectionUtils.isEmpty(accountAmountTypeGroups)) {
            accountAmountTypeGroupDao.updateBatchById(accountAmountTypeGroups);
        }
        if (!CollectionUtils.isEmpty(accountTypes)) {
            //联通沃支付，没有修改accountTypes，所以if判断
            accountAmountTypeDao.saveOrUpdateBatch(accountTypes);
        }
        List<Long> accountDeductionDetailIds = deductionDetails.stream()
                .map(AccountDeductionDetail::getId).collect(Collectors.toList());
        PayDeductionDetailEvt payDeductionDetailEvt = PayDeductionDetailEvt.builder()
                .accountDeductionDetailIds(accountDeductionDetailIds)
                .build();
        applicationContext.publishEvent(payDeductionDetailEvt);
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
            List<AccountConsumeSceneStoreRelation> sceneStoreRelations = sceneStoreRelationsFuture.get();

            MerchantStoreRelation merStoreRelation = merchantStoreRelationFuture.get();
            List<PaymentChannelConfig> paymentChannelConfigList = paymentChannelConfigListFuture.get();

            chargeBeforePay(
                    paymentRequest,
                    account,
                    supplierStore,
                    merStoreRelation,
                    sceneStoreRelations,
                    paymentChannelConfigList
            );

            SubAccount subAccount = subAccountFuture.get();
            ThirdPartyBarcodePaymentDTO thirdPartyBarcodePaymentDTO = new ThirdPartyBarcodePaymentDTO();
            thirdPartyBarcodePaymentDTO.setAccountCode(accountCode);
            thirdPartyBarcodePaymentDTO.setAccountName(account.getAccountName());
            thirdPartyBarcodePaymentDTO.setPhone(account.getPhone());
            thirdPartyBarcodePaymentDTO.setSurplusQuota(account.getSurplusQuota());
            thirdPartyBarcodePaymentDTO.setAccountBalance(account.getAccountBalance());
            if (subAccount != null) {
                thirdPartyBarcodePaymentDTO.setPasswordFreeSignature(subAccount.getPasswordFreeSignature());
            }
            return thirdPartyBarcodePaymentDTO;
        } catch (InterruptedException | ExecutionException e) {
            log.error(StrUtil.format("查询检查第三方支付码异步执行异常, paymentRequest: {}", JSON.toJSON(paymentRequest)), e);
            throw new BizException(ExceptionCode.UNKNOWN_EXCEPTION, "系统异常", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MultiOrderPaymentRequest multiOrderUnionPay(MultiOrderPaymentRequest multiOrderPaymentRequest) {
        //为了使各种aop切面生效，从容器中拿当前paymentService
        PaymentService paymentService = SpringBeanUtils.getBean(PaymentService.class);
        List<OnlinePaymentRequest> onlinePaymentRequests = multiOrderPaymentRequest.toOnlinePaymentRequests();
        for (OnlinePaymentRequest onlinePaymentRequest : onlinePaymentRequests) {
            paymentService.paymentRequest(onlinePaymentRequest);
        }
        return multiOrderPaymentRequest;
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
