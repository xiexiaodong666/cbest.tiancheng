package com.welfare.service.impl;

import com.welfare.persist.dao.PaymentChannelDao;
import com.welfare.service.PaymentChannelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 服务接口实现
 *
 * @author kancy
 * @since 2021-03-11 17:31:46
 * @description 由 Mybatisplus Code Generator 创建
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class PaymentChannelServiceImpl implements PaymentChannelService {
    private final PaymentChannelDao paymentChannelDao;

}