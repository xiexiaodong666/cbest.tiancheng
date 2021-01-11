package com.welfare.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import  com.welfare.persist.dao.AccountTypeDao;
import com.welfare.persist.entity.AccountType;
import com.welfare.persist.mapper.AccountTypeMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.welfare.service.AccountTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 员工类型服务接口实现
 *
 * @author Yuxiang Li
 * @since 2021-01-06 13:49:25
 * @description 由 Mybatisplus Code Generator 创建
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class AccountTypeServiceImpl implements AccountTypeService {
    private final AccountTypeDao accountTypeDao;

    @Override
    public Page<AccountType> pageQuery(Page<AccountType> page,
        QueryWrapper<AccountType> queryWrapper) {
        return accountTypeDao.page(page,queryWrapper);
    }

    @Override
    public AccountType getAccountType(Long id) {
        return accountTypeDao.getById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean save(AccountType accountType){
        return accountTypeDao.save(accountType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean update(AccountType accountType) {
        return accountTypeDao.updateById(accountType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean delete(Long id) {
        UpdateWrapper<AccountType> updateWrapper = new UpdateWrapper();
        updateWrapper.eq(AccountType.ID,id);
        AccountType accountType = new AccountType();
        accountType.setDeleted(true);

        return accountTypeDao.update(accountType,updateWrapper);
    }
}