package com.welfare.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.welfare.common.constants.AccountChangeType;
import com.welfare.common.enums.ShoppingActionTypeEnum;
import com.welfare.common.exception.BusiException;
import com.welfare.common.exception.ExceptionCode;
import com.welfare.persist.dao.AccountConsumeSceneDao;
import com.welfare.persist.dao.AccountConsumeSceneStoreRelationDao;
import com.welfare.persist.entity.Account;
import com.welfare.persist.entity.AccountChangeEventRecord;
import com.welfare.persist.entity.AccountConsumeScene;
import com.welfare.persist.entity.AccountConsumeSceneStoreRelation;
import com.welfare.persist.mapper.AccountConsumeSceneCustomizeMapper;
import com.welfare.persist.mapper.AccountConsumeSceneStoreRelationMapper;
import com.welfare.persist.mapper.AccountCustomizeMapper;
import com.welfare.service.AccountChangeEventRecordService;
import com.welfare.service.AccountConsumeSceneStoreRelationService;
import com.welfare.service.dto.ConsumeTypeJson;
import com.welfare.service.dto.StoreConsumeRelationDTO;
import com.welfare.service.sync.event.AccountConsumeSceneEvt;
import com.welfare.service.utils.AccountUtils;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

/**
 * @author yaoxiao
 * @version 0.0.1
 * @date 2021/1/13 17:02
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class AccountConsumeSceneStoreRelationServiceImpl implements
    AccountConsumeSceneStoreRelationService {

  private final AccountConsumeSceneStoreRelationDao accountConsumeSceneStoreRelationDao;
  private final AccountCustomizeMapper accountCustomizeMapper;
  private final AccountConsumeSceneStoreRelationMapper accountConsumeSceneStoreRelationMapper;
  @Autowired
  private AccountChangeEventRecordService accountChangeEventRecordService;
  private final ApplicationContext applicationContext;
  private final AccountConsumeSceneCustomizeMapper accountConsumeSceneCustomizeMapper;
  private final AccountConsumeSceneDao accountConsumeSceneDao;

  @Override
  public List<AccountConsumeSceneStoreRelation> getListByConsumeSceneId(Long accountConsumeSceneId){
    QueryWrapper<AccountConsumeSceneStoreRelation> wrapper = new QueryWrapper();
    wrapper.eq(AccountConsumeSceneStoreRelation.ACCOUNT_CONSUME_SCENE_ID,accountConsumeSceneId);
    return accountConsumeSceneStoreRelationDao.list(wrapper);
  }
  public List<AccountConsumeSceneStoreRelation> getListByStoreCode(String merCode,String storeCode){
    return accountConsumeSceneStoreRelationMapper.queryRelationDetail(merCode,storeCode);
  }
  public List<AccountConsumeSceneStoreRelation> getRelationList(String merCode,List<String> storeCodeList){
    return accountConsumeSceneStoreRelationMapper.queryRelationList(merCode,storeCodeList);
  }

  @Transactional(rollbackFor = Exception.class)
  @Override
  public void deleteConsumeScene(String merCode,List<String> storeCodeList){
    log.info("商户门店删除,变更员工类型消费场景 merCode:{},storeCodeList:{}",merCode,storeCodeList);
    //要变更的账号信息
    List<Account> accounts = accountCustomizeMapper.getUpdateAccountByMerCode(merCode,storeCodeList);
    List<AccountChangeEventRecord> recordList = AccountUtils
        .getEventList(accounts, AccountChangeType.ACCOUNT_CONSUME_SCENE_CONSUMETYPE_CHANGE);
    accountChangeEventRecordService.batchSave(recordList, AccountChangeType.ACCOUNT_CONSUME_SCENE_CONSUMETYPE_CHANGE);

    //删除 AccountConsumeStoreRelation
    List<AccountConsumeSceneStoreRelation> relationList = accountConsumeSceneStoreRelationMapper
        .queryDeleteRelationScene(merCode,storeCodeList);
    accountConsumeSceneStoreRelationDao.removeByIds(relationList.stream().map(accountConsumeSceneStoreRelation -> {
      return accountConsumeSceneStoreRelation.getId();
    }).collect(Collectors.toList()));

    //删除 AccountConsumeScene
    List<Long> deleteConsumeIdList = accountConsumeSceneCustomizeMapper.queryDeleteConsumeIdList(merCode);
    if(CollectionUtils.isNotEmpty(deleteConsumeIdList)){
      accountConsumeSceneDao.removeByIds(deleteConsumeIdList);
    }

    List<AccountConsumeSceneStoreRelation> allRelations = accountConsumeSceneStoreRelationMapper.queryAllRelationList(merCode);
    //下发数据
    applicationContext.publishEvent( AccountConsumeSceneEvt
        .builder().typeEnum(ShoppingActionTypeEnum.ACCOUNT_CONSUME_SCENE_BATCH_DELETE).relationList(allRelations).merCode(merCode).build());
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void updateStoreConsumeTypeByDTOList(String merCode,List<StoreConsumeRelationDTO> relationDTOList) {
    log.info("updateStoreConsumeType merCode:{},relationDTOList:{}",merCode,relationDTOList);
    if (StringUtils.isBlank(merCode) || CollectionUtils.isEmpty(relationDTOList)) {
      return;
    }
    List<String> storeCodeList = relationDTOList.stream().map(storeConsumeRelationDTO -> {
      return storeConsumeRelationDTO.getStoreCode();
    }).collect(Collectors.toList());
    List<AccountConsumeSceneStoreRelation> accountConsumeSceneStoreRelations = getRelationList(merCode,storeCodeList);

    HashMap<String,String> updateRelationMap = new HashMap<String,String>();
    relationDTOList.forEach(storeConsumeRelationDTO -> {
      updateRelationMap.put(storeConsumeRelationDTO.getStoreCode(),storeConsumeRelationDTO.getConsumeType());
    });

    List<AccountConsumeSceneStoreRelation> updateList = new LinkedList<AccountConsumeSceneStoreRelation>();
    accountConsumeSceneStoreRelations.forEach(accountConsumeSceneStoreRelation -> {
      //原员工类型配置消费方式
      String[] selectType = accountConsumeSceneStoreRelation.getSceneConsumType().split(",");
      //要修改的消费配置方式
      String consumeType = updateRelationMap.get(accountConsumeSceneStoreRelation.getStoreCode());
      //修改结果
      StringBuilder sb = getRelationUpdateResult(selectType,accountConsumeSceneStoreRelation,consumeType);
      //return consumeTypeJson.getValue(s);
      if(StringUtils.isBlank(sb)){
        //需要商户门店消费场景该表  员工唯一的消费场景被删除，relation关系删除
        log.info("员工类型所剩唯一的消费场景被修改 merCode:{},storeCode:{},consumeType:{},relationId:{}",
            merCode,accountConsumeSceneStoreRelation.getStoreCode(),consumeType,accountConsumeSceneStoreRelation.getAccountConsumeSceneId());
        accountConsumeSceneStoreRelation.setSceneConsumType("");
        accountConsumeSceneStoreRelation.setDeleted(true);
        updateList.add(accountConsumeSceneStoreRelation);
      }
      if( !sb.toString().equals(accountConsumeSceneStoreRelation.getSceneConsumType())){
        accountConsumeSceneStoreRelation.setSceneConsumType(sb.toString());
        //账号类型发生了改变
        updateList.add(accountConsumeSceneStoreRelation);
      }

    });
    boolean updateResult = accountConsumeSceneStoreRelationDao.saveOrUpdateBatch(updateList);
    if( updateResult){
      //数据变更
      accountChangeEventRecordService.batchSaveBySceneStoreRelation(updateList);
      List<Long> deleteIdList = updateList.stream().map(accountConsumeSceneStoreRelation -> {
        if( StringUtils.isBlank(accountConsumeSceneStoreRelation.getSceneConsumType()) ){
          return accountConsumeSceneStoreRelation.getId();
        }else{
          return null;
        }
      }).collect(Collectors.toList());
      accountConsumeSceneStoreRelationDao.removeByIds(deleteIdList);
      //批量下发数据传的是全量 不支持增量
      List<AccountConsumeSceneStoreRelation> allRelations = accountConsumeSceneStoreRelationMapper.queryAllRelationList(merCode);
      applicationContext.publishEvent( AccountConsumeSceneEvt
          .builder().typeEnum(ShoppingActionTypeEnum.UPDATE).relationList(allRelations).build());
    }
    return;
  }
//
  private StringBuilder getRelationUpdateResult(String[] selectType,AccountConsumeSceneStoreRelation accountConsumeSceneStoreRelation,
      String consumeType){
    StringBuilder sb = new StringBuilder();
    Gson gson = new Gson();
    ConsumeTypeJson consumeTypeJson = gson.fromJson(consumeType,ConsumeTypeJson.class);
    for( int i =0 ,t = 0; i <selectType.length; i ++  ){
      String type = selectType[i];
      if( consumeTypeJson.getType(type) ){
        if( t == 0  ){
          t++;
          sb.append(type);
        }else{
          sb.append(",").append(type);
        }
      }
    }
    return sb;
  }

  /**
   * 获取异常提示信息
   * @param selectType
   * @return
   */
  private StringBuilder getText( String[] selectType) {
    List<String> typeValue = Arrays.asList(selectType);
    StringBuilder text = new StringBuilder();
    typeValue.forEach(t ->{
      text.append(t );
    });
    return text;
  }


}
