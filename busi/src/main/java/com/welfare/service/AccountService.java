package com.welfare.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.persist.entity.Account;

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
}
