package com.welfare.service;

import com.welfare.persist.entity.StoreConsumeType;

import java.util.List;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/2/1 4:10 PM
 */
public interface StoreConsumeTypeService {

  boolean removeByStore(String storeCode);

  List<StoreConsumeType> getStoreConsumeTypeList(String storeCode);
}
