package com.welfare.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.welfare.common.constants.WelfareConstant;
import com.welfare.common.constants.WelfareConstant.PaymentChannel;
import com.welfare.common.constants.WelfareConstant.TransType;
import com.welfare.common.enums.PaymentTypeEnum;
import com.welfare.common.exception.BizException;
import com.welfare.common.exception.ExceptionCode;
import com.welfare.common.util.SpringBeanUtils;
import com.welfare.persist.dao.SubAccountDao;
import com.welfare.persist.dto.ThirdPartyPaymentRequestDao;
import com.welfare.persist.entity.SubAccount;
import com.welfare.persist.entity.ThirdPartyPaymentRequest;
import com.welfare.service.AccountPaymentResultService;
import com.welfare.service.BarcodeService;
import com.welfare.service.dto.BarcodePaymentNotifyReq;
import com.welfare.service.dto.BarcodePaymentResultDTO;
import com.welfare.service.dto.BarcodePaymentResultReq;
import com.welfare.service.dto.CreateThirdPartyPaymentDTO;
import com.welfare.service.dto.CreateThirdPartyPaymentNotifyReq;
import com.welfare.service.dto.CreateThirdPartyPaymentReq;
import com.welfare.service.dto.ThirdPartyPaymentResultNotifyReq;
import com.welfare.service.remote.entity.AlipayUserAgreementQueryResp;
import com.welfare.service.remote.entity.CbestPayBaseResp;
import com.welfare.service.remote.entity.CbestPayRespStatusConstant;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AccountPaymentResultServiceImpl implements AccountPaymentResultService {

    @Resource
    private RedissonClient redissonClient;

    @Resource
    private SubAccountDao subAccountDao;

    @Resource
    private ThirdPartyPaymentRequestDao thirdPartyPaymentRequestDao;

    public static final String KEY_PREFIX = "e-welfare_account_payment_result:{}:{}";

    public static final String CREATE_THIRD_PARTY_PAYMENT_KEY_PREFIX = "create_third_party_payment:{}";


    @Override
    public void barcodePaymentNotify(BarcodePaymentNotifyReq req) {
        log.info(StrUtil.format("扫码支付通知-{}", JSON.toJSONString(req)));
        req.setAccountBalance(fenToYuan(req.getAccountBalance()));
        req.setAccountCreedit(fenToYuan(req.getAccountCreedit()));
        req.setTotalAmount(fenToYuan(req.getTotalAmount()));
        req.setReceiptAmount(fenToYuan(req.getReceiptAmount()));
        req.setActualAmount(fenToYuan(req.getActualAmount()));
        req.setTotalDiscountAmount(fenToYuan(req.getTotalDiscountAmount()));
        req.setChannelDiscountAmount(fenToYuan(req.getChannelDiscountAmount()));
        req.setMerchantDiscountAmount(fenToYuan(req.getMerchantDiscountAmount()));
        RBucket<BarcodePaymentNotifyReq> bucket = redissonClient.<BarcodePaymentNotifyReq>getBucket(
            StrUtil.format(KEY_PREFIX, req.getAccountCode(), req.getBarcode()));
        bucket.set(req, 1, TimeUnit.MINUTES);
    }

    private String fenToYuan(String amount) {
        try {
            if (amount == null) {
                return null;
            }
            BigDecimal bigDecimal = new BigDecimal(amount)
                .divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP);
            return bigDecimal.toString();
        } catch (Exception e) {
            log.error("金额单位从元转分异常", e);
            return null;
        }
    }

    @Override
    public BarcodePaymentResultDTO queryBarcodePaymentResult(BarcodePaymentResultReq req) {
        RBucket<BarcodePaymentNotifyReq> bucket = redissonClient
            .getBucket(StrUtil.format(KEY_PREFIX, req.getAccountCode(), req.getBarcode()));
        BarcodePaymentNotifyReq barcodePaymentNotifyReq = bucket.get();
        if (barcodePaymentNotifyReq == null) {
            return null;
        }
        BarcodePaymentResultDTO barcodePaymentResultDTO = new BarcodePaymentResultDTO();
        BeanUtil.copyProperties(barcodePaymentNotifyReq, barcodePaymentResultDTO);
        return barcodePaymentResultDTO;
    }

    @Override
    public void thirdPartyPaymentResultNotify(CbestPayBaseResp resp) {
        String status = resp.getStatus();
        if (!CbestPayRespStatusConstant.SUCCESS.equals(status)) {
            log.error("第三方支付结果通知返回非成功状态， resp: {}", JSON.toJSONString(resp));
        }
        String bizStatus = resp.getBizStatus();
        if (!CbestPayRespStatusConstant.SUCCESS.equals(bizStatus)) {
            log.error("第三方支付结果通知业务状态返回非成功状态， resp: {}", JSON.toJSONString(resp));
            return;
        }
        String bizContent = resp.getBizContent();
        ThirdPartyPaymentResultNotifyReq thirdPartyPaymentResultNotifyReq = JSON
            .parseObject(bizContent, ThirdPartyPaymentResultNotifyReq.class);
        BarcodePaymentNotifyReq req = new BarcodePaymentNotifyReq();
        BeanUtils.copyProperties(thirdPartyPaymentResultNotifyReq, req);
        req.setTotalAmount(fenToYuan(thirdPartyPaymentResultNotifyReq.getTotalAmount()));
        req.setActualAmount(fenToYuan(thirdPartyPaymentResultNotifyReq.getActualAmount()));
        req.setTotalDiscountAmount(
            fenToYuan(thirdPartyPaymentResultNotifyReq.getTotalDiscountAmount()));
        String barcode = thirdPartyPaymentResultNotifyReq.getBarcode();
        Long accountCode = calculateAccountCode(barcode);
        //缓存第三方支付结果通知
        RBucket<BarcodePaymentNotifyReq> bucket = redissonClient.<BarcodePaymentNotifyReq>getBucket(
            StrUtil.format(KEY_PREFIX, accountCode, barcode));
        bucket.set(req, 1, TimeUnit.MINUTES);

        ThirdPartyPaymentRequest thirdPartyPaymentRequest = thirdPartyPaymentRequestDao
                .getBaseMapper().selectOne(
                        Wrappers.<ThirdPartyPaymentRequest>lambdaQuery()
                                .eq(ThirdPartyPaymentRequest::getTransNo,
                                        thirdPartyPaymentResultNotifyReq.getGatewayTradeNo())
                                .eq(ThirdPartyPaymentRequest::getAccountCode, accountCode)
                                .eq(ThirdPartyPaymentRequest::getTransType,
                                        TransType.CONSUME.code()));
        if(Objects.isNull(thirdPartyPaymentRequest)){
            thirdPartyPaymentRequest = new ThirdPartyPaymentRequest();
            thirdPartyPaymentRequest.setPaymentRequest(JSON.toJSONString(req));
            thirdPartyPaymentRequest.setPaymentRequestType("alipay-password-free");
            thirdPartyPaymentRequest.setTransStatus(WelfareConstant.AsyncStatus.SUCCEED.code());
            thirdPartyPaymentRequest.setPaymentChannel(PaymentChannel.ALIPAY.code());
            thirdPartyPaymentRequest.setPaymentType(PaymentTypeEnum.BARCODE.getCode());
            thirdPartyPaymentRequest.setPaymentTypeInfo(req.getBarcode());
            thirdPartyPaymentRequest.setTransType(TransType.CONSUME.code());
            thirdPartyPaymentRequest.setTransAmount(new BigDecimal(req.getActualAmount()));
            thirdPartyPaymentRequest.setTransNo(req.getGatewayTradeNo());
            thirdPartyPaymentRequest.setAccountCode(Long.valueOf(req.getAccountCode()));
            thirdPartyPaymentRequestDao.save(thirdPartyPaymentRequest);
        }else{
            thirdPartyPaymentRequest.setPaymentRequest(JSON.toJSONString(req));
            if(req.getBarcode().startsWith(PaymentChannel.WECHAT.barcodePrefix())){
                thirdPartyPaymentRequest.setPaymentRequestType(PaymentChannel.WECHAT.code());
                thirdPartyPaymentRequest.setPaymentChannel(PaymentChannel.WECHAT.code());
            }else if(req.getBarcode().startsWith(PaymentChannel.ALIPAY.barcodePrefix())){
                thirdPartyPaymentRequest.setPaymentRequestType(PaymentChannel.ALIPAY.code());
                thirdPartyPaymentRequest.setPaymentChannel(PaymentChannel.ALIPAY.code());
            }else{
                log.warn("支付通知收到不支持的条码:{}",req.getBarcode());
                throw new BizException(ExceptionCode.ILLEGALITY_ARGURMENTS,"不支持的条码"+req.getBarcode());
            }
            thirdPartyPaymentRequest.setTransStatus(WelfareConstant.AsyncStatus.SUCCEED.code());
            thirdPartyPaymentRequestDao.updateById(thirdPartyPaymentRequest);
        }

    }

    private Long calculateAccountCode(String barcode) {
        BarcodeService barcodeService = SpringBeanUtils.getBean(BarcodeService.class);
        Long accountCode = barcodeService
            .parseAccountFromBarcode(barcode, Calendar.getInstance().getTime(), true, false);
        return accountCode;
    }

    @Override
    public void createThirdPartyPaymentNotify(CbestPayBaseResp resp) {
        String status = resp.getStatus();
        if (!CbestPayRespStatusConstant.SUCCESS.equals(status)) {
            log.error("第三方交易创建通知返回非成功状态， resp: {}", JSON.toJSONString(resp));
        }
        String bizStatus = resp.getBizStatus();
        if (!CbestPayRespStatusConstant.SUCCESS.equals(bizStatus)) {
            log.error("第三方交易创建通知业务状态返回非成功状态， resp: {}", JSON.toJSONString(resp));
            return;
        }
        String bizContent = resp.getBizContent();
        CreateThirdPartyPaymentNotifyReq req = JSON
            .parseObject(bizContent, CreateThirdPartyPaymentNotifyReq.class);
        //缓存第三方交易创建通知
        RBucket<CreateThirdPartyPaymentNotifyReq> bucket = redissonClient.<CreateThirdPartyPaymentNotifyReq>getBucket(
            StrUtil.format(CREATE_THIRD_PARTY_PAYMENT_KEY_PREFIX, req.getBarcode()));
        bucket.set(req, 1, TimeUnit.MINUTES);

        //保存第三方交易为支付中
        ThirdPartyPaymentRequest thirdPartyPaymentRequest = buildThirdPartyPaymentRequest(req);
        thirdPartyPaymentRequestDao.save(thirdPartyPaymentRequest);
    }

    private ThirdPartyPaymentRequest buildThirdPartyPaymentRequest(
        CreateThirdPartyPaymentNotifyReq req) {
        ThirdPartyPaymentRequest thirdPartyPaymentRequest = new ThirdPartyPaymentRequest();
        thirdPartyPaymentRequest.setPaymentRequest(JSON.toJSONString(req));
        thirdPartyPaymentRequest.setTransStatus(WelfareConstant.AsyncStatus.HANDLING.code());
        thirdPartyPaymentRequest.setPaymentType(PaymentTypeEnum.BARCODE.getCode());
        String barcode = req.getBarcode();
        Long accountCode = calculateAccountCode(barcode);
        thirdPartyPaymentRequest.setAccountCode(accountCode);
        thirdPartyPaymentRequest.setTransType(WelfareConstant.TransType.CONSUME.code());
        thirdPartyPaymentRequest.setPaymentTypeInfo(barcode);
        thirdPartyPaymentRequest.setTransNo(req.getTradeNo());
        String amount = req.getAmount();
        thirdPartyPaymentRequest
            .setTransAmount(amount == null ? null : new BigDecimal(fenToYuan(amount)));
        if (barcode.startsWith(WelfareConstant.PaymentChannel.WECHAT.barcodePrefix())) {
            thirdPartyPaymentRequest
                .setPaymentChannel(WelfareConstant.PaymentChannel.WECHAT.code());
        } else if (barcode.startsWith(WelfareConstant.PaymentChannel.ALIPAY.barcodePrefix())) {
            thirdPartyPaymentRequest
                .setPaymentChannel(WelfareConstant.PaymentChannel.ALIPAY.code());
        }
        return thirdPartyPaymentRequest;
    }

    @Override
    public CreateThirdPartyPaymentDTO createThirdPartyPayment(CreateThirdPartyPaymentReq req) {
        RBucket<CreateThirdPartyPaymentNotifyReq> bucket = redissonClient
            .getBucket(StrUtil.format(CREATE_THIRD_PARTY_PAYMENT_KEY_PREFIX, req.getBarcode()));
        CreateThirdPartyPaymentNotifyReq createThirdPartyPaymentNotifyReq = bucket.get();
        if (createThirdPartyPaymentNotifyReq == null) {
            return null;
        }
        CreateThirdPartyPaymentDTO createThirdPartyPaymentDTO = new CreateThirdPartyPaymentDTO();
        createThirdPartyPaymentDTO
            .setOrderString(createThirdPartyPaymentNotifyReq.getOrderString());
        return createThirdPartyPaymentDTO;
    }

    @Override
    public void thirdPartySignResultNotify(CbestPayBaseResp resp) {
        log.info("签约或解约结果通知, resp: {}", JSON.toJSONString(resp));
        String status = resp.getStatus();
        if (!CbestPayRespStatusConstant.SUCCESS.equals(status)) {
            log.error("签约或解约结果通知返回非成功状态， resp: {}", JSON.toJSONString(resp));
        }
        String bizStatus = resp.getBizStatus();
        if (!CbestPayRespStatusConstant.SUCCESS.equals(bizStatus)) {
            log.error("签约或解约结果通知业务状态返回非成功状态， resp: {}", JSON.toJSONString(resp));
            return;
        }
        String bizContent = resp.getBizContent();
        AlipayUserAgreementQueryResp alipayUserAgreementQueryResp = JSON
            .parseObject(bizContent, AlipayUserAgreementQueryResp.class);
        String signStatus = alipayUserAgreementQueryResp.getStatus();

        String externalLogonId = alipayUserAgreementQueryResp.getExternalLogonId();
        Long accountCode = Long.valueOf(externalLogonId);
        String subAccountType = PaymentChannel.ALIPAY.code();
        if ("NORMAL".equals(signStatus)) {
            String passwordFreeSignature = alipayUserAgreementQueryResp.getAgreementNo();
            SubAccount subAccount = subAccountDao
                .getByAccountCodeAndType(accountCode, subAccountType);
            subAccount.setPasswordFreeSignature(passwordFreeSignature);
            boolean updated = subAccountDao.updateById(subAccount);

        }
        if ("STOP".equals(signStatus)) {
            LambdaUpdateWrapper<SubAccount> updateWrapper = Wrappers.<SubAccount>lambdaUpdate()
                .set(SubAccount::getPasswordFreeSignature, null)
                .eq(SubAccount::getAccountCode, accountCode)
                .eq(SubAccount::getSubAccountType, subAccountType);
            boolean updated = subAccountDao.update(updateWrapper);

        }
    }
}
