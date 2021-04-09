package com.welfare.persist.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.welfare.persist.entity.AccountType;
import com.welfare.persist.mapper.AccountTypeMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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

    public Map<String,AccountType> mapByMerCodeAndCodes(String merCode, Set<String> codes){
        QueryWrapper<AccountType> queryWrapper = new QueryWrapper<>();
        queryWrapper.in(AccountType.TYPE_CODE,codes);
        queryWrapper.eq(AccountType.MER_CODE, merCode);
        List<AccountType> list = list(queryWrapper);
        Map<String,AccountType> map = new HashMap<>();
        if (CollectionUtils.isNotEmpty(list)) {
            map = list.stream().collect(Collectors.toMap(AccountType::getTypeCode, accountType -> accountType));
        }
        return map;
    }
}