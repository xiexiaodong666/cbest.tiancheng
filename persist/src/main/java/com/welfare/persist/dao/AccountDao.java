package com.welfare.persist.dao;

import lombok.extern.slf4j.Slf4j;
import com.welfare.persist.entity.Account;
import com.welfare.persist.mapper.AccountMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Repository;

/**
 * 账户信息(account)数据DAO
 *
 * @author Yuxiang Li
 * @since 2021-02-01 11:20:41
 * @description 由 Mybatisplus Code Generator 创建
 */
@Slf4j
@Repository
public class AccountDao extends ServiceImpl<AccountMapper, Account> {
    @Override
    public boolean updateById(Account entity) {
        return super.updateById(entity);
    }

    public Integer updateAllColumnById(Account entity){
        return getBaseMapper().alwaysUpdateSomeColumnById(entity);
    }
}