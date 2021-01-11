package com.welfare.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.welfare.common.enums.ConsumeTypeEnum;
import com.welfare.common.util.EmptyChecker;
import com.welfare.persist.dao.MerchantStoreRelationDao;
import com.welfare.persist.dao.SupplierStoreDao;
import com.welfare.persist.dto.SupplierStoreWithMerchantDTO;
import com.welfare.persist.dto.query.StorePageReq;
import com.welfare.persist.entity.MerchantStoreRelation;
import com.welfare.persist.entity.SupplierStore;
import com.welfare.persist.mapper.SupplierStoreExMapper;
import com.welfare.service.SupplierStoreService;
import com.welfare.service.helper.QueryHelper;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 供应商门店服务接口实现
 *
 * @author Yuxiang Li
 * @description 由 Mybatisplus Code Generator 创建
 * @since 2021-01-06 13:49:25
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class SupplierStoreServiceImpl implements SupplierStoreService {

  private final SupplierStoreDao supplierStoreDao;

  private final MerchantStoreRelationDao merchantStoreRelationDao;

  private final SupplierStoreExMapper supplierStoreExMapper;

  private final ObjectMapper mapper;

  @Override
  public Page<SupplierStoreWithMerchantDTO> page(Page page, StorePageReq req) {
    Page<SupplierStoreWithMerchantDTO> pageResult=supplierStoreExMapper.listWithMerchant(page, req);
    return pageResult;
  }

  @Override
  public SupplierStore detail(Long id) {
    return supplierStoreDao.getById(id);
  }

  @Override
  public SupplierStore getSupplierStoreByStoreCode(String storeCode) {
    QueryWrapper<SupplierStore> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq(SupplierStore.STORE_CODE, storeCode);
    return supplierStoreDao.getOne(queryWrapper);
  }

  @Override
  public boolean add(SupplierStore supplierStore) {
    return supplierStoreDao.save(supplierStore);
  }

  @Override
  public boolean activate(Long id, Integer status) {
    SupplierStore supplierStore = new SupplierStore();
    supplierStore.setId(id);
    supplierStore.setStatus(status);
    return supplierStoreDao.updateById(supplierStore);
  }

  @Override
  public boolean batchAdd(List<SupplierStore> list) {
    return supplierStoreDao.saveBatch(list);
  }

  @Override
  public boolean delete(Long id) {
    return supplierStoreDao.removeById(id);
  }

  @Override
  public boolean update(SupplierStore supplierStore) {
    if(EmptyChecker.notEmpty(supplierStore.getConsumType())){
      this.syncConsumeType(supplierStore.getStoreCode(),supplierStore.getStoreCode());
    }
    return supplierStoreDao.updateById(supplierStore);
  }

  @Override
  public String exportList(StorePageReq req) {
    return null;
  }

  @Override
  @Transactional(rollbackFor = {Exception.class})
  public boolean syncConsumeType(String storeCode, String consumeType) {
    QueryWrapper<MerchantStoreRelation> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq(MerchantStoreRelation.STORE_CODE, storeCode);

    List<MerchantStoreRelation> storeRelationList = merchantStoreRelationDao.list(queryWrapper);

    if (CollectionUtils.isEmpty(storeRelationList)) {
      return true;
    }

    try {
      Map<String, String> map = mapper.readValue(consumeType, Map.class);
      boolean isSelectO2O = Boolean.parseBoolean(map.get(ConsumeTypeEnum.O2O.getCode()));
      boolean isSelectOnlineMall = Boolean.parseBoolean(
          map.get(ConsumeTypeEnum.ONLINE_MALL.getCode()));
      boolean isSelectShopShopping = Boolean.parseBoolean(
          map.get(ConsumeTypeEnum.SHOP_SHOPPING.getCode()));

      for (MerchantStoreRelation storeRelation :
          storeRelationList) {
        Map<String, String> consumeTypeMap = mapper.readValue(
            storeRelation.getConsumType(), Map.class);
        if (!isSelectO2O) {
          consumeTypeMap.remove(ConsumeTypeEnum.O2O.getCode());
        } else {
          if(!consumeTypeMap.containsKey(ConsumeTypeEnum.O2O.getCode())) {
            consumeTypeMap.put(ConsumeTypeEnum.O2O.getCode(), "false");
          }
        }

        if (!isSelectOnlineMall) {
          consumeTypeMap.remove(ConsumeTypeEnum.ONLINE_MALL.getCode());
        } else {
          if(!consumeTypeMap.containsKey(ConsumeTypeEnum.ONLINE_MALL.getCode())) {
            consumeTypeMap.put(ConsumeTypeEnum.ONLINE_MALL.getCode(), "false");
          }
        }

        if (!isSelectShopShopping) {
          consumeTypeMap.remove(ConsumeTypeEnum.SHOP_SHOPPING.getCode());
        } else {
          if(!consumeTypeMap.containsKey(ConsumeTypeEnum.SHOP_SHOPPING.getCode())) {
            consumeTypeMap.put(ConsumeTypeEnum.SHOP_SHOPPING.getCode(), "false");
          }
        }

        storeRelation.setConsumType(mapper.writeValueAsString(consumeTypeMap));
      }
    } catch (JsonProcessingException e) {
      log.error("[syncConsumeType] json convert error", consumeType);
    }

    return merchantStoreRelationDao.saveOrUpdateBatch(storeRelationList);
  }


}