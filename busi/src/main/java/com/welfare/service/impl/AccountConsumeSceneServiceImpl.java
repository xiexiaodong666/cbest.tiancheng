package com.welfare.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.welfare.common.constants.AccountChangeType;
import com.welfare.common.constants.AccountConsumeSceneStatus;
import com.welfare.common.enums.ShoppingActionTypeEnum;
import com.welfare.common.exception.BusiException;
import com.welfare.common.exception.ExceptionCode;
import com.welfare.common.util.MerchantUserHolder;
import com.welfare.persist.dao.AccountConsumeSceneDao;
import com.welfare.persist.dao.AccountConsumeSceneStoreRelationDao;
import com.welfare.persist.dao.MerchantAccountTypeDao;
import com.welfare.persist.dto.AccountConsumeSceneMapperDTO;
import com.welfare.persist.dto.AccountConsumeScenePageDTO;
import com.welfare.persist.dto.AccountConsumeStoreInfoDTO;
import com.welfare.persist.dto.query.AccountConsumePageQuery;
import com.welfare.persist.entity.*;
import com.welfare.persist.mapper.AccountConsumeSceneCustomizeMapper;
import com.welfare.persist.mapper.AccountConsumeSceneStoreRelationMapper;
import com.welfare.service.AccountChangeEventRecordService;
import com.welfare.service.AccountConsumeSceneService;
import com.welfare.service.AccountTypeService;
import com.welfare.service.MerchantService;
import com.welfare.service.dto.*;
import com.welfare.service.sync.event.AccountConsumeSceneEvt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 员工消费场景配置服务接口实现
 *
 * @author Yuxiang Li
 * @description 由 Mybatisplus Code Generator 创建
 * @since 2021-01-06 13:49:25
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class AccountConsumeSceneServiceImpl implements AccountConsumeSceneService {

  private final AccountConsumeSceneDao accountConsumeSceneDao;
  private final AccountConsumeSceneCustomizeMapper accountConsumeSceneCustomizeMapper;
  private final AccountConsumeSceneStoreRelationDao accountConsumeSceneStoreRelationDao;
  @Autowired
  MerchantService merchantService;
  private final AccountTypeService accountTypeService;
  private final AccountChangeEventRecordService accountChangeEventRecordService;
  private final ApplicationContext applicationContext;
  private final AccountConsumeSceneStoreRelationMapper accountConsumeSceneStoreRelationMapper;
  private final MerchantAccountTypeDao merchantAccountTypeDao;
  private final CacheManager cacheManager;
  @Override
  public AccountConsumeScene getAccountConsumeScene(Long id) {
    return accountConsumeSceneDao.getById(id);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public Boolean save(AccountConsumeSceneAddReq accountConsumeSceneAddReq) {
    List<String> accountTypeCodeList = accountConsumeSceneAddReq.getAccountTypeCodeList();
    if( CollectionUtils.isEmpty(accountTypeCodeList)  || accountTypeCodeList.size() == 0){
      throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS,"员工类型不能为空", null);
    }
    List<AccountConsumeSceneStoreRelation> sendData = new LinkedList<AccountConsumeSceneStoreRelation>();
    accountTypeCodeList.forEach(accountTypeCode -> {
      AccountConsumeScene accountConsumeScene = new AccountConsumeScene();
      BeanUtils.copyProperties(accountConsumeSceneAddReq, accountConsumeScene);
      accountConsumeScene.setAccountTypeCode(accountTypeCode);
      accountConsumeScene.setStatus(AccountConsumeSceneStatus.ENABLE.getCode());
      validationAccountConsumeScene(accountConsumeScene,true);
      accountConsumeSceneDao.save(accountConsumeScene);
      List<AccountConsumeSceneStoreRelation> accountConsumeSceneStoreRelationList = getAccountConsumeSceneStoreRelations(
          accountConsumeSceneAddReq, accountConsumeScene);
      accountConsumeSceneStoreRelationDao.saveBatch(accountConsumeSceneStoreRelationList);
      sendData.addAll(accountConsumeSceneStoreRelationList);
    });
    //下发数据
    applicationContext.publishEvent( AccountConsumeSceneEvt.builder().typeEnum(ShoppingActionTypeEnum.ADD).relationList(sendData).build());
    return true;
  }

  private void validationAccountConsumeScene(AccountConsumeScene accountConsumeScene,boolean isNew){
    Merchant merchant = merchantService.detailByMerCode(accountConsumeScene.getMerCode());
    if( null == merchant ) {
      throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS,"商户不存在",null);
    }
    AccountType queryAccountType = accountTypeService.queryByTypeCode(accountConsumeScene.getMerCode(),accountConsumeScene.getAccountTypeCode());
    if( null == queryAccountType ) {
      throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS,"商户员工类型不存在",null);
    }
    if(!isNew){
      AccountConsumeScene queryAccountConsumeScene = accountConsumeSceneDao.getById(accountConsumeScene.getId());
      if( null == queryAccountConsumeScene ){
        throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS,"员工类型消费场景不存在",null);
      }
      AccountConsumeScene sameTypeScene = queryAccountConsumeScene(accountConsumeScene.getMerCode(),accountConsumeScene.getAccountTypeCode());
      if( sameTypeScene.getId().compareTo(accountConsumeScene.getId())!= 0){
        throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS,"该商户已经存在相同员工类型的消费场景配置",null);
      }
    }else{
      AccountConsumeScene queryAccountConsumeScene = queryAccountConsumeScene(accountConsumeScene.getMerCode(),accountConsumeScene.getAccountTypeCode());
      if(null != queryAccountConsumeScene){
        throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS,"该商户已经存在相同员工类型的消费场景配置",null);
      }
    }
  }

  public AccountConsumeScene queryAccountConsumeScene(String merCode,String accountTypecode){
    QueryWrapper<AccountConsumeScene> queryWrapper = new QueryWrapper<AccountConsumeScene>();
    queryWrapper.eq(AccountConsumeScene.MER_CODE,merCode);
    queryWrapper.eq(AccountConsumeScene.ACCOUNT_TYPE_CODE,accountTypecode);
    return accountConsumeSceneDao.getOne(queryWrapper);
  }

  private List<AccountConsumeSceneStoreRelation> getAccountConsumeSceneStoreRelations(
      AccountConsumeSceneAddReq accountConsumeSceneAddReq,
      AccountConsumeScene accountConsumeScene) {
    List<AccountConsumeSceneStoreRelationReq> accountConsumeSceneStoreRelationReqList = accountConsumeSceneAddReq
        .getAccountConsumeSceneStoreRelationReqList();
    List<AccountConsumeSceneStoreRelation> accountConsumeSceneStoreRelationList = new ArrayList<>(
        accountConsumeSceneStoreRelationReqList.size());
    accountConsumeSceneStoreRelationReqList.forEach(accountConsumeSceneStoreRelationReq -> {
      if(StringUtils.isBlank(accountConsumeSceneStoreRelationReq.getSceneConsumType())){
        throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS,"请选择该员工类型在该门店下得消费方式", null);
      }
      AccountConsumeSceneStoreRelation accountConsumeSceneStoreRelation = new AccountConsumeSceneStoreRelation();
      BeanUtils
          .copyProperties(accountConsumeSceneStoreRelationReq, accountConsumeSceneStoreRelation);
      accountConsumeSceneStoreRelation.setAccountConsumeSceneId(accountConsumeScene.getId());
      accountConsumeSceneStoreRelationList.add(accountConsumeSceneStoreRelation);
    });
    return accountConsumeSceneStoreRelationList;
  }


  @Override
  @Transactional(rollbackFor = Exception.class)
  public Boolean update(AccountConsumeSceneReq accountConsumeSceneReq) {
    AccountConsumeScene accountConsumeScene = new AccountConsumeScene();
    BeanUtils.copyProperties(accountConsumeSceneReq, accountConsumeScene);
    validationAccountConsumeScene(accountConsumeScene,false);
    accountConsumeSceneDao.updateById(accountConsumeScene);
    UpdateWrapper wrapper = new UpdateWrapper();
    wrapper.eq(AccountConsumeSceneStoreRelation.ACCOUNT_CONSUME_SCENE_ID,accountConsumeScene.getId());
    accountConsumeSceneStoreRelationDao.remove(wrapper);
    List<AccountConsumeSceneStoreRelation> accountConsumeSceneStoreRelationList = new ArrayList<>();
    accountConsumeSceneReq.getAccountConsumeSceneStoreRelationReqList().stream()
        .forEach(accountConsumeSceneStoreRelationReq -> {
          AccountConsumeSceneStoreRelation accountConsumeSceneStoreRelation = new AccountConsumeSceneStoreRelation();
          BeanUtils.copyProperties(accountConsumeSceneStoreRelationReq,
              accountConsumeSceneStoreRelation);
          accountConsumeSceneStoreRelation.setAccountConsumeSceneId(accountConsumeScene.getId());
          accountConsumeSceneStoreRelationList.add(accountConsumeSceneStoreRelation);
        });
    boolean updateResult = accountConsumeSceneStoreRelationDao.saveOrUpdateBatch(accountConsumeSceneStoreRelationList);
    if( updateResult ){
      accountChangeEventRecordService.batchSaveBySceneStoreRelation(accountConsumeSceneStoreRelationList);
      //下发数据
      List<AccountConsumeSceneStoreRelation> allRelations = accountConsumeSceneStoreRelationMapper.queryAllRelationList(accountConsumeSceneReq.getMerCode());
      applicationContext.publishEvent( AccountConsumeSceneEvt.builder().typeEnum(ShoppingActionTypeEnum.UPDATE).relationList(allRelations).build());
    }
    return true;
  }


  @Override
  @Transactional(rollbackFor = Exception.class)
  public Boolean delete(Long id) {
    AccountConsumeScene accountConsumeScene = accountConsumeSceneDao.getById(id);
    if( null == accountConsumeScene ) {
      throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS,"消费场景不存在",null);
    }
    boolean deleteResult =  accountConsumeSceneDao.removeById(id);
    if(deleteResult){
      accountChangeEventRecordService.batchSaveByAccountTypeCode(accountConsumeScene.getAccountTypeCode(),AccountChangeType.ACCOUNT_CONSUME_SCENE_DELETE);
    }
    //删除关联relation
    QueryWrapper<AccountConsumeSceneStoreRelation> queryWrapper = new QueryWrapper<AccountConsumeSceneStoreRelation>();
    queryWrapper.eq(AccountConsumeSceneStoreRelation.ACCOUNT_CONSUME_SCENE_ID,accountConsumeScene.getId());
    accountConsumeSceneStoreRelationDao.remove(queryWrapper);

    //下发数据
    List<AccountConsumeSceneStoreRelation> allRelations = accountConsumeSceneStoreRelationMapper.queryAllRelationList(accountConsumeScene.getMerCode());
    applicationContext.publishEvent( AccountConsumeSceneEvt.builder().typeEnum(ShoppingActionTypeEnum.UPDATE).relationList(allRelations).build());
    return deleteResult;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public Boolean updateStatus(Long id, Integer status) {
    AccountConsumeScene queryAC = accountConsumeSceneDao.getById(id);
    if( null == queryAC ) {
      throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS,"该消费场景不存在",null);
    }
    UpdateWrapper<AccountConsumeScene> updateWrapper = new UpdateWrapper();
    updateWrapper.eq(AccountConsumeScene.ID, id);
    AccountConsumeScene accountConsumeScene = new AccountConsumeScene();
    accountConsumeScene.setStatus(status);
    boolean updateResult =  accountConsumeSceneDao.update(accountConsumeScene, updateWrapper);
    if(updateResult){
      accountChangeEventRecordService.batchSaveByAccountTypeCode(accountConsumeScene.getAccountTypeCode(),AccountChangeType.getByAccountConsumeStatus(status));
      List<AccountConsumeSceneStoreRelation> allRelations = accountConsumeSceneStoreRelationMapper.queryAllRelationList(queryAC.getMerCode());
      applicationContext.publishEvent( AccountConsumeSceneEvt.builder().typeEnum(ShoppingActionTypeEnum.UPDATE).relationList(allRelations).build());
    }
    return updateResult;
  }

  @Override
  public IPage<AccountConsumeScenePageDTO> getPageDTO(Page<AccountConsumeScenePageDTO> page,
      AccountConsumePageQuery accountConsumePageReq) {
    return accountConsumeSceneCustomizeMapper.getPageDTO(page, accountConsumePageReq.getMerCode(),
        accountConsumePageReq.getAccountTypeName(), accountConsumePageReq.getStatus(),
        accountConsumePageReq.getCreateTimeStart(), accountConsumePageReq.getCreateTimeEnd());
  }

  @Override
  public List<AccountConsumeScenePageDTO> export(AccountConsumePageQuery accountConsumePageReq) {
    return accountConsumeSceneCustomizeMapper.getPageDTO(accountConsumePageReq.getMerCode(),
        accountConsumePageReq.getAccountTypeName(), accountConsumePageReq.getStatus(),
        accountConsumePageReq.getCreateTimeStart(), accountConsumePageReq.getCreateTimeEnd());
  }

  @Override
  public AccountConsumeSceneDTO findAccountConsumeSceneDTOById(Long id) {
    AccountConsumeSceneDTO accountConsumeSceneDTO = new AccountConsumeSceneDTO();
    AccountConsumeSceneMapperDTO accountConsumeSceneMapperDTO = accountConsumeSceneCustomizeMapper
        .queryAccountConsumerScene4Detail(id);
    BeanUtils.copyProperties(accountConsumeSceneMapperDTO, accountConsumeSceneDTO);
    return accountConsumeSceneDTO;
  }

  @Override
  public List<AccountConsumeSceneResp> findAllAccountConsumeSceneDTO(String merCode) {
    List<AccountConsumeSceneResp> resps = new ArrayList<>();
    List<AccountConsumeStoreInfoDTO> consumeStoreInfoDTOS = accountConsumeSceneCustomizeMapper.findAllAccountConsumeSceneDTO(merCode);
    if (CollectionUtils.isNotEmpty(consumeStoreInfoDTOS)) {
      //按消费id分组
      consumeStoreInfoDTOS.stream()
              .collect(Collectors.groupingBy(AccountConsumeStoreInfoDTO::getId))
              .forEach((id, accountConsumeStoreInfoDTOS) -> {
                AccountConsumeSceneResp resp = new AccountConsumeSceneResp();
                resp.setId(String.valueOf(id));
                AccountConsumeStoreInfoDTO storeInfoDTO = accountConsumeStoreInfoDTOS.get(0);
                resp.setAccountTypeCode(storeInfoDTO.getAccountTypeCode());
                resp.setAccountTypeName(storeInfoDTO.getAccountTypeName());
                resp.setMerCode(storeInfoDTO.getMerCode());
                resp.setConsumeSceneSupplierDTOS(new ArrayList<>());
                //按供应商编码分组
                accountConsumeStoreInfoDTOS.stream()
                        .collect(Collectors.groupingBy(AccountConsumeStoreInfoDTO::getSupplierCode))
                        .forEach((supplierCode, accountConsumeStoreInfos) -> {
                          AccountConsumeSceneSupplierDTO consumeSceneSupplierDTO = new AccountConsumeSceneSupplierDTO();
                          consumeSceneSupplierDTO.setSupplierCode(supplierCode);
                          consumeSceneSupplierDTO.setSupplierName(accountConsumeStoreInfos.get(0).getSupplierName());
                          consumeSceneSupplierDTO.setConsumeStoreRelationInfos(new ArrayList<>());
                          // 封装具体的消费门店配置信息
                          accountConsumeStoreInfos.forEach(accountConsume -> {
                            AccountConsumeStoreRelationInfo storeRelationInfo = new AccountConsumeStoreRelationInfo();
                            BeanUtils.copyProperties(accountConsume, storeRelationInfo);
                            consumeSceneSupplierDTO.getConsumeStoreRelationInfos().add(storeRelationInfo);
                          });
                          resp.getConsumeSceneSupplierDTOS().add(consumeSceneSupplierDTO);
                        });
                resps.add(resp);
              });
    }
    return resps;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public Boolean edit(List<AccountConsumeSceneEditReq> consumeSceneEditReqs) {
    if (CollectionUtils.isEmpty(consumeSceneEditReqs)) {
      throw new BusiException("至少配置一个员工类型");
    }
    //删除已有配置
    String merCode = MerchantUserHolder.getMerchantUser().getMerchantCode();
    List<AccountConsumeScene> oldScenes = accountConsumeSceneDao.getAllByMercode(Lists.newArrayList(merCode));
    try {
      if (CollectionUtils.isNotEmpty(oldScenes)) {
        List<Long> oldConsumeSceneIds = oldScenes.stream()
                .map(AccountConsumeScene::getId).collect(Collectors.toList());
        accountConsumeSceneDao.deleteConsumeSceneByIds(oldConsumeSceneIds);
        accountConsumeSceneStoreRelationDao.deleteByConsumeSceneIds(oldConsumeSceneIds);
      }

      //保存最新配置
      List<AccountConsumeScene> newScenes = new ArrayList<>();
      Map<AccountConsumeSceneEditReq, AccountConsumeScene> sceneMap = new HashMap<>();
      consumeSceneEditReqs.forEach(consumeSceneEditReq -> {
        if (CollectionUtils.isNotEmpty(consumeSceneEditReq.getAccountConsumeStoreRelationEditReqs())) {
          AccountConsumeScene scene = new AccountConsumeScene();
          BeanUtils.copyProperties(consumeSceneEditReq, scene);
          scene.setId(null);
          newScenes.add(scene);
          sceneMap.put(consumeSceneEditReq, scene);
        }
      });
      accountConsumeSceneDao.saveBatch(newScenes);
      List<AccountConsumeSceneStoreRelation> newRelations = new ArrayList<>();
      consumeSceneEditReqs.forEach(consumeSceneEditReq -> {
        if (CollectionUtils.isNotEmpty(consumeSceneEditReq.getAccountConsumeStoreRelationEditReqs())) {
          consumeSceneEditReq.getAccountConsumeStoreRelationEditReqs()
                  .forEach(consumeStoreRelation -> {
                    AccountConsumeSceneStoreRelation relation = new AccountConsumeSceneStoreRelation();
                    BeanUtils.copyProperties(consumeStoreRelation,relation);
                    relation.setAccountConsumeSceneId(sceneMap.get(consumeSceneEditReq).getId());
                    newRelations.add(relation);
                  });
        }
      });
      accountConsumeSceneStoreRelationDao.saveBatch(newRelations);
      //保存账户变更记录
      List<MerchantAccountType> merchantAccountTypes = merchantAccountTypeDao.queryAllByMerCode(merCode);
      List<String> accountTypeCodes = merchantAccountTypes.stream().map(MerchantAccountType::getMerAccountTypeCode).collect(Collectors.toList());
      accountChangeEventRecordService.batchSaveByAccountTypeCode(accountTypeCodes, AccountChangeType.ACCOUNT_CONSUME_SCENE_EDIT);
      //消费门店配置推送给商户端
      List<AccountConsumeSceneStoreRelation> allRelations = accountConsumeSceneStoreRelationMapper.queryAllRelationList(merCode);
      applicationContext.publishEvent( AccountConsumeSceneEvt.builder().typeEnum(ShoppingActionTypeEnum.UPDATE).relationList(allRelations).build());
      return true;
    } finally {
      if (TransactionSynchronizationManager.isActualTransactionActive()) {
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
          @Override
          public void afterCompletion(int status) {
            if (CollectionUtils.isNotEmpty(oldScenes)) {
              String cacheName = "getAccountTypeAndMerCode";
              Cache cache = cacheManager.getCache(cacheName);
              if (cache != null) {
                oldScenes.forEach(scene -> {
                  String key = "#"+scene.getAccountTypeCode()+"#"+scene.getMerCode();
                  if (cache.evictIfPresent(key)) {
                    log.info("删除缓存，name:{} key:{}", cacheName, key);
                  } else {
                    log.info("没有缓存，name:{} key:{}", cacheName, key);
                  }
                });
              }
            }
          }
        });
      }
    }
  }
}