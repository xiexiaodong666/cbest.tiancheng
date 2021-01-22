package com.welfare.persist.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.welfare.persist.entity.AccountType;
import com.welfare.persist.mapper.AccountTypeMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * 员工类型(account_type)数据DAO
 *
 * @author Yuxiang Li
 * @since 2021-01-06 13:49:25
 * @description 由 Mybatisplus Code Generator 创建
 */
@Slf4j
@Repository
public class AccountTypeDao extends ServiceImpl<AccountTypeMapper, AccountType> {
    public AccountType getOneByAccountType(String accountType){
        QueryWrapper<AccountType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(AccountType.TYPE_CODE,accountType);
        return getOne(queryWrapper);
    }
}