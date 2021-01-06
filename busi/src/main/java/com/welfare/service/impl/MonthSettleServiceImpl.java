package com.welfare.service.impl;

import  com.welfare.persist.dao.MonthSettleDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.welfare.service.MonthSettleService;
import org.springframework.stereotype.Service;

/**
 * 月度结算账单服务接口实现
 *
 * @author Yuxiang Li
 * @since 2021-01-06 13:49:25
 * @description 由 Mybatisplus Code Generator 创建
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class MonthSettleServiceImpl implements MonthSettleService {
    private final MonthSettleDao monthSettleDao;

}