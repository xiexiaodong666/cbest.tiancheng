package com.welfare.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.persist.entity.Account;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * 账户信息服务接口
 *
 * @author Yuxiang Li
 * @since 2021-01-06 13:49:25
 * @description 由 Mybatisplus Code Generator 创建
 */
public interface AccountService {
    /**
     * 分页查询
     * @param page
     * @param queryWrapper
     * @return
     */
    Page<Account> pageQuery(Page<Account> page,QueryWrapper<Account> queryWrapper);

    /**
     * 增加员工账号余额
     * @param increaseBalance
     * @param updateUser
     * @param accountCode
     * @return
     */
    int increaseAccountBalance(BigDecimal increaseBalance, String updateUser, String accountCode);

    Account getByAccountCode(String accountCode);
}
