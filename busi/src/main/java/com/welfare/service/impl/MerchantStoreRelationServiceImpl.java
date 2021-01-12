package com.welfare.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.welfare.common.enums.ConsumeTypeEnum;
import com.welfare.common.exception.BusiException;
import com.welfare.common.exception.ExceptionCode;
import com.welfare.common.util.ApiUserHolder;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.util.Strings;
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

    RoleConsumptionReq roleConsumptionReq = new RoleConsumptionReq();
    List<RoleConsumptionListReq> roleConsumptionListReqs = new ArrayList<>();


    roleConsumptionReq.setList(roleConsumptionListReqs);
    roleConsumptionReq.setActionType("ADD");
    roleConsumptionReq.setRequestId(GenerateCodeUtil.getAccountIdByUUId());
    roleConsumptionReq.setTimestamp(new Date());

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

      if (ApiUserHolder.getUserInfo() != null) {
        merchantStoreRelation.setCreateUser(ApiUserHolder.getUserInfo().getUserName());
      }

      RoleConsumptionListReq roleConsumptionListReq = new RoleConsumptionListReq();
      roleConsumptionListReqs.add(roleConsumptionListReq);
      roleConsumptionListReq.setMerchantCode(relationAddReq.getMerCode());
      roleConsumptionListReq.setEnabled(Boolean.TRUE);

      List<RoleConsumptionBindingsReq> roleConsumptionBindingsReqs = new ArrayList<>();
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

    // send after tx commit but is async
    TransactionSynchronizationManager.registerSynchronization(
         new TransactionSynchronizationAdapter() {
          @Override
          public void afterCommit() {
              try {
                log.info(mapper.writeValueAsString(roleConsumptionReq));
                RoleConsumptionResp roleConsumptionResp = shoppingFeignClient.addOrUpdateRoleConsumption(roleConsumptionReq);

                if(roleConsumptionResp.equals("200")) {
                  // 写入
                  for (MerchantStoreRelation m:
                      merchantStoreRelationList) {
                    m.setSyncStatus(1);
                  }
                  merchantStoreRelationDao.saveOrUpdateBatch(merchantStoreRelationList);
                }
              } catch (Exception e) {
                log.error("[afterCommit] call addOrUpdateRoleConsumption error", e.getMessage());
              }

          }
        }
    );
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
    List<RoleConsumptionListReq> roleConsumptionListReqs = new ArrayList<>();
    roleConsumptionReq.setList(roleConsumptionListReqs);
    roleConsumptionReq.setActionType("UPDATE");
    roleConsumptionReq.setRequestId(GenerateCodeUtil.getAccountIdByUUId());
    roleConsumptionReq.setTimestamp(new Date());

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


    // send after tx commit but is async
    TransactionSynchronizationManager.registerSynchronization(
        new TransactionSynchronizationAdapter() {
          @Override
          public void afterCommit() {
            try {
              log.info(mapper.writeValueAsString(roleConsumptionReq));
              RoleConsumptionResp roleConsumptionResp = shoppingFeignClient.addOrUpdateRoleConsumption(roleConsumptionReq);

            } catch (Exception e) {
              log.error("[afterCommit] call addOrUpdateRoleConsumption error", e.getMessage());
            }

          }
        }
    );


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