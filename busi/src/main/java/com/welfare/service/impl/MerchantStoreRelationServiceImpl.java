package com.welfare.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.welfare.common.enums.ConsumeTypeEnum;
import com.welfare.common.enums.ShoppingActionTypeEnum;
import com.welfare.common.exception.BusiException;
import com.welfare.common.exception.ExceptionCode;
import com.welfare.common.util.UserInfoHolder;
import com.welfare.common.util.ConsumeTypesUtils;
import com.welfare.common.util.GenerateCodeUtil;
import com.welfare.persist.dao.MerchantStoreRelationDao;
import com.welfare.persist.dto.AdminMerchantStore;
import com.welfare.persist.dto.MerchantStoreRelationDTO;
import com.welfare.persist.dto.query.MerchantStoreRelationAddReq;
import com.welfare.persist.dto.query.MerchantStoreRelationUpdateReq;
import com.welfare.persist.entity.MerchantStoreRelation;
import com.welfare.persist.entity.SupplierStore;
import com.welfare.persist.mapper.MerchantStoreRelationMapper;
import com.welfare.service.MerchantStoreRelationService;
import com.welfare.service.SupplierStoreService;
import com.welfare.service.remote.ShoppingFeignClient;
import com.welfare.service.remote.entity.RoleConsumptionBindingsReq;
import com.welfare.service.remote.entity.RoleConsumptionListReq;
import com.welfare.service.remote.entity.RoleConsumptionReq;
import com.welfare.service.remote.entity.RoleConsumptionResp;
import com.welfare.service.sync.event.MerchantStoreRelationEvt;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

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
  private final SupplierStoreService supplierStoreService;
  private final ObjectMapper mapper;

  private final ApplicationContext applicationContext;
  private final ShoppingFeignClient shoppingFeignClient;

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

    return merchantStoreRelationMapper.searchMerchantStoreRelations(
        page, merName, status, startTime, endTime);
  }

  @Override
  public List<MerchantStoreRelationDTO> exportMerchantStoreRelations(String merName, String status,
      Date startTime, Date endTime) {
    return merchantStoreRelationMapper.exportMerchantStoreRelations(merName, status, startTime, endTime);
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
  @Transactional(rollbackFor = {Exception.class})
  public boolean add(MerchantStoreRelationAddReq relationAddReq) {
    // 防止门店，消费门店  消费方法不一致
    if (!validateConsumeType(relationAddReq.getAdminMerchantStoreList())) {
      throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "门店,消费门店  消费方法不一致", null);
    }
    QueryWrapper<MerchantStoreRelation> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq(MerchantStoreRelation.MER_CODE, relationAddReq.getMerCode());

    List<MerchantStoreRelation> validateMerchantStoreRelationList = merchantStoreRelationDao.list(queryWrapper);
    if(CollectionUtils.isNotEmpty(validateMerchantStoreRelationList)) {
      throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "该商户已配置了门店", null);
    }

    RoleConsumptionReq roleConsumptionReq = new RoleConsumptionReq();
    List<RoleConsumptionListReq> roleConsumptionListReqs = new ArrayList<>();
    List<RoleConsumptionBindingsReq> roleConsumptionBindingsReqs = new ArrayList<>();

    roleConsumptionReq.setList(roleConsumptionListReqs);
    roleConsumptionReq.setActionType(ShoppingActionTypeEnum.ADD.getCode());
    roleConsumptionReq.setRequestId(GenerateCodeUtil.getAccountIdByUUId());
    roleConsumptionReq.setTimestamp(new Date());

    RoleConsumptionListReq roleConsumptionListReq = new RoleConsumptionListReq();
    roleConsumptionListReqs.add(roleConsumptionListReq);
    roleConsumptionListReq.setMerchantCode(relationAddReq.getMerCode());
    roleConsumptionListReq.setEnabled(Boolean.TRUE);

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
      merchantStoreRelation.setSyncStatus(0);

      if (UserInfoHolder.getUserInfo() != null) {
        merchantStoreRelation.setCreateUser(UserInfoHolder.getUserInfo().getUserName());
      }

      RoleConsumptionBindingsReq roleConsumptionBindingsReq = new RoleConsumptionBindingsReq();

      try {
        Map<String, Boolean> consumeTypeMap = mapper.readValue(
            merchantStoreRelation.getConsumType(), Map.class);

        roleConsumptionBindingsReq.setConsumeTypes(ConsumeTypesUtils.transfer(consumeTypeMap));
        roleConsumptionBindingsReq.setStoreCode(merchantStoreRelation.getStoreCode());

        roleConsumptionBindingsReqs.add(roleConsumptionBindingsReq);
        roleConsumptionListReq.setBindings(roleConsumptionBindingsReqs);

      } catch (JsonProcessingException e) {
        log.error("[add] json convert error", e.getMessage());
      }
      merchantStoreRelationList.add(merchantStoreRelation);
    }

    boolean save = merchantStoreRelationDao.saveBatch(merchantStoreRelationList);
    MerchantStoreRelationEvt evt = new MerchantStoreRelationEvt();
    evt.setRoleConsumptionReq(roleConsumptionReq);
    applicationContext.publishEvent(evt);

    return save;
  }

  @Override
  @Transactional(rollbackFor = {Exception.class})
  public boolean update(MerchantStoreRelationUpdateReq relationUpdateReq) {

    // 防止门店，消费门店  消费方法不一致
    if (!validateConsumeType(relationUpdateReq.getAdminMerchantStoreList())) {
      throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "门店,消费门店  消费方法不一致", null);
    }

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

    RoleConsumptionReq roleConsumptionReq = new RoleConsumptionReq();
    List<RoleConsumptionBindingsReq> roleConsumptionBindingsReqs = new ArrayList<>();

    List<RoleConsumptionListReq> roleConsumptionListReqs = new ArrayList<>();
    roleConsumptionReq.setList(roleConsumptionListReqs);
    roleConsumptionReq.setActionType(ShoppingActionTypeEnum.UPDATE.getCode());
    roleConsumptionReq.setRequestId(GenerateCodeUtil.getAccountIdByUUId());
    roleConsumptionReq.setTimestamp(new Date());

    RoleConsumptionListReq roleConsumptionListReq = new RoleConsumptionListReq();
    roleConsumptionListReqs.add(roleConsumptionListReq);
    roleConsumptionListReq.setMerchantCode(merchantStoreRelation.getMerCode());
    roleConsumptionListReq.setEnabled(Boolean.TRUE);

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
        merchantStoreRelationNew.setSyncStatus(0);
        if (UserInfoHolder.getUserInfo() != null) {
          merchantStoreRelationNew.setCreateUser(UserInfoHolder.getUserInfo().getUserName());
          merchantStoreRelationNew.setUpdateUser(UserInfoHolder.getUserInfo().getUserName());

        }
        merchantStoreRelationNew.setStatus(0);
        merchantStoreRelationNewList.add(merchantStoreRelationNew);

        RoleConsumptionBindingsReq roleConsumptionBindingsReq = new RoleConsumptionBindingsReq();

        try {
          Map<String, Boolean> consumeTypeMap = mapper.readValue(
              merchantStoreRelationNew.getConsumType(), Map.class);

          roleConsumptionBindingsReq.setConsumeTypes(ConsumeTypesUtils.transfer(consumeTypeMap));
          roleConsumptionBindingsReq.setStoreCode(merchantStoreRelationNew.getStoreCode());

          roleConsumptionBindingsReqs.add(roleConsumptionBindingsReq);
          roleConsumptionListReq.setBindings(roleConsumptionBindingsReqs);

        } catch (JsonProcessingException e) {
          log.error("[add] json convert error", e.getMessage());
        }
      }
    }
    List<MerchantStoreRelation> removeMerchantStoreRelationList = new ArrayList<>();

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
        m.setSyncStatus(0);

        RoleConsumptionBindingsReq roleConsumptionBindingsReq = new RoleConsumptionBindingsReq();

        try {
          Map<String, Boolean> consumeTypeMap = mapper.readValue(
              adminMerchantStore.getConsumType(), Map.class);

          roleConsumptionBindingsReq.setConsumeTypes(ConsumeTypesUtils.transfer(consumeTypeMap));
          roleConsumptionBindingsReq.setStoreCode(adminMerchantStore.getStoreCode());

          roleConsumptionBindingsReqs.add(roleConsumptionBindingsReq);
          roleConsumptionListReq.setBindings(roleConsumptionBindingsReqs);

        } catch (JsonProcessingException e) {
          log.error("[add] json convert error", e.getMessage());
        }

      } // delete
      else {
        deleteIds.add(m.getId());
        removeMerchantStoreRelationList.add(m);
      }
    }

    if (CollectionUtils.isNotEmpty(removeMerchantStoreRelationList)) {
      merchantStoreRelations.removeAll(removeMerchantStoreRelationList);
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

    MerchantStoreRelationEvt evt = new MerchantStoreRelationEvt();
    evt.setRoleConsumptionReq(roleConsumptionReq);
    applicationContext.publishEvent(evt);

    return remove && updateBatch && saveBath;
  }

  @Override
  @Transactional(rollbackFor = {Exception.class})
  public boolean updateStatus(Long id, Integer delete, Integer status) {
    QueryWrapper<MerchantStoreRelation> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq(MerchantStoreRelation.ID, id);
    MerchantStoreRelation merchantStoreRelation = merchantStoreRelationDao.getOne(queryWrapper);

    queryWrapper = new QueryWrapper<>();
    queryWrapper.eq(MerchantStoreRelation.MER_CODE, merchantStoreRelation.getMerCode());
    List<MerchantStoreRelation> merchantStoreRelations = merchantStoreRelationDao.list(
        queryWrapper);

    RoleConsumptionReq roleConsumptionReq = new RoleConsumptionReq();

    List<RoleConsumptionListReq> roleConsumptionListReqs = new ArrayList<>(1);
    roleConsumptionReq.setList(roleConsumptionListReqs);

    RoleConsumptionListReq roleConsumptionListReq = new RoleConsumptionListReq();
      roleConsumptionListReq.setMerchantCode(merchantStoreRelation.getMerCode());
    roleConsumptionListReqs.add(roleConsumptionListReq);

    roleConsumptionReq.setRequestId(GenerateCodeUtil.getAccountIdByUUId());
    roleConsumptionReq.setTimestamp(new Date());

    List<RoleConsumptionBindingsReq> roleConsumptionBindingsReqs = new ArrayList<>();

    if (status != null) {
      roleConsumptionListReq.setEnabled(status == 0);
    }

    List<Long> removeIds = new ArrayList<>();

    if (CollectionUtils.isNotEmpty(merchantStoreRelations)) {
      for (MerchantStoreRelation storeRelation :
          merchantStoreRelations) {
        if (delete != null) {
          roleConsumptionReq.setActionType(ShoppingActionTypeEnum.DELETE.getCode());

          removeIds.add(storeRelation.getId());
          storeRelation.setSyncStatus(0);
        }

        if (status != null) {
          roleConsumptionReq.setActionType(ShoppingActionTypeEnum.UPDATE.getCode());
          storeRelation.setSyncStatus(0);
          RoleConsumptionBindingsReq roleConsumptionBindingsReq = new RoleConsumptionBindingsReq();

          try {
            Map<String, Boolean> consumeTypeMap = mapper.readValue(
                storeRelation.getConsumType(), Map.class);
            roleConsumptionBindingsReq.setConsumeTypes(ConsumeTypesUtils.transfer(consumeTypeMap));
            roleConsumptionBindingsReq.setStoreCode(storeRelation.getStoreCode());
            roleConsumptionBindingsReqs.add(roleConsumptionBindingsReq);

            roleConsumptionListReq.setBindings(roleConsumptionBindingsReqs);

          } catch (JsonProcessingException e) {
            log.error("[updateStatus] json convert error", e.getMessage());
          }

          storeRelation.setStatus(status);
        }

      }
    } else {
      return true;
    }

    boolean saveOrUpdateBatch = merchantStoreRelationDao.saveOrUpdateBatch(merchantStoreRelations);

    boolean remove = true;
    if(CollectionUtils.isNotEmpty(removeIds)) {
      remove = merchantStoreRelationDao.removeByIds(removeIds);
    }

    MerchantStoreRelationEvt evt = new MerchantStoreRelationEvt();
    evt.setRoleConsumptionReq(roleConsumptionReq);
    applicationContext.publishEvent(evt);

    return remove && saveOrUpdateBatch;
  }

  private boolean validateConsumeType(List<AdminMerchantStore> merchantStoreList) {

    boolean validate = true;
    for (AdminMerchantStore merchantStore :
        merchantStoreList) {
      SupplierStore supplierStore = supplierStoreService.getSupplierStoreByStoreCode(
          merchantStore.getStoreCode());
      try {
        Map<String, Boolean> consumeTypeMap = mapper.readValue(
            supplierStore.getConsumType(), Map.class);

        Map<String, Boolean> merchantStoreConsumeTypeMap = mapper.readValue(
            merchantStore.getConsumType(), Map.class);
        boolean isSelectO2O = consumeTypeMap.get(ConsumeTypeEnum.O2O.getCode());
        if (!isSelectO2O && merchantStoreConsumeTypeMap.containsKey(
            ConsumeTypeEnum.O2O.getCode())) {
          validate = false;
          break;
        }
        boolean isSelectOnlineMall =
            consumeTypeMap.get(ConsumeTypeEnum.ONLINE_MALL.getCode());
        if (!isSelectOnlineMall && merchantStoreConsumeTypeMap.containsKey(
            ConsumeTypeEnum.ONLINE_MALL.getCode())) {
          validate = false;
          break;
        }
        boolean isSelectShopShopping =
            consumeTypeMap.get(ConsumeTypeEnum.SHOP_SHOPPING.getCode());
        if (!isSelectShopShopping && merchantStoreConsumeTypeMap.containsKey(
            ConsumeTypeEnum.SHOP_SHOPPING.getCode())) {
          validate = false;
          break;
        }

      } catch (JsonProcessingException e) {
        validate = false;
        log.error("[syncConsumeType] json convert error", e.getMessage());
      }
    }

    return validate;
  }
}