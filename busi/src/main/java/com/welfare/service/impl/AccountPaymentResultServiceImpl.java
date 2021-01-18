package com.welfare.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.welfare.service.AccountPaymentResultService;
import com.welfare.service.dto.BarcodePaymentNotifyReq;
import com.welfare.service.dto.BarcodePaymentResultDTO;
import com.welfare.service.dto.BarcodePaymentResultReq;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

@Service
public class AccountPaymentResultServiceImpl implements AccountPaymentResultService {

    @Resource
    private RedissonClient redissonClient;

    public static final String KEY_PREFIX = "e-welfare_account_payment_result:{}:{}";

    @Override
    public void barcodePaymentNotify(BarcodePaymentNotifyReq req) {
        RBucket<BarcodePaymentNotifyReq> bucket = redissonClient.<BarcodePaymentNotifyReq>getBucket(
            StrUtil.format(KEY_PREFIX, req.getAccountCode(), req.getBarcode()));
        bucket.set(req, 1, TimeUnit.MINUTES);
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
}
