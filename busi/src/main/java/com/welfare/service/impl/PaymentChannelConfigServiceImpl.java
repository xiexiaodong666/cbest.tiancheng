package com.welfare.service.impl;

import com.welfare.persist.dao.PaymentChannelConfigDao;
import com.welfare.service.PaymentChannelConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 服务接口实现
 *
 * @author Yuxiang Li
 * @since 2021-03-23 09:35:43
 * @description 由 Mybatisplus Code Generator 创建
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class PaymentChannelConfigServiceImpl implements PaymentChannelConfigService {
    private final PaymentChannelConfigDao paymentChannelConfigDao;

}
