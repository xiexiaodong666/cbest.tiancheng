package com.welfare.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.welfare.common.constants.AccountChangeType;
import com.welfare.persist.dao.AccountDao;
import com.welfare.persist.entity.Account;
import com.welfare.persist.entity.AccountChangeEventRecord;
import com.welfare.persist.mapper.AccountChangeEventRecordCustomizeMapper;
import com.welfare.persist.mapper.AccountCustomizeMapper;
import com.welfare.service.AccountChangeEventRecordService;
import com.welfare.service.utils.AccountUtils;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author yaoxiao
 * @version 0.0.1
 * @date 2021/1/14 17:36
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class AccountChangeEventRecordServiceImpl implements AccountChangeEventRecordService{
  private final AccountChangeEventRecordCustomizeMapper accountChangeEventRecordCustomizeMapper;
  private final AccountDao accountDao;
  private final AccountCustomizeMapper accountCustomizeMapper;

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void save(AccountChangeEventRecord accountChangeEventRecord) {
    accountChangeEventRecordCustomizeMapper.insertAccountChangeEvent(accountChangeEventRecord);
    if( accountChangeEventRecord.getChangeType().equals(AccountChangeType.ACCOUNT_NEW.getChangeType())){
      return;
    }else{
      UpdateWrapper<Account> accountUpdateWrapper = new UpdateWrapper<Account>();
      accountUpdateWrapper.eq(Account.ACCOUNT_CODE,accountChangeEventRecord.getAccountCode());
      Account account = new Account();
      account.setChangeEventId(accountChangeEventRecord.getId());
      accountDao.update(account,accountUpdateWrapper);
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void batchSave(List<AccountChangeEventRecord> accountChangeEventRecordList,AccountChangeType accountChangeType) {
    if(CollectionUtils.isEmpty(accountChangeEventRecordList)){
      return;
    }
    accountChangeEventRecordCustomizeMapper.batchInsert(accountChangeEventRecordList);
    if( accountChangeType.equals(AccountChangeType.ACCOUNT_NEW.getChangeType())){
      return;
    }else{
      //新增操作记录之后
      List<Map<String, Object>> list = AccountUtils.getMaps(accountChangeEventRecordList);
      accountCustomizeMapper.batchUpdateChangeEventId(list);
    }
  }
}
