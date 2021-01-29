package com.welfare.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.welfare.common.exception.BusiException;
import com.welfare.common.exception.ExceptionCode;
import com.welfare.persist.dao.AccountConsumeSceneStoreRelationDao;
import com.welfare.persist.entity.AccountConsumeSceneStoreRelation;
import com.welfare.persist.mapper.AccountConsumeSceneStoreRelationMapper;
import com.welfare.persist.mapper.AccountCustomizeMapper;
import com.welfare.service.AccountChangeEventRecordService;
import com.welfare.service.AccountConsumeSceneStoreRelationService;
import com.welfare.service.dto.ConsumeTypeJson;
import com.welfare.service.dto.StoreConsumeRelationDTO;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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
  private final AccountChangeEventRecordService accountChangeEventRecordService;

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
    if( CollectionUtils.isEmpty(accountConsumeSceneStoreRelations) ){
      return;
    }
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
        StringBuilder text = getText(selectType);
        throw  new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS,"员工类型消费场景ID:"+accountConsumeSceneStoreRelation.getAccountConsumeSceneId()+"配置了"+text.toString()+"消费方式",null);
      }
      if( !sb.toString().equals(accountConsumeSceneStoreRelation.getSceneConsumType())){
        accountConsumeSceneStoreRelation.setSceneConsumType(sb.toString());
        //账号类型发生了改变
        updateList.add(accountConsumeSceneStoreRelation);
      }
    });
    boolean updateResult = accountConsumeSceneStoreRelationDao.saveOrUpdateBatch(updateList);
    if( updateResult ){
      accountChangeEventRecordService.batchSaveBySceneStoreRelation(updateList);
    }
    return;
  }
//
  private StringBuilder getRelationUpdateResult(String[] selectType,AccountConsumeSceneStoreRelation accountConsumeSceneStoreRelation,
      String consumeType){
    StringBuilder sb = new StringBuilder();
    Gson gson = new Gson();
    ConsumeTypeJson consumeTypeJson = gson.fromJson(consumeType,ConsumeTypeJson.class);
    for( int i =0 ; i <selectType.length; i ++  ){
      String type = selectType[i];
      if( consumeTypeJson.getType(type) ){
        if( i == 0  ){
          sb.append(type);
        }else{
          sb.append(",").append(type);
        }
      }
    }
    return sb;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void updateStoreConsumeType( String merCode,String storeCode, String consumeType) {
    List<AccountConsumeSceneStoreRelation> accountConsumeSceneStoreRelations = this.getListByStoreCode(merCode,storeCode);
    if(CollectionUtils.isEmpty(accountConsumeSceneStoreRelations)){
      return;
    }
    Gson gson = new Gson();
    ConsumeTypeJson consumeTypeJson = gson.fromJson(consumeType,ConsumeTypeJson.class);
    List<AccountConsumeSceneStoreRelation> updateList = new LinkedList<AccountConsumeSceneStoreRelation>();
    accountConsumeSceneStoreRelations.forEach(accountConsumeSceneStoreRelation -> {
      String[] selectType = accountConsumeSceneStoreRelation.getSceneConsumType().split(",");
      StringBuilder sb = new StringBuilder();
      for( int i =0 ; i <selectType.length; i ++  ){
        String type = selectType[i];
        if( consumeTypeJson.getType(type) ){
          if( i == 0  ){
            sb.append(type);
          }else{
            sb.append(",").append(type);
          }
        }
      }
      //return consumeTypeJson.getValue(s);
      if(StringUtils.isBlank(sb)){
        StringBuilder text = getText(selectType);
        throw  new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS,"员工类型消费场景ID:"+accountConsumeSceneStoreRelation.getAccountConsumeSceneId()+"配置了"+text.toString()+"消费方式",null);
        //throw  new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS,"员工类型:"++"消费场景配置了对应得消费方式",null);
      }
      if( !sb.toString().equals(accountConsumeSceneStoreRelation.getSceneConsumType())){
        accountConsumeSceneStoreRelation.setSceneConsumType(sb.toString());
        //账号类型发生了改变
        updateList.add(accountConsumeSceneStoreRelation);
      }
    });

    boolean updateResult = accountConsumeSceneStoreRelationDao.saveOrUpdateBatch(updateList);
    if( updateResult ){
      accountChangeEventRecordService.batchSaveBySceneStoreRelation(updateList);
    }
    return;
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
