package com.welfare.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import  com.welfare.persist.dao.MerchantCreditDao;
import com.welfare.persist.entity.MerchantCredit;
import com.welfare.persist.mapper.MerchantCreditMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.welfare.service.MerchantCreditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

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

    @Autowired
    private MerchantCreditMapper merchantCreditMapper;

    @Autowired
    private MerchantCreditDao merchantCreditDao;

    @Override
    public int decreaseRechargeLimit(BigDecimal increaseLimit, Long id) {
        return merchantCreditMapper.decreaseRechargeLimit(increaseLimit, id);
    }

    @Override
    public MerchantCredit getByMerCode(String merCode) {
        QueryWrapper<MerchantCredit> query = new QueryWrapper<>();
        query.eq(MerchantCredit.MER_CODE, merCode);
        return  merchantCreditDao.getOne(query);
    }
}