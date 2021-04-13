package com.welfare.service.impl;

import com.welfare.common.exception.BizAssert;
import com.welfare.common.exception.ExceptionCode;
import com.welfare.persist.dao.AccountAmountTypeDao;
import com.welfare.persist.entity.Account;
import com.welfare.persist.entity.AccountAmountType;
import com.welfare.service.AccountAmountTypeGroupService;
import com.welfare.service.AccountAmountTypeService;
import com.welfare.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/4/13 2:46 下午
 */
@Service
@Slf4j
public class AccountAmountTypeGroupServiceImpl implements AccountAmountTypeGroupService {

  @Autowired
  private AccountService accountService;
  @Autowired
  private AccountAmountTypeService accountAmountTypeService;
  @Autowired
  private AccountAmountTypeDao accountAmountTypeDao;

  @Override
  public boolean removeByAccountCode(Long accountCode, String merAccountTypeCode) {
    Account account = accountService.getByAccountCode(accountCode);
    BizAssert.notNull(account, ExceptionCode.ILLEGALITY_ARGURMENTS, "用户不存在");
    AccountAmountType accountAmountType = accountAmountTypeService.queryOne(accountCode, merAccountTypeCode);
    BizAssert.notNull(accountAmountType, ExceptionCode.ILLEGALITY_ARGURMENTS, "福利类型为空");
    BizAssert.isTrue(accountAmountType.getJoinedGroup() != null && accountAmountType.getJoinedGroup(),
            ExceptionCode.ILLEGALITY_ARGURMENTS, "该用户没加入任何组");
    accountAmountType.setJoinedGroup(Boolean.FALSE);
    accountAmountType.setAccountAmountTypeGroupId(null);
    return accountAmountTypeDao.updateById(accountAmountType);
  }

  @Override
  public boolean addByAccountCodeAndMerAccountTypeCode(Long joinAccountCode, Long groupAccountCode, String merAccountTypeCode) {
    return false;
  }
}
