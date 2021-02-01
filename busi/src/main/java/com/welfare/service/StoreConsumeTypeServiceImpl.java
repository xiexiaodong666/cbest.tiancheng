package com.welfare.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.welfare.persist.dao.StoreConsumeTypeDao;
import com.welfare.persist.entity.StoreConsumeType;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/2/1 4:11 PM
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class StoreConsumeTypeServiceImpl implements StoreConsumeTypeService {

  private final StoreConsumeTypeDao storeConsumeTypeDao;

  @Override
  public boolean removeByStore(String storeCode) {
    QueryWrapper<StoreConsumeType> storeConsumeTypeQueryWrapper = new QueryWrapper<>();
    storeConsumeTypeQueryWrapper.eq(StoreConsumeType.STORE_CODE, storeCode);
    List<StoreConsumeType> storeConsumeTypeRemoveList = storeConsumeTypeDao.list(
        storeConsumeTypeQueryWrapper);
    List<Long> ids = storeConsumeTypeRemoveList.stream().map(s->s.getId()).collect(Collectors.toList()) ;
    return storeConsumeTypeDao.removeByIds(ids);
  }

  @Override
  public List<StoreConsumeType> getStoreConsumeTypeList(String storeCode) {
    QueryWrapper<StoreConsumeType> storeConsumeTypeQueryWrapper = new QueryWrapper<>();
    storeConsumeTypeQueryWrapper.eq(StoreConsumeType.STORE_CODE, storeCode);
    List<StoreConsumeType> storeConsumeTypeRemoveList = storeConsumeTypeDao.list(
        storeConsumeTypeQueryWrapper);
    return storeConsumeTypeRemoveList;
  }


}
