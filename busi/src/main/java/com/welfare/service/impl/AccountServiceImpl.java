package com.welfare.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import  com.welfare.persist.dao.AccountDao;
import com.welfare.persist.dto.AccountPageDTO;
import com.welfare.persist.mapper.AccountCustomizeMapper;
import com.welfare.persist.mapper.AccountMapper;
import com.welfare.service.converter.AccountConverter;
import com.welfare.service.dto.AccountDTO;
import com.welfare.service.dto.AccountPageReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.welfare.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

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
    @Autowired
    private final AccountMapper accountMapper;
    private final AccountCustomizeMapper accountCustomizeMapper;
    private final AccountConverter accountConverter;


    @Override
    public Page<AccountDTO> getPageDTO(Page<AccountPageDTO> page, AccountPageReq accountPageReq) {
        IPage<AccountPageDTO> iPage = accountCustomizeMapper.queryPageDTO(page,accountPageReq.getMerCode(),accountPageReq.getAccountName(),accountPageReq.getDepartmentCode(),accountPageReq.getAccountStatus(),accountPageReq.getAccountTypeCode());
        return accountConverter.toPage(iPage);
    }

    @Override
    public int increaseAccountBalance(BigDecimal increaseBalance, String updateUser, String accountCode) {
        return accountMapper.increaseAccountBalance(increaseBalance, updateUser, accountCode);
    }
}