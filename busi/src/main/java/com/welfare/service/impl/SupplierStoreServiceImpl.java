package com.welfare.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.welfare.common.constants.WelfareConstant;
import com.welfare.common.enums.ConsumeTypeEnum;
import com.welfare.common.enums.ShoppingActionTypeEnum;
import com.welfare.common.enums.SupplierStoreSourceEnum;
import com.welfare.common.exception.BusiException;
import com.welfare.common.util.ConsumeTypesUtils;
import com.welfare.common.util.EmptyChecker;
import com.welfare.common.util.GenerateCodeUtil;
import com.welfare.persist.dao.MerchantStoreRelationDao;
import com.welfare.persist.dao.SupplierStoreDao;
import com.welfare.persist.dto.SupplierStoreWithMerchantDTO;
import com.welfare.persist.dto.query.StorePageReq;
import com.welfare.persist.entity.MerchantAddress;
import com.welfare.persist.entity.MerchantStoreRelation;
import com.welfare.persist.entity.SupplierStore;
import com.welfare.persist.mapper.SupplierStoreExMapper;
import com.welfare.service.AccountConsumeSceneStoreRelationService;
import com.welfare.service.DictService;
import com.welfare.service.MerchantAddressService;
import com.welfare.service.MerchantService;
import com.welfare.service.SupplierStoreService;
import com.welfare.service.converter.SupplierStoreDetailConverter;
import com.welfare.service.dto.DictDTO;
import com.welfare.service.dto.DictReq;
import com.welfare.service.dto.MerchantAddressDTO;
import com.welfare.service.dto.MerchantAddressReq;
import com.welfare.service.dto.MerchantDetailDTO;
import com.welfare.service.dto.SupplierStoreActivateReq;
import com.welfare.service.dto.SupplierStoreDetailDTO;
import com.welfare.service.dto.SupplierStoreImportDTO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.welfare.service.dto.SupplierStoreListReq;
import com.welfare.service.helper.QueryHelper;
import com.welfare.service.listener.SupplierStoreListener;
import com.welfare.service.remote.ShoppingFeignClient;
import com.welfare.service.remote.entity.RoleConsumptionResp;
import com.welfare.service.remote.entity.StoreShoppingReq;
import com.welfare.service.sync.event.SupplierStoreActivateEvt;
import com.welfare.service.sync.event.SupplierStoreAddEvt;
import com.welfare.service.sync.event.SupplierStoreBatchAddEvt;
import com.welfare.service.sync.event.SupplierStoreDeleteEvt;
import com.welfare.service.sync.event.SupplierStoreUpdateEvt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
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

    private final MerchantStoreRelationDao merchantStoreRelationDao;

    private final SupplierStoreExMapper supplierStoreExMapper;

    private final MerchantService merchantService;
    private final ApplicationContext applicationContext;
    private final ObjectMapper mapper;
    private final SupplierStoreDetailConverter supplierStoreDetailConverter;
    private final ShoppingFeignClient shoppingFeignClient;

    private final DictService dictService;
    private final AccountConsumeSceneStoreRelationService accountConsumeSceneStoreRelationService;
    private final MerchantAddressService merchantAddressService;


    @Override
    public List<SupplierStore> list(SupplierStoreListReq req) {
        return supplierStoreDao.list(QueryHelper.getWrapper(req));
    }

    @Override
    public Page<SupplierStoreWithMerchantDTO> page(Page page, StorePageReq req) {
        Page<SupplierStoreWithMerchantDTO> pageResult = supplierStoreExMapper.listWithMerchant(page, req);
        List<SupplierStoreWithMerchantDTO> supplierStoreWithMerchantDTOS = pageResult.getRecords();
        // 消费门店拉取需要过滤已有配置的门店和 移除没有勾选的消费方法
        if (SupplierStoreSourceEnum.MERCHANT_STORE_RELATION.getCode().equals(req.getSource())) {
            QueryWrapper<MerchantStoreRelation> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq(MerchantStoreRelation.MER_CODE, req.getMerCode());
            queryWrapper.eq(MerchantStoreRelation.STATUS, 0);
            List<MerchantStoreRelation> merchantStoreRelations = merchantStoreRelationDao.list(queryWrapper);
            if (CollectionUtils.isNotEmpty(merchantStoreRelations)) {
                List<String> storeCodeList = merchantStoreRelations.stream().map(MerchantStoreRelation::getStoreCode).collect(Collectors.toList());

                supplierStoreWithMerchantDTOS.stream().filter(s -> !storeCodeList.contains(s.getStoreCode())).collect(Collectors.toList());
            }

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

        }
        return pageResult;
    }

    @Override
    public SupplierStoreDetailDTO detail(Long id) {
        SupplierStoreDetailDTO store = supplierStoreDetailConverter.toD(supplierStoreDao.getById(id));
        if (EmptyChecker.isEmpty(store)) {
            throw new BusiException("门店不存在");
        }
        Map<String, Boolean> consumeTypeMap = null;
        try {
            consumeTypeMap = mapper.readValue(
                    store.getConsumType(), Map.class);
            store.setConsumType(ConsumeTypesUtils.transferStr(consumeTypeMap));
        } catch (JsonProcessingException e) {
            log.info("消费方式转换失败，格式错误【{}】", store.getConsumType());
        }
        //字典转义
        dictService.trans(SupplierStoreDetailDTO.class, SupplierStore.class.getSimpleName(), true, store);
        //商户名称
        store.setMerName(merchantService.getMerchantByMerCode(store.getMerCode()).getMerName());
        //自提点地址
        MerchantAddressReq merchantAddressReq = new MerchantAddressReq();
        merchantAddressReq.setRelatedType(SupplierStore.class.getSimpleName());
        merchantAddressReq.setRelatedId(store.getId());
        List<MerchantAddressDTO> addressDTOLis = merchantAddressService.list(merchantAddressReq);
        dictService.trans(MerchantAddressDTO.class, MerchantAddress.class.getSimpleName(), true, addressDTOLis.toArray());

        return store;
    }

    @Override
    public SupplierStore getSupplierStoreByStoreCode(String storeCode) {
        QueryWrapper<SupplierStore> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SupplierStore.STORE_CODE, storeCode);
        return supplierStoreDao.getOne(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(SupplierStoreDetailDTO supplierStore) {
        supplierStore.setStatus(0);
        if (EmptyChecker.notEmpty(this.getSupplierStoreByStoreCode(supplierStore.getStoreCode()))) {
            throw new BusiException("门店编码已存在");
        }
        SupplierStore save = supplierStoreDetailConverter.toE((supplierStore));
        save.setStoreParent(save.getMerCode() + "-" + save.getStoreCode());
        boolean flag = supplierStoreDao.save(save) && merchantAddressService.saveOrUpdateBatch(supplierStore.getAddressList(), SupplierStore.class.getSimpleName(), save.getId());
        //同步商城中台
        List<SupplierStoreDetailDTO> syncList = new ArrayList<>();
        syncList.add(supplierStore);
        applicationContext.publishEvent(SupplierStoreAddEvt.builder().typeEnum(ShoppingActionTypeEnum.ADD).supplierStoreDetailDTOS(syncList).build());
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
        List<SupplierStoreDetailDTO> syncList = new ArrayList<>();
        syncList.add(supplierStoreDetailConverter.toD(supplierStore));
        applicationContext.publishEvent(SupplierStoreActivateEvt.builder().typeEnum(ShoppingActionTypeEnum.UPDATE).supplierStoreDetailDTOS(syncList).build());
        return flag;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean batchAdd(List<SupplierStore> list) {
        boolean flag = supplierStoreDao.saveBatch(list);
        //同步商城中台
        applicationContext.publishEvent(SupplierStoreBatchAddEvt.builder().typeEnum(ShoppingActionTypeEnum.ADD).supplierStoreDetailDTOS(supplierStoreDetailConverter.toD(list)).build());
        return flag;
    }

    public List<SupplierStore> list(QueryWrapper<SupplierStore> queryWrapper) {
        return supplierStoreDao.list(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String upload(MultipartFile multipartFile) {
        DictReq req = new DictReq();
        req.setDictType(WelfareConstant.DictType.STORE_CONSUM_TYPE.code());
        //查询所有的消费类型字典，用来初始化
        List<DictDTO> dictList = dictService.getByType(req);
        Map<String, Boolean> map = dictList.stream().collect(Collectors.toMap(DictDTO::getDictCode, item -> false));
        try {
            SupplierStoreListener listener = new SupplierStoreListener(merchantService, this, JSON.toJSONString(map));
            EasyExcel.read(multipartFile.getInputStream(), SupplierStoreImportDTO.class, listener).sheet()
                    .doRead();
            String result = listener.getUploadInfo().toString();
            listener.getUploadInfo().delete(0, listener.getUploadInfo().length());
            return result;
        } catch (Exception e) {
            log.info("批量新增门店解析 Excel exception:{}", e.getMessage());
        }
        return "解析失败";
    }

    @Override
    public boolean delete(Long id) {
        SupplierStore supplierStore = supplierStoreDao.getById(id);
        if (EmptyChecker.isEmpty(supplierStore)) {
            throw new BusiException("门店不存在");
        }
        boolean flag = supplierStoreDao.removeById(id) && merchantAddressService.delete(SupplierStore.class.getSimpleName(), id);
        //同步商城中台
        List<SupplierStoreDetailDTO> syncList = new ArrayList<>();
        syncList.add(supplierStoreDetailConverter.toD(supplierStore));
        applicationContext.publishEvent(SupplierStoreDeleteEvt.builder().typeEnum(ShoppingActionTypeEnum.DELETE).supplierStoreDetailDTOS(syncList).build());
        return flag;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(SupplierStoreDetailDTO supplierStore) {
        supplierStore.setConsumType(JSON.toJSONString(ConsumeTypesUtils.transfer(supplierStore.getConsumType())));
        boolean flag2 = true;
        if (EmptyChecker.notEmpty(supplierStore.getConsumType())) {
            flag2 = this.syncConsumeType(supplierStore.getStoreCode(), supplierStore.getConsumType());
            accountConsumeSceneStoreRelationService.updateStoreConsumeType(supplierStore.getStoreCode(), supplierStore.getConsumType());
        }
        supplierStore.setConsumType(JSON.toJSONString(ConsumeTypesUtils.transfer(supplierStore.getConsumType())));
        SupplierStore update = supplierStoreDetailConverter.toE((supplierStore));
        boolean flag = supplierStoreDao.updateById(update);
        boolean flag3 = merchantAddressService.saveOrUpdateBatch(supplierStore.getAddressList(), SupplierStore.class.getSimpleName(), supplierStore.getId());
        //同步商城中台
        List<SupplierStoreDetailDTO> syncList = new ArrayList<>();
        syncList.add(supplierStore);
        applicationContext.publishEvent(SupplierStoreUpdateEvt.builder().typeEnum(ShoppingActionTypeEnum.UPDATE).supplierStoreDetailDTOS(syncList).build());
        return flag && flag2 && flag3;
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
                    if (!consumeTypeMap.containsKey(ConsumeTypeEnum.O2O.getCode())) {
                        consumeTypeMap.put(ConsumeTypeEnum.O2O.getCode(), "false");
                    }
                }

                if (!isSelectOnlineMall) {
                    consumeTypeMap.remove(ConsumeTypeEnum.ONLINE_MALL.getCode());
                } else {
                    if (!consumeTypeMap.containsKey(ConsumeTypeEnum.ONLINE_MALL.getCode())) {
                        consumeTypeMap.put(ConsumeTypeEnum.ONLINE_MALL.getCode(), "false");
                    }
                }

                if (!isSelectShopShopping) {
                    consumeTypeMap.remove(ConsumeTypeEnum.SHOP_SHOPPING.getCode());
                } else {
                    if (!consumeTypeMap.containsKey(ConsumeTypeEnum.SHOP_SHOPPING.getCode())) {
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