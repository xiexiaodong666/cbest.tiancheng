package com.welfare.service.impl;

import com.welfare.persist.dao.AccountChangeEventRecordDao;
import com.welfare.persist.entity.AccountChangeEventRecord;
import com.welfare.persist.mapper.AccountChangeEventRecordCustomizeMapper;
import com.welfare.service.AccountChangeEventRecordService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
  private final AccountChangeEventRecordDao accountChangeEventRecordDao;

  @Override
  public void save(AccountChangeEventRecord accountChangeEventRecord) {
    accountChangeEventRecordCustomizeMapper.insert(accountChangeEventRecord);
  }

  @Override
  public void batchSave(List<AccountChangeEventRecord> accountChangeEventRecordList) {
    accountChangeEventRecordDao.saveBatch(accountChangeEventRecordList);
  }
}
