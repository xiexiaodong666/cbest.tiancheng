package com.welfare.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import  com.welfare.persist.dao.AccountDao;
import com.welfare.persist.entity.Account;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.welfare.service.AccountService;
import org.springframework.stereotype.Service;

/**
 * 账户信息服务接口实现
 *
 * @author Yuxiang Li
 * @since 2021-01-06 13:49:25
 * @description 由 Mybatisplus Code Generator 创建
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class AccountServiceImpl implements AccountService {
    private final AccountDao accountDao;

    @Override
    public Page<Account> pageQuery(Page<Account> page,QueryWrapper<Account> queryWrapper) {
        Page<Account> resultPage = accountDao.page(page, queryWrapper);
        return resultPage;
    }
}