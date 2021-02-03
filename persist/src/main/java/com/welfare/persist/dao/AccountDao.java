package com.welfare.persist.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import com.welfare.persist.entity.Account;
import com.welfare.persist.mapper.AccountMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public Map<Long, Account> mapByAccountCodes(List<Long> accountCodes) {
        Map<Long, Account> map = new HashMap<>();
        QueryWrapper<Account> queryWrapper = new QueryWrapper<>();
        queryWrapper.in(Account.ACCOUNT_CODE, accountCodes);
        List<Account> list = list(queryWrapper);
        if (CollectionUtils.isNotEmpty(list)) {
            map = list.stream().collect(Collectors.toMap(Account::getAccountCode, a -> a,(k1, k2)->k1));
        }
        return map;
    }
}