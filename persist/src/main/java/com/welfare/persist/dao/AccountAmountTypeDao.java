package com.welfare.persist.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.welfare.persist.entity.AccountAmountType;
import com.welfare.persist.mapper.AccountAmountTypeMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * (account_amount_type)数据DAO
 *
 * @author kancy
 * @since 2021-01-08 21:33:49
 * @description 由 Mybatisplus Code Generator 创建
 */
@Slf4j
@Repository
public class AccountAmountTypeDao extends ServiceImpl<AccountAmountTypeMapper, AccountAmountType> {

    /**
     * 根据账户号查询AccountAmountType列表
     * @param accountCode
     * @return
     */
    public List<AccountAmountType> queryByAccountCode(Long accountCode){
        QueryWrapper<AccountAmountType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(AccountAmountType.ACCOUNT_CODE,accountCode);
        return list(queryWrapper);
    }
}