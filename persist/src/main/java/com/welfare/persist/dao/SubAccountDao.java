package com.welfare.persist.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.welfare.persist.entity.SubAccount;
import com.welfare.persist.mapper.SubAccountMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

/**
 * 子账户信息(sub_account)数据DAO
 *
 * @author Yuxiang Li
 * @since 2021-03-10 15:43:18
 * @description 由 Mybatisplus Code Generator 创建
 */
@Slf4j
@Repository
public class SubAccountDao extends ServiceImpl<SubAccountMapper, SubAccount> {

  public SubAccount getByAccountCodeAndType(Long accountCode, String subAccountType) {
    if (accountCode != null && StringUtils.isNoneBlank(subAccountType)) {
      QueryWrapper<SubAccount> queryWrapper = new QueryWrapper<>();
      queryWrapper.eq(SubAccount.ACCOUNT_CODE, accountCode);
      queryWrapper.eq(SubAccount.SUB_ACCOUNT_TYPE, subAccountType);
      return getOne(queryWrapper);
    }
    return null;
  }

  public boolean deleteAccountCode(Long accountCode) {
    if (accountCode != null) {
      QueryWrapper<SubAccount> queryWrapper = new QueryWrapper<>();
      queryWrapper.eq(SubAccount.ACCOUNT_CODE, accountCode);
      return remove(queryWrapper);
    }
    return false;
  }
}