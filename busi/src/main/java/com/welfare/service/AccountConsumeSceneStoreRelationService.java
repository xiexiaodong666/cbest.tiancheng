package com.welfare.service;

import com.welfare.persist.entity.AccountConsumeSceneStoreRelation;
import java.util.List;

/**
 * @author yaoxiao
 * @version 0.0.1
 * @date 2021/1/13 17:02
 */
public interface AccountConsumeSceneStoreRelationService {
  public List<AccountConsumeSceneStoreRelation> getListByConsumeSceneId(Long accountConsumeSceneId);
}
