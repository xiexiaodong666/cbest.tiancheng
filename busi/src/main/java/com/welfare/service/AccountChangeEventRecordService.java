package com.welfare.service;

import com.welfare.common.constants.AccountChangeType;
import com.welfare.persist.entity.AccountChangeEventRecord;
import com.welfare.persist.entity.AccountConsumeSceneStoreRelation;

import java.util.List;

public interface AccountChangeEventRecordService {
  public void save(AccountChangeEventRecord accountChangeEventRecord);

  /**
   * 调用方法之前account必须存在
   * @param accountChangeEventRecordList
   * @param accountChangeType
   */
  public void batchSave(List<AccountChangeEventRecord> accountChangeEventRecordList,AccountChangeType accountChangeType);
  public void batchSaveByAccountTypeCode(String accountTypeCode,AccountChangeType accountChangeType);
  public void batchSaveBySceneStoreRelation(List<AccountConsumeSceneStoreRelation> accountConsumeSceneStoreRelationList);
  void batchSaveByAccountTypeCode(List<String> accountTypeCodes,AccountChangeType accountChangeType);
}
