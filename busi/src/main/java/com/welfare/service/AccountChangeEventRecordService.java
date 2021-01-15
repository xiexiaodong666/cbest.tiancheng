package com.welfare.service;

import com.welfare.common.constants.AccountChangeType;
import com.welfare.persist.entity.AccountChangeEventRecord;
import com.welfare.persist.entity.AccountConsumeSceneStoreRelation;
import java.util.List;

public interface AccountChangeEventRecordService {
  public void save(AccountChangeEventRecord accountChangeEventRecord);
  public void batchSave(List<AccountChangeEventRecord> accountChangeEventRecordList,AccountChangeType accountChangeType);
  public void batchSaveByAccountTypeCode(String accountTypeCode,AccountChangeType accountChangeType);
  public void batchSaveBySceneStoreRelation(List<AccountConsumeSceneStoreRelation> accountConsumeSceneStoreRelationList);
}
