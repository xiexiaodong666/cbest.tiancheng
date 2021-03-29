package com.welfare.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.welfare.common.util.SpringBeanUtils;
import com.welfare.service.AccountPaymentResultService;
import com.welfare.service.BarcodeService;
import com.welfare.service.dto.BarcodePaymentNotifyReq;
import com.welfare.service.dto.BarcodePaymentResultDTO;
import com.welfare.service.dto.BarcodePaymentResultReq;
import com.welfare.service.dto.CreateThirdPartyPaymentDTO;
import com.welfare.service.dto.CreateThirdPartyPaymentNotifyReq;
import com.welfare.service.dto.CreateThirdPartyPaymentReq;
import com.welfare.service.dto.ThirdPartyPaymentResultNotifyReq;
import com.welfare.service.remote.entity.CbestPayBaseResp;
import com.welfare.service.remote.entity.CbestPayRespStatusConstant;
import java.util.Calendar;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class AccountPaymentResultServiceImpl implements AccountPaymentResultService {

    @Resource
    private RedissonClient redissonClient;

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
        if (CbestPayRespStatusConstant.SUCCESS.equals(resp.getStatus())) {
            String bizContent = resp.getBizContent();
            ThirdPartyPaymentResultNotifyReq thirdPartyPaymentResultNotifyReq = JSON
                .parseObject(bizContent, ThirdPartyPaymentResultNotifyReq.class);
            BarcodePaymentNotifyReq req = new BarcodePaymentNotifyReq();
            BeanUtils.copyProperties(thirdPartyPaymentResultNotifyReq, req);
            req.setTotalAmount(fenToYuan(thirdPartyPaymentResultNotifyReq.getTotalAmount()));
            req.setActualAmount(fenToYuan(thirdPartyPaymentResultNotifyReq.getActualAmount()));
            req.setTotalDiscountAmount(fenToYuan(thirdPartyPaymentResultNotifyReq.getTotalDiscountAmount()));
            String barcode = thirdPartyPaymentResultNotifyReq.getBarcode();
            BarcodeService barcodeService = SpringBeanUtils.getBean(BarcodeService.class);
            Long accountCode = barcodeService
                .parseAccountFromBarcode(barcode, Calendar.getInstance().getTime(), true);
            RBucket<BarcodePaymentNotifyReq> bucket = redissonClient.<BarcodePaymentNotifyReq>getBucket(
                StrUtil.format(KEY_PREFIX, accountCode, barcode));
            bucket.set(req, 1, TimeUnit.MINUTES);
        }
    }

    @Override
    public void createThirdPartyPaymentNotify(CbestPayBaseResp resp) {
        if (CbestPayRespStatusConstant.SUCCESS.equals(resp.getStatus())) {
            String bizContent = resp.getBizContent();
            CreateThirdPartyPaymentNotifyReq req = JSON
                .parseObject(bizContent, CreateThirdPartyPaymentNotifyReq.class);
            RBucket<CreateThirdPartyPaymentNotifyReq> bucket = redissonClient.<CreateThirdPartyPaymentNotifyReq>getBucket(
                StrUtil.format(CREATE_THIRD_PARTY_PAYMENT_KEY_PREFIX, req.getBarcode()));
            bucket.set(req, 1, TimeUnit.MINUTES);
        }
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
        createThirdPartyPaymentDTO.setOrderString(createThirdPartyPaymentNotifyReq.getOrderString());
        return createThirdPartyPaymentDTO;
    }
}
