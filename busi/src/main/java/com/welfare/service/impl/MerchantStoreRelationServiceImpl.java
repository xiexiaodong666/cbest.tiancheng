package com.welfare.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.common.util.ApiUserHolder;
import com.welfare.persist.dao.MerchantStoreRelationDao;
import com.welfare.persist.dto.AdminMerchantStore;
import com.welfare.persist.dto.MerchantStoreRelationDTO;
import com.welfare.persist.dto.query.MerchantStoreRelationAddReq;
import com.welfare.persist.dto.query.MerchantStoreRelationUpdateReq;
import com.welfare.persist.entity.MerchantStoreRelation;
import com.welfare.persist.mapper.MerchantStoreRelationMapper;
import com.welfare.service.MerchantStoreRelationService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 商户消费场景配置服务接口实现
 *
 * @author Yuxiang Li
 * @description 由 Mybatisplus Code Generator 创建
 * @since 2021-01-06 13:49:25
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class MerchantStoreRelationServiceImpl implements MerchantStoreRelationService {

  private final MerchantStoreRelationDao merchantStoreRelationDao;
  private final MerchantStoreRelationMapper merchantStoreRelationMapper;

  @Override
  public Page<MerchantStoreRelation> pageQuery(Page<MerchantStoreRelation> page,
      QueryWrapper<MerchantStoreRelation> queryWrapper) {
    Page<MerchantStoreRelation> resultPage = merchantStoreRelationDao.page(page, queryWrapper);
    return resultPage;
  }

  @Override
  public Page<MerchantStoreRelationDTO> searchMerchantStoreRelations(
      Page<MerchantStoreRelation> page,
      String merName, String status, Date startTime, Date endTime) {

    return merchantStoreRelationMapper.searchMerchantStoreRelations(page, merName, status, startTime, endTime);
  }

  @Override
  public MerchantStoreRelation getMerchantStoreRelationById(
      QueryWrapper<MerchantStoreRelation> queryWrapper) {
    return merchantStoreRelationDao.getOne(queryWrapper);
  }

  @Override
  public List<MerchantStoreRelation> getMerchantStoreRelationListByMerCode(
      QueryWrapper<MerchantStoreRelation> queryWrapper) {
    return merchantStoreRelationDao.list(queryWrapper);
  }

  @Override
  public boolean add(MerchantStoreRelationAddReq relationAddReq) {
    List<MerchantStoreRelation> merchantStoreRelationList = new ArrayList<>();
    List<AdminMerchantStore> adminMerchantStoreList = relationAddReq.getAdminMerchantStoreList();

    for (AdminMerchantStore store :
        adminMerchantStoreList) {
      MerchantStoreRelation merchantStoreRelation = new MerchantStoreRelation();
      merchantStoreRelation.setMerCode(relationAddReq.getMerCode());

      merchantStoreRelation.setRamark(relationAddReq.getRamark());
      merchantStoreRelation.setDeleted(false);
      merchantStoreRelation.setStatus(0);
      merchantStoreRelation.setStoreCode(store.getStoreCode());
      merchantStoreRelation.setConsumType(store.getConsumType());
      merchantStoreRelation.setStoreAlias(store.getStoreAlias());
      merchantStoreRelation.setIsRebate(store.getIsRebate());
      merchantStoreRelation.setRebateType(store.getRebateType());
      merchantStoreRelation.setRebateRatio(store.getRebateRatio());

      if (ApiUserHolder.getUserInfo() != null) {
        merchantStoreRelation.setCreateUser(ApiUserHolder.getUserInfo().getUserName());
      }

      merchantStoreRelationList.add(merchantStoreRelation);
    }
    return merchantStoreRelationDao.saveBatch(merchantStoreRelationList);
  }

  @Override
  @Transactional(rollbackFor = {Exception.class})
  public boolean update(MerchantStoreRelationUpdateReq relationUpdateReq) {
    QueryWrapper<MerchantStoreRelation> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq(MerchantStoreRelation.ID, relationUpdateReq.getId());
    MerchantStoreRelation merchantStoreRelation = merchantStoreRelationDao.getOne(queryWrapper);
    queryWrapper = new QueryWrapper<>();
    queryWrapper.eq(MerchantStoreRelation.MER_CODE, merchantStoreRelation.getMerCode());
    List<MerchantStoreRelation> merchantStoreRelations = merchantStoreRelationDao.list(
        queryWrapper);
    List<MerchantStoreRelation> merchantStoreRelationNewList = new ArrayList<>();
    List<Long> deleteIds = new ArrayList<>();
    List<AdminMerchantStore> adminMerchantStoreList = relationUpdateReq.getAdminMerchantStoreList();

    List<AdminMerchantStore> adminMerchantStoreUpdateList = new ArrayList<>();

    if (adminMerchantStoreList == null) {
      adminMerchantStoreList = new ArrayList<>(0);
    }

    for (AdminMerchantStore merchantStore :
        adminMerchantStoreList) {
      // update
      if (Strings.isNotEmpty(merchantStore.getMerchantStoreId())) {
        adminMerchantStoreUpdateList.add(merchantStore);
      } // add
      else {
        MerchantStoreRelation merchantStoreRelationNew = new MerchantStoreRelation();
        merchantStoreRelationNew.setMerCode(merchantStoreRelation.getMerCode());
        merchantStoreRelationNew.setStoreCode(merchantStore.getStoreCode());
        merchantStoreRelationNew.setConsumType(merchantStore.getConsumType());
        merchantStoreRelationNew.setStoreAlias(merchantStore.getStoreAlias());
        merchantStoreRelationNew.setIsRebate(merchantStore.getIsRebate());
        merchantStoreRelationNew.setRebateType(merchantStore.getRebateType());
        merchantStoreRelationNew.setRebateRatio(merchantStore.getRebateRatio());
        merchantStoreRelationNew.setRamark(relationUpdateReq.getRamark());
        merchantStoreRelationNew.setDeleted(false);
        if (ApiUserHolder.getUserInfo() != null) {
          merchantStoreRelationNew.setCreateUser(ApiUserHolder.getUserInfo().getUserName());
          merchantStoreRelationNew.setUpdateUser(ApiUserHolder.getUserInfo().getUserName());

        }
        merchantStoreRelationNew.setStatus(0);
        merchantStoreRelationNewList.add(merchantStoreRelationNew);
      }
    }

    for (MerchantStoreRelation m :
        merchantStoreRelations) {
      Optional<AdminMerchantStore> adminMerchantStoreOptional = adminMerchantStoreUpdateList
          .stream()
          .filter(a -> a.getMerchantStoreId().equals(String.valueOf(m.getId()))).findFirst();
      // update
      if (adminMerchantStoreOptional.isPresent()) {
        AdminMerchantStore adminMerchantStore = adminMerchantStoreOptional.get();
        m.setConsumType(adminMerchantStore.getConsumType());
        m.setStoreCode(adminMerchantStore.getStoreCode());
        m.setStoreAlias(adminMerchantStore.getStoreAlias());
        m.setIsRebate(adminMerchantStore.getIsRebate());
        m.setRebateRatio(adminMerchantStore.getRebateRatio());
        m.setRebateType(adminMerchantStore.getRebateType());
        m.setRamark(relationUpdateReq.getRamark());
      } // delete
      else {
        deleteIds.add(m.getId());
        merchantStoreRelations.remove(m);
      }
    }
    boolean remove = true;
    boolean updateBatch = true;
    boolean saveBath = true;
    if (CollectionUtils.isNotEmpty(deleteIds)) {
      remove = merchantStoreRelationDao.removeByIds(deleteIds);
    }
    if (CollectionUtils.isNotEmpty(merchantStoreRelations)) {
      updateBatch = merchantStoreRelationDao.saveOrUpdateBatch(merchantStoreRelations);
    }
    if (CollectionUtils.isNotEmpty(merchantStoreRelationNewList)) {
      saveBath = merchantStoreRelationDao.saveBatch(merchantStoreRelationNewList);
    }

    // TODO 同步门店消费能力
    return remove && updateBatch && saveBath;
  }

  @Override
  public boolean updateStatus(Long id, Integer delete, Integer status) {
    QueryWrapper<MerchantStoreRelation> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq(MerchantStoreRelation.ID, id);
    MerchantStoreRelation merchantStoreRelation = merchantStoreRelationDao.getOne(queryWrapper);

    queryWrapper = new QueryWrapper<>();
    queryWrapper.eq(MerchantStoreRelation.MER_CODE, merchantStoreRelation.getMerCode());
    List<MerchantStoreRelation> merchantStoreRelations = merchantStoreRelationDao.list(
        queryWrapper);

    if (CollectionUtils.isNotEmpty(merchantStoreRelations)) {
      for (MerchantStoreRelation storeRelation :
          merchantStoreRelations) {
        if (delete != null) {
          storeRelation.setDeleted(delete != 0);
        }

        if (status != null) {
          storeRelation.setStatus(status);
        }
      }
    } else {
      return true;
    }

    return merchantStoreRelationDao.saveOrUpdateBatch(merchantStoreRelations);
  }
}