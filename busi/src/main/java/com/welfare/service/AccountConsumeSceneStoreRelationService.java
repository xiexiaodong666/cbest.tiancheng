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
   * 批量修改商户关联门店消费场景
   * @param merCode
   * @param relationDTOList
   */
  public void updateStoreConsumeTypeByDTOList(String merCode,List<StoreConsumeRelationDTO> relationDTOList);

  /**
   * 在商户门店配置列表删除所有的配置消费场景门店
   * @param merCode
   */
  public void deleteConsumeScene(String merCode);
}
