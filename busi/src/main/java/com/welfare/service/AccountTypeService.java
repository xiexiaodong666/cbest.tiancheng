package com.welfare.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.persist.entity.AccountType;

/**
 * 员工类型服务接口
 *
 * @author Yuxiang Li
 * @since 2021-01-06 13:49:25
 * @description 由 Mybatisplus Code Generator 创建
 */
public interface AccountTypeService {
  Page<AccountType> pageQuery(Page<AccountType> page, QueryWrapper<AccountType> queryWrapper);
  public AccountType getAccountType(Long id);
  public Boolean save(AccountType accountType);
  public Boolean update(AccountType accountType);
  public Boolean delete(Long id);
  AccountType queryByTypeCode(String merCode,String typeCode);
}
