package com.welfare.service.impl;

import com.welfare.persist.dao.AccountBillDetailDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.welfare.service.AccountBillDetailService;
import org.springframework.stereotype.Service;

/**
 * 用户流水明细服务接口实现
 *
 * @author Yuxiang Li
 * @since 2021-01-06 13:49:25
 * @description 由 Mybatisplus Code Generator 创建
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class AccountBillDetailServiceImpl implements AccountBillDetailService {
    private final AccountBillDetailDao accountBillDetailDao;

}