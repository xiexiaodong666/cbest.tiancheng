package com.welfare.persist.mapper;

import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.welfare.persist.entity.Account;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * 账户信息(account)数据Mapper
 *
 * @author Yuxiang Li
 * @since 2021-01-06 13:49:25
 * @description 由 Mybatisplus Code Generator 创建
*/
@Mapper
public interface AccountMapper extends BaseMapper<Account> {

  int increaseAccountBalance(@Param("increaseBalance")BigDecimal increaseBalance, @Param("updateUser")String updateUser,
                             @Param("accountCode")String accountCode);

  /**
   * 以account为基准，更新所有字段（除了deleted）
   * @param account
   * @return
   */
  Integer alwaysUpdateSomeColumnById(@Param(Constants.ENTITY) Account account);
}
