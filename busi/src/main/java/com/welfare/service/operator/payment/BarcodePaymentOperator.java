package com.welfare.service.operator.payment;

import com.welfare.service.BarcodeService;
import com.welfare.service.dto.payment.PaymentRequest;
import com.welfare.service.dto.payment.BarcodePaymentRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 1/12/2021
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class BarcodePaymentOperator extends AbstractPaymentOperator {

    private final BarcodeService barcodeService;
    @Override
    public void doPay(PaymentRequest paymentRequest) {

    }
}
