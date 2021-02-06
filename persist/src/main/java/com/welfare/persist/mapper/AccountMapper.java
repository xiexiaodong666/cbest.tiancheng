package com.welfare.persist.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.welfare.persist.dto.AccountDepositIncreDTO;
import com.welfare.persist.entity.Account;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * 账户信息(account)数据Mapper
 *
 * @author Yuxiang Li
 * @since 2021-02-01 11:20:41
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

  int batchUpdateAccountBalance(List<AccountDepositIncreDTO> accountList);

  int updateAccountBalance(@Param("increaseBalance")BigDecimal increaseBalance, @Param("accountCode")Long accountCode);
}
