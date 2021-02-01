package com.welfare.service;

import com.welfare.persist.entity.AccountConsumeSceneStoreRelation;

import com.welfare.service.dto.StoreConsumeRelationDTO;
import java.util.List;
import java.util.Map;

/**
 * @author yaoxiao
 * @version 0.0.1
 * @date 2021/1/13 17:02
 */
public interface AccountConsumeSceneStoreRelationService {
  public List<AccountConsumeSceneStoreRelation> getListByConsumeSceneId(Long accountConsumeSceneId);

  /**
   * 门店修改了消费方式,员工类型同步修改消费方式
   *
   */
  public void updateStoreConsumeType(String merCode,String storeCode, String consumeType);

  public void updateStoreConsumeTypeByDTOList(String merCode,List<StoreConsumeRelationDTO> relationDTOList);
}
