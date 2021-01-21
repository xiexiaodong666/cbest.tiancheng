package com.welfare.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.welfare.common.constants.WelfareConstant;
import com.welfare.common.enums.ConsumeTypeEnum;
import com.welfare.common.enums.MerIdentityEnum;
import com.welfare.common.enums.ShoppingActionTypeEnum;
import com.welfare.common.enums.SupplierStoreSourceEnum;
import com.welfare.common.exception.BusiException;
import com.welfare.common.exception.ExceptionCode;
import com.welfare.common.util.ConsumeTypesUtils;
import com.welfare.common.util.EmptyChecker;
import com.welfare.common.util.GenerateCodeUtil;
import com.welfare.persist.dao.MerchantStoreRelationDao;
import com.welfare.persist.dao.SupplierStoreDao;
import com.welfare.persist.dto.SupplierStoreWithMerchantDTO;
import com.welfare.persist.dto.query.StorePageReq;
import com.welfare.persist.entity.Merchant;
import com.welfare.persist.entity.MerchantAddress;
import com.welfare.persist.entity.MerchantStoreRelation;
import com.welfare.persist.entity.SupplierStore;
import com.welfare.persist.mapper.SupplierStoreExMapper;
import com.welfare.service.AccountConsumeSceneStoreRelationService;
import com.welfare.service.DictService;
import com.welfare.service.MerchantAddressService;
import com.welfare.service.MerchantService;
import com.welfare.service.SupplierStoreService;
import com.welfare.service.converter.SupplierStoreAddConverter;
import com.welfare.service.converter.SupplierStoreDetailConverter;
import com.welfare.service.converter.SupplierStoreSyncConverter;
import com.welfare.service.converter.SupplierStoreTreeConverter;
import com.welfare.service.dto.DictReq;
import com.welfare.service.dto.MerchantAddressDTO;
import com.welfare.service.dto.MerchantAddressReq;
import com.welfare.service.dto.MerchantReq;
import com.welfare.service.dto.SupplierStoreActivateReq;
import com.welfare.service.dto.SupplierStoreAddDTO;
import com.welfare.service.dto.SupplierStoreDetailDTO;
import com.welfare.service.dto.SupplierStoreImportDTO;
import com.welfare.service.dto.SupplierStoreListReq;
import com.welfare.service.dto.SupplierStoreSyncDTO;
import com.welfare.service.dto.SupplierStoreTreeDTO;
import com.welfare.service.dto.SupplierStoreUpdateDTO;
import com.welfare.service.helper.QueryHelper;
import com.welfare.service.listener.SupplierStoreListener;
import com.welfare.service.remote.entity.RoleConsumptionBindingsReq;
import com.welfare.service.remote.entity.RoleConsumptionListReq;
import com.welfare.service.remote.entity.RoleConsumptionReq;
import com.welfare.service.sync.event.MerchantStoreRelationEvt;
import com.welfare.service.sync.event.SupplierStoreEvt;
import com.welfare.service.utils.TreeUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;


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
  private final SupplierStoreAddConverter supplierStoreAddConverter;

  private final MerchantStoreRelationDao merchantStoreRelationDao;

  private final SupplierStoreExMapper supplierStoreExMapper;
  private final SupplierStoreTreeConverter supplierStoreTreeConverter;

  private final MerchantService merchantService;
  private final ApplicationContext applicationContext;
  private final ObjectMapper mapper;
  private final SupplierStoreDetailConverter supplierStoreDetailConverter;
  private final SupplierStoreSyncConverter supplierStoreSyncConverter;

  private final DictService dictService;
  private final AccountConsumeSceneStoreRelationService accountConsumeSceneStoreRelationService;
  private final MerchantAddressService merchantAddressService;


  @Override
  public List<SupplierStore> list(SupplierStoreListReq req) {
    QueryWrapper<SupplierStore> q = QueryHelper.getWrapper(req);
    q.orderByDesc(SupplierStore.CREATE_TIME);
    return supplierStoreDao.list(q);
  }

  @Override
  public List<SupplierStoreTreeDTO> tree(String merCode, String source) {
    Set<String> merCodes = new HashSet<>();
    if (Strings.isNotEmpty(merCode)) {
      merCodes.add(merCode);
    }
    List<SupplierStore> supplierStores;
    // 消费门店配置拉取
    if (SupplierStoreSourceEnum.MERCHANT_STORE_RELATION.getCode().equals(source)) {
      MerchantReq req = new MerchantReq();
      req.setMerIdentity(MerIdentityEnum.supplier.getCode());
      List<Merchant> merchantList = merchantService.list(req);
      List<String> merCodeList = merchantList.stream().map(
          Merchant::getMerCode).collect(Collectors.toList());
      merCodes = new HashSet<>(merCodeList);
      supplierStores = supplierStoreExMapper.listUnionMerchant(merCodes);
      for (SupplierStore s :
          supplierStores) {
        try {
          if (Strings.isNotEmpty(s.getConsumType())) {
            Map<String, Boolean> consumeTypeMap = mapper.readValue(
                s.getConsumType(), Map.class);
            ConsumeTypesUtils.removeFalseKey(consumeTypeMap);
            s.setConsumType(mapper.writeValueAsString(consumeTypeMap));
          } else {
            log.error(s.getConsumType() + "########");
          }

        } catch (JsonProcessingException e) {
          log.info("消费方式转换失败，格式错误【{}】", s.getConsumType());
        }

      }
    } else {
      supplierStores = supplierStoreExMapper.listUnionMerchant(merCodes);
    }

    List<SupplierStoreTreeDTO> treeDTOS = supplierStoreTreeConverter.toD(supplierStores);
    treeDTOS.forEach(item -> {
      item.setParentCode(item.getStoreParent());
      item.setCode(item.getStoreCode());

    });
    TreeUtil treeUtil = new TreeUtil(treeDTOS, "0");
    return treeUtil.getTree();
  }

  @Override
  public Page<SupplierStoreWithMerchantDTO> page(Page page, StorePageReq req) {
    Page<SupplierStoreWithMerchantDTO> pageResult = supplierStoreExMapper.listWithMerchant(
        page, req);
    dictService.trans(SupplierStoreWithMerchantDTO.class, SupplierStore.class.getSimpleName(), true,
                      pageResult.getRecords().toArray()
    );
    // 消费门店拉取需要过滤已有配置的门店和 移除没有勾选的消费方法
/*    if (SupplierStoreSourceEnum.MERCHANT_STORE_RELATION.getCode().equals(req.getSource())) {

      QueryWrapper<MerchantStoreRelation> queryWrapper = new QueryWrapper<>();
      queryWrapper.eq(MerchantStoreRelation.MER_CODE, req.getMerCode());
      // queryWrapper.eq(MerchantStoreRelation.STATUS, 0);
      List<MerchantStoreRelation> merchantStoreRelations = merchantStoreRelationDao.list(
          queryWrapper);
      if (CollectionUtils.isNotEmpty(merchantStoreRelations)) {
        List<String> storeCodeList = merchantStoreRelations.stream().map(
            MerchantStoreRelation::getStoreCode).collect(Collectors.toList());
        Set<String> storeCodeSet = new HashSet<>(storeCodeList);

        req.setStoreCodes(storeCodeSet);
        pageResult = supplierStoreExMapper.listWithMerchant(page, req);

      }
      List<SupplierStoreWithMerchantDTO> supplierStoreWithMerchantDTOS = pageResult.getRecords();

      for (SupplierStoreWithMerchantDTO s :
          supplierStoreWithMerchantDTOS) {
        try {
          Map<String, Boolean> consumeTypeMap = mapper.readValue(
              s.getConsumType(), Map.class);
          ConsumeTypesUtils.removeFalseKey(consumeTypeMap);
          s.setConsumType(mapper.writeValueAsString(consumeTypeMap));
        } catch (JsonProcessingException e) {
          log.info("消费方式转换失败，格式错误【{}】", s.getConsumType());
        }

      }

    }*/
    return pageResult;
  }

  @Override
  public SupplierStoreDetailDTO detail(Long id) {
    SupplierStoreDetailDTO store = supplierStoreDetailConverter.toD(supplierStoreDao.getById(id));
    if (EmptyChecker.isEmpty(store)) {
      throw new BusiException("门店不存在");
    }
    try {
      Map<String, Boolean> consumeTypeMap = mapper.readValue(
          store.getConsumType(), Map.class);
      store.setConsumType(ConsumeTypesUtils.transferStr(consumeTypeMap));
    } catch (JsonProcessingException e) {
      log.info("消费类型格式错误{}", store.getConsumType());
    }
    //商户名称
    store.setMerName(merchantService.getMerchantByMerCode(store.getMerCode()).getMerName());
    //自提点地址
    MerchantAddressReq merchantAddressReq = new MerchantAddressReq();
    merchantAddressReq.setRelatedType(SupplierStore.class.getSimpleName());
    merchantAddressReq.setRelatedId(store.getId());
    List<MerchantAddressDTO> addressDTOLis = merchantAddressService.list(merchantAddressReq);
    store.setAddressList(addressDTOLis);
    dictService.trans(
        MerchantAddressDTO.class, MerchantAddress.class.getSimpleName(), true,
        addressDTOLis.toArray()
    );
    dictService.trans(
        SupplierStoreDetailDTO.class, SupplierStore.class.getSimpleName(), true,
        store
    );

    return store;
  }

  @Override
  public SupplierStore getSupplierStoreByStoreCode(String storeCode) {
    QueryWrapper<SupplierStore> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq(SupplierStore.STORE_CODE, storeCode);
    return supplierStoreDao.getOne(queryWrapper);
  }

  @Override
  public SupplierStore getSupplierStoreByCashierNo(String cashierNo) {
    QueryWrapper<SupplierStore> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq(SupplierStore.CASHIER_NO, cashierNo);
    return supplierStoreDao.getOne(queryWrapper);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public boolean add(SupplierStoreAddDTO supplierStore) {
    List<String> consumTypes=Arrays.asList(supplierStore.getConsumType().split(","));
    if(!ConsumeTypeEnum.getCodeList().containsAll(consumTypes)){
      throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS,"未输入正确的消费类型",null);
    }
    if(consumTypes.contains(ConsumeTypeEnum.O2O.getCode())
            &&consumTypes.contains(ConsumeTypeEnum.ONLINE_MALL.getCode())){
      throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS,"O2O和线上商城不能同时选择",null);
    }
    Merchant merchant = merchantService.detailByMerCode(supplierStore.getMerCode());
    if (EmptyChecker.isEmpty(merchant)) {
      throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "商户不存在", null);
    }
    if (!Arrays.asList(merchant.getMerIdentity().split(",")).contains(
        MerIdentityEnum.supplier.getCode())) {
      throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "非供应商商户", null);

    }
    if (EmptyChecker.notEmpty(this.getSupplierStoreByStoreCode(supplierStore.getStoreCode()))) {
      throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "门店编码已存在", null);

    }
    if (EmptyChecker.notEmpty(this.getSupplierStoreByCashierNo(supplierStore.getCashierNo()))) {
      throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "虚拟收银机号已存在", null);
    }

    supplierStore.setConsumType(
        JSON.toJSONString(ConsumeTypesUtils.transfer(supplierStore.getConsumType())));
    SupplierStore save = supplierStoreAddConverter.toE((supplierStore));
    save.setStatus(0);
    save.setStorePath(save.getMerCode() + "-" + save.getStoreCode());
    save.setStoreParent(save.getMerCode());
    boolean flag = supplierStoreDao.save(save) && merchantAddressService.saveOrUpdateBatch(
        supplierStore.getAddressList(), SupplierStore.class.getSimpleName(), save.getId());
    //同步商城中台
    if (!flag) {
      throw new BusiException("新增门店失败");
    }
    SupplierStoreSyncDTO detailDTO = supplierStoreSyncConverter.toD(save);
    detailDTO.setId(save.getId());
    detailDTO.setAddressList(supplierStore.getAddressList());
    List<SupplierStoreSyncDTO> syncList = new ArrayList<>();
    syncList.add(detailDTO);
    applicationContext.publishEvent(SupplierStoreEvt.builder().typeEnum(
        ShoppingActionTypeEnum.ADD).supplierStoreDetailDTOS(syncList).build());
    return flag;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public boolean activate(SupplierStoreActivateReq storeActivateReq) {
    SupplierStore supplierStore = supplierStoreDao.getById(storeActivateReq.getId());
    if (EmptyChecker.isEmpty(supplierStore)) {
      throw new BusiException("门店不存在");
    }
    supplierStore.setId(storeActivateReq.getId());
    supplierStore.setStatus(storeActivateReq.getStatus());
    boolean flag = supplierStoreDao.updateById(supplierStore);
    //同步商城中台
    if (!flag) {
      throw new BusiException("修改门店激活状态失败");
    }
    //更新需要全量数据传过去，这里需要再查一次门店的 地址数据
    SupplierStoreSyncDTO sync = supplierStoreSyncConverter.toD(supplierStore);
    MerchantAddressReq merchantAddressReq = new MerchantAddressReq();
    merchantAddressReq.setRelatedType(SupplierStore.class.getSimpleName());
    merchantAddressReq.setRelatedId(sync.getId());
    List<MerchantAddressDTO> syncAddress = merchantAddressService.list(merchantAddressReq);
    sync.setAddressList(syncAddress);
    List<SupplierStoreSyncDTO> syncList = new ArrayList<>();
    syncList.add(sync);
    applicationContext.publishEvent(SupplierStoreEvt.builder().typeEnum(
        ShoppingActionTypeEnum.UPDATE).supplierStoreDetailDTOS(syncList).build());
    return flag;
  }

  @Transactional(rollbackFor = Exception.class)
  public boolean batchAdd(List<SupplierStoreAddDTO> list) {
    List<SupplierStore> saves = supplierStoreAddConverter.toE((list));
    //存放门店code和地址的对应关系，用于批量新增门店后，存入对应地址
    Map<String, List<MerchantAddressDTO>> map = new HashMap<>();
    for (SupplierStoreAddDTO supplierStoreAddDTO : list) {
      map.put(supplierStoreAddDTO.getStoreCode(), supplierStoreAddDTO.getAddressList());
    }
    List<MerchantAddressDTO> addressDTOList = new ArrayList<>();
    List<SupplierStoreSyncDTO> syncList = new ArrayList<>();
    for (SupplierStore store : saves) {
      store.setStatus(0);
      store.setStorePath(store.getMerCode() + "-" + store.getStoreCode());
      store.setStoreParent(store.getMerCode());
      SupplierStoreSyncDTO syncDTO = supplierStoreSyncConverter.toD(store);
      List<MerchantAddressDTO> addressItemList = map.get(store.getStoreCode());
      if (EmptyChecker.notEmpty(addressItemList)) {
        syncDTO.setAddressList(addressItemList);
        addressItemList.forEach(item -> {
          item.setRelatedType(SupplierStore.class.getSimpleName());
          item.setRelatedId(store.getId());
        });
        addressDTOList.addAll(addressItemList);
      }
      syncList.add(syncDTO);

    }
    if (!supplierStoreDao.saveBatch(saves)) {
      throw new BusiException("导入门店--批量插入失败");
    }
    if (!merchantAddressService.batchSave(addressDTOList, SupplierStore.class.getSimpleName())) {
      throw new BusiException("导入门店--批量插入地址失败");
    }
    //同步商城中台
    applicationContext.publishEvent(SupplierStoreEvt.builder().typeEnum(
        ShoppingActionTypeEnum.ADD).supplierStoreDetailDTOS(syncList)
                                        .build());
    return Boolean.TRUE;
  }

  public List<SupplierStore> list(QueryWrapper<SupplierStore> queryWrapper) {
    return supplierStoreDao.list(queryWrapper);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public String upload(MultipartFile multipartFile) {
    DictReq req = new DictReq();
    req.setDictType(WelfareConstant.DictType.STORE_CONSUM_TYPE.code());
    SupplierStoreListener listener = new SupplierStoreListener(
        merchantService, this);
    try {
      EasyExcel.read(multipartFile.getInputStream(), SupplierStoreImportDTO.class, listener).sheet()
          .doRead();
    } catch (IOException e) {
      throw new BusiException("excel解析失败");
    }
    String result = listener.getUploadInfo().toString();
    listener.getUploadInfo().delete(0, listener.getUploadInfo().length());
    if (!SupplierStoreListener.success.equals(result)) {
      throw new BusiException(result);
    }
    return result;
  }

  @Override
  public boolean delete(Long id) {
    SupplierStore supplierStore = supplierStoreDao.getById(id);
    if (EmptyChecker.isEmpty(supplierStore)) {
      throw new BusiException("门店不存在");
    }
    Boolean flag = supplierStoreDao.removeById(id);
    merchantAddressService.delete(
        SupplierStore.class.getSimpleName(), id);
    //同步商城中台
    if (!flag) {
      throw new BusiException("删除门店失败");
    }
    List<SupplierStoreSyncDTO> syncList = new ArrayList<>();
    syncList.add(supplierStoreSyncConverter.toD(supplierStore));
    applicationContext.publishEvent(SupplierStoreEvt.builder().typeEnum(
        ShoppingActionTypeEnum.DELETE).supplierStoreDetailDTOS(syncList).build());
    return flag;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public boolean update(SupplierStoreUpdateDTO supplierStore) {
    List<String> consumTypes=Arrays.asList(supplierStore.getConsumType().split(","));
    if(!ConsumeTypeEnum.getCodeList().containsAll(consumTypes)){
      throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS,"未输入正确的消费类型",null);
    }
    boolean flag2 = true;
    if (EmptyChecker.notEmpty(supplierStore.getConsumType())) {
      supplierStore.setConsumType(
          JSON.toJSONString(ConsumeTypesUtils.transfer(supplierStore.getConsumType())));
      flag2 = this.syncConsumeType(supplierStore.getStoreCode(), supplierStore.getConsumType());
    }
    SupplierStore update = this.buildUpdate(supplierStore);
    update.setStoreParent(update.getMerCode());
    boolean flag = 1 == supplierStoreDao.updateAllColumnById(update);
    boolean flag3 = merchantAddressService.saveOrUpdateBatch(
        supplierStore.getAddressList(), SupplierStore.class.getSimpleName(), supplierStore.getId());
    //同步商城中台
    if (!(flag && flag2 && flag3)) {
      throw new BusiException("更新门店失败");
    }
    List<SupplierStoreSyncDTO> syncList = new ArrayList<>();
    SupplierStoreSyncDTO detailDTO = supplierStoreSyncConverter.toD(update);
    detailDTO.setAddressList(supplierStore.getAddressList());
    syncList.add(detailDTO);
    applicationContext.publishEvent(SupplierStoreEvt.builder().typeEnum(
        ShoppingActionTypeEnum.UPDATE).supplierStoreDetailDTOS(syncList).build());
    return flag && flag2 && flag3;
  }

  private SupplierStore buildUpdate(SupplierStoreUpdateDTO update) {
    SupplierStore entity = supplierStoreDao.getById(update.getId());
    if (EmptyChecker.isEmpty(entity)) {
      throw new BusiException("id不存在");
    }
    if (!update.getStoreCode().equals(entity.getStoreCode())
        && EmptyChecker.notEmpty(this.getSupplierStoreByStoreCode(update.getStoreCode()))) {
      throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "门店编码已存在", null);

    }
    if (EmptyChecker.notEmpty(update.getCashierNo())
        && !update.getCashierNo().equals(entity.getCashierNo())
        && EmptyChecker.notEmpty(this.getSupplierStoreByCashierNo(update.getCashierNo()))) {
      throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "虚拟收银机号已存在", null);
    }
    entity.setMerCode(update.getMerCode());
    entity.setCashierNo(update.getCashierNo());
    entity.setStoreCode(update.getStoreCode());
    entity.setStoreName(update.getStoreName());
    entity.setRemark(update.getRemark());
    entity.setConsumType(update.getConsumType());
    entity.setUpdateUser(update.getUpdateUser());
    entity.setExternalCode(update.getExternalCode());
    return entity;
  }

  @Override
  public List<SupplierStoreWithMerchantDTO> exportList(StorePageReq req) {
    return this.page(new Page(0, Integer.MAX_VALUE), req).getRecords();
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

    Map<String, Boolean> map = null;
    try {
      map = mapper.readValue(consumeType, Map.class);
    } catch (JsonProcessingException e) {
      log.error("[syncConsumeType] json convert error", consumeType);
    }

    if (map == null) {
      throw new BusiException("消费方法格式错误");
    }

    boolean isSelectO2O = map.get(ConsumeTypeEnum.O2O.getCode());
    boolean isSelectOnlineMall =
        map.get(ConsumeTypeEnum.ONLINE_MALL.getCode());
    boolean isSelectShopShopping =
        map.get(ConsumeTypeEnum.SHOP_SHOPPING.getCode());

    for (MerchantStoreRelation storeRelation :
        storeRelationList) {
      Map<String, Boolean> consumeTypeMap = null;
      try {
        consumeTypeMap = mapper.readValue(
            storeRelation.getConsumType(), Map.class);
      } catch (JsonProcessingException e) {
        log.error("[syncConsumeType] json convert error", storeRelation.getConsumType());
      }

      if (consumeTypeMap == null) {
        throw new BusiException("消费方法格式错误");
      }

      if (!isSelectO2O) {
        consumeTypeMap.remove(ConsumeTypeEnum.O2O.getCode());
      } else {
        if (!consumeTypeMap.containsKey(ConsumeTypeEnum.O2O.getCode())) {
          consumeTypeMap.put(ConsumeTypeEnum.O2O.getCode(), false);
        }
      }

      if (!isSelectOnlineMall) {
        consumeTypeMap.remove(ConsumeTypeEnum.ONLINE_MALL.getCode());
      } else {
        if (!consumeTypeMap.containsKey(ConsumeTypeEnum.ONLINE_MALL.getCode())) {
          consumeTypeMap.put(ConsumeTypeEnum.ONLINE_MALL.getCode(), false);
        }
      }

      if (!isSelectShopShopping) {
        consumeTypeMap.remove(ConsumeTypeEnum.SHOP_SHOPPING.getCode());
      } else {
        if (!consumeTypeMap.containsKey(ConsumeTypeEnum.SHOP_SHOPPING.getCode())) {
          consumeTypeMap.put(ConsumeTypeEnum.SHOP_SHOPPING.getCode(), false);
        }
      }

      boolean isSelectO2OSync = true;
      boolean isSelectOnlineMallSync = true;
      boolean isSelectShopShoppingSync = true;

      if (consumeTypeMap.get(ConsumeTypeEnum.O2O.getCode()) == null || !consumeTypeMap.get(
          ConsumeTypeEnum.O2O.getCode())) {
        isSelectO2OSync = false;
      }

      if (consumeTypeMap.get(ConsumeTypeEnum.ONLINE_MALL.getCode()) == null || !consumeTypeMap
          .get(ConsumeTypeEnum.ONLINE_MALL.getCode())) {
        isSelectOnlineMallSync = false;
      }

      if (consumeTypeMap.get(ConsumeTypeEnum.SHOP_SHOPPING.getCode()) == null || !consumeTypeMap
          .get(ConsumeTypeEnum.SHOP_SHOPPING.getCode())) {
        isSelectShopShoppingSync = false;
      }

      Assert.isTrue(
          isSelectO2OSync || isSelectOnlineMallSync || isSelectShopShoppingSync,
          "消费门店下消费方法不能全被置为空"
      );
      try {
        storeRelation.setConsumType(mapper.writeValueAsString(consumeTypeMap));
      } catch (JsonProcessingException e) {
        log.error("[syncConsumeType] writeValueAsString error", consumeTypeMap);
        throw new BusiException("消费方法格式错误");
      }

      // 同步员工消费方法
      accountConsumeSceneStoreRelationService.updateStoreConsumeType(
          storeRelation.getMerCode(), storeRelation.getStoreCode(), storeRelation.getConsumType());
    }

    boolean isSaveOrUpdateBatch = merchantStoreRelationDao.saveOrUpdateBatch(storeRelationList);

    Map<String, List<MerchantStoreRelation>> mapByMerCode = storeRelationList.stream()
        .collect(Collectors.groupingBy(t -> t.getMerCode()));
    queryWrapper = new QueryWrapper<>();
    queryWrapper.in(MerchantStoreRelation.MER_CODE, mapByMerCode.keySet());
    List<MerchantStoreRelation> storeRelationListByMerCode = merchantStoreRelationDao.list(
        queryWrapper);

    Map<String, List<MerchantStoreRelation>> mapByMerCodeAll = storeRelationListByMerCode.stream()
        .collect(Collectors.groupingBy(t -> t.getMerCode()));

    for (Map.Entry<String, List<MerchantStoreRelation>> m : mapByMerCodeAll.entrySet()) {
      RoleConsumptionReq roleConsumptionReq = new RoleConsumptionReq();
      roleConsumptionReq.setActionType(ShoppingActionTypeEnum.UPDATE.getCode());
      roleConsumptionReq.setTimestamp(new Date());
      roleConsumptionReq.setRequestId(GenerateCodeUtil.getAccountIdByUUId());

      List<RoleConsumptionListReq> roleConsumptionListReqList = new ArrayList<>();

      RoleConsumptionListReq roleConsumptionListReq = new RoleConsumptionListReq();
      roleConsumptionListReqList.add(roleConsumptionListReq);
      roleConsumptionReq.setList(roleConsumptionListReqList);

      List<RoleConsumptionBindingsReq> roleConsumptionBindingsReqList = new ArrayList<>();
      roleConsumptionListReq.setBindings(roleConsumptionBindingsReqList);

      for (MerchantStoreRelation merchantStoreRelation :
          m.getValue()) {

        RoleConsumptionBindingsReq roleConsumptionBindingsReq = new RoleConsumptionBindingsReq();
        Map<String, Boolean> consumeTypeMap = null;
        try {
          roleConsumptionListReq.setMerchantCode(merchantStoreRelation.getMerCode());
          roleConsumptionListReq.setEnabled(merchantStoreRelation.getStatus() == 1);

          consumeTypeMap = mapper.readValue(
              merchantStoreRelation.getConsumType(), Map.class);
        } catch (JsonProcessingException e) {
          log.error("[syncConsumeType] json convert error", merchantStoreRelation.getConsumType());
        }
        if (consumeTypeMap == null) {
          throw new BusiException("消费方法格式错误");
        }
        roleConsumptionBindingsReq.setConsumeTypes(ConsumeTypesUtils.transfer(consumeTypeMap));
        roleConsumptionBindingsReq.setStoreCode(merchantStoreRelation.getStoreCode());
        roleConsumptionBindingsReqList.add(roleConsumptionBindingsReq);
      }

      MerchantStoreRelationEvt evt = new MerchantStoreRelationEvt();
      evt.setRoleConsumptionReq(roleConsumptionReq);
      applicationContext.publishEvent(evt);
    }

    return isSaveOrUpdateBatch;
  }

}