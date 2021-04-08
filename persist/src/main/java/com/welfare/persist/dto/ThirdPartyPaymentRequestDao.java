package com.welfare.persist.dto;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.welfare.common.constants.WelfareConstant.TransType;
import com.welfare.common.enums.PaymentTypeEnum;
import lombok.extern.slf4j.Slf4j;
import com.welfare.persist.entity.ThirdPartyPaymentRequest;
import com.welfare.persist.mapper.ThirdPartyPaymentRequestMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Repository;

/**
 * (third_party_payment_request)数据DAO
 *
 * @author Yuxiang Li
 * @since 2021-03-17 09:20:23
 * @description 由 Mybatisplus Code Generator 创建
 */
@Slf4j
@Repository
public class ThirdPartyPaymentRequestDao extends ServiceImpl<ThirdPartyPaymentRequestMapper, ThirdPartyPaymentRequest> {
    public ThirdPartyPaymentRequest queryByTransNo(String transNo){
        QueryWrapper<ThirdPartyPaymentRequest> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(ThirdPartyPaymentRequest.TRANS_NO,transNo);
        queryWrapper.eq(ThirdPartyPaymentRequest.TRANS_TYPE, TransType.CONSUME.code());
        queryWrapper.last("limit 1");

        return getOne(queryWrapper);
    }

    public ThirdPartyPaymentRequest queryByBarcode(String barcode){
        QueryWrapper<ThirdPartyPaymentRequest> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(ThirdPartyPaymentRequest.PAYMENT_TYPE, PaymentTypeEnum.BARCODE.getCode());
        queryWrapper.eq(ThirdPartyPaymentRequest.PAYMENT_TYPE_INFO,barcode);
        return getOne(queryWrapper);
    }
}