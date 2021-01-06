package com.welfare.service.impl;

import  com.welfare.persist.dao.MerchantCreditDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.welfare.service.MerchantCreditService;
import org.springframework.stereotype.Service;

/**
 * 商户额度信服务接口实现
 *
 * @author Yuxiang Li
 * @since 2021-01-06 13:49:25
 * @description 由 Mybatisplus Code Generator 创建
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class MerchantCreditServiceImpl implements MerchantCreditService {
    private final MerchantCreditDao merchantCreditDao;

}