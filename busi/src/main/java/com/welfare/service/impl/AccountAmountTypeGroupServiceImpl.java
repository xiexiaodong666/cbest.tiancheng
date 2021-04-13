package com.welfare.service.impl;

import com.welfare.service.AccountAmountTypeGroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/4/13 2:46 下午
 */
@Service
@Slf4j
public class AccountAmountTypeGroupServiceImpl implements AccountAmountTypeGroupService {


  @Override
  public boolean removeByAccountCode(String accountCode) {
    return false;
  }

  @Override
  public boolean addByAccountCodeAndMerAccountTypeCode(String joinAccountCode, String groupAccountCode, String merAccountTypeCode) {
    return false;
  }
}
