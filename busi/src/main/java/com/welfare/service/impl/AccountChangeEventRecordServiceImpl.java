package com.welfare.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.welfare.common.constants.AccountChangeType;
import com.welfare.persist.dao.AccountDao;
import com.welfare.persist.entity.Account;
import com.welfare.persist.entity.AccountChangeEventRecord;
import com.welfare.persist.entity.AccountConsumeSceneStoreRelation;
import com.welfare.persist.mapper.AccountChangeEventRecordCustomizeMapper;
import com.welfare.persist.mapper.AccountCustomizeMapper;
import com.welfare.service.AccountChangeEventRecordService;
import com.welfare.service.AccountService;
import com.welfare.service.utils.AccountUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author yaoxiao
 * @version 0.0.1
 * @date 2021/1/14 17:36
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class AccountChangeEventRecordServiceImpl implements AccountChangeEventRecordService {

  private final AccountChangeEventRecordCustomizeMapper accountChangeEventRecordCustomizeMapper;
  private final AccountDao accountDao;
  private final AccountCustomizeMapper accountCustomizeMapper;
  @Autowired
  private AccountService accountService;

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void save(AccountChangeEventRecord accountChangeEventRecord) {
    accountChangeEventRecordCustomizeMapper.insertAccountChangeEvent(accountChangeEventRecord);
    if (accountChangeEventRecord.getChangeType()
        .equals(AccountChangeType.ACCOUNT_NEW.getChangeType())) {
      return;
    } else {
      UpdateWrapper<Account> accountUpdateWrapper = new UpdateWrapper<Account>();
      accountUpdateWrapper.eq(Account.ACCOUNT_CODE, accountChangeEventRecord.getAccountCode());
      Account account = new Account();
      account.setChangeEventId(accountChangeEventRecord.getId());
      accountDao.update(account, accountUpdateWrapper);
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void batchSave(List<AccountChangeEventRecord> accountChangeEventRecordList,
      AccountChangeType accountChangeType) {
    if (CollectionUtils.isEmpty(accountChangeEventRecordList)) {
      return;
    }
    accountChangeEventRecordCustomizeMapper.batchInsert(accountChangeEventRecordList);
    if (accountChangeType.getChangeType().equals(AccountChangeType.ACCOUNT_NEW.getChangeType())) {
      //批量回写
      List<Map<String, Object>> mapList = AccountUtils.getMaps(accountChangeEventRecordList);
      accountService.batchUpdateChangeEventId(mapList);
    } else {
      //新增操作记录之后
      List<Map<String, Object>> list = AccountUtils.getMaps(accountChangeEventRecordList);
      accountCustomizeMapper.batchUpdateChangeEventId(list);
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void batchSaveByAccountTypeCode(String accountTypeCode,
      AccountChangeType accountChangeType) {
    List<Account> accounts = accountService.queryByAccountTypeCode(accountTypeCode);
    List<AccountChangeEventRecord> recordList = AccountUtils
        .getEventList(accounts, AccountChangeType.ACCOUNT_TYPE_DELETE);
    this.batchSave(recordList, AccountChangeType.ACCOUNT_TYPE_DELETE);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void batchSaveBySceneStoreRelation(List<AccountConsumeSceneStoreRelation> accountConsumeSceneStoreRelationList) {
    if (CollectionUtils.isEmpty(accountConsumeSceneStoreRelationList)) {
      return;
    }
    List<Long> sceneIdList = accountConsumeSceneStoreRelationList.stream()
        .map(accountConsumeSceneStoreRelation -> {
          return accountConsumeSceneStoreRelation.getAccountConsumeSceneId();
        }).collect(Collectors.toList());
    List<Account> accountList = accountCustomizeMapper.queryByConsumeSceneIdList(sceneIdList);
    if (CollectionUtils.isEmpty(accountList)) {
      return;
    }
    List<AccountChangeEventRecord> getEventList = AccountUtils.getEventList(accountList,
        AccountChangeType.ACCOUNT_CONSUME_SCENE_CONSUMETYPE_CHANGE);
    this.batchSave(getEventList, AccountChangeType.ACCOUNT_CONSUME_SCENE_CONSUMETYPE_CHANGE);
  }
}
