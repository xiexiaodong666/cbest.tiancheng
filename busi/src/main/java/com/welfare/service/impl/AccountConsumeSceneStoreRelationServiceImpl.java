package com.welfare.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.welfare.persist.dao.AccountConsumeSceneStoreRelationDao;
import com.welfare.persist.entity.AccountConsumeSceneStoreRelation;
import com.welfare.persist.mapper.AccountConsumeSceneMapper;
import com.welfare.service.AccountConsumeSceneStoreRelationService;
import com.welfare.service.dto.ConsumeTypeJson;
import java.util.LinkedList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

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
  private final AccountConsumeSceneMapper accountConsumeSceneMapper;

  @Override
  public List<AccountConsumeSceneStoreRelation> getListByConsumeSceneId(Long accountConsumeSceneId){
    QueryWrapper<AccountConsumeSceneStoreRelation> wrapper = new QueryWrapper();
    wrapper.eq(AccountConsumeSceneStoreRelation.ACCOUNT_CONSUME_SCENE_ID,accountConsumeSceneId);
    return accountConsumeSceneStoreRelationDao.list(wrapper);
  }
  public List<AccountConsumeSceneStoreRelation> getListByStoreCode(String storeCode){
    QueryWrapper<AccountConsumeSceneStoreRelation> wrapper = new QueryWrapper();
    wrapper.eq(AccountConsumeSceneStoreRelation.STORE_CODE,storeCode);
    return accountConsumeSceneStoreRelationDao.list(wrapper);
  }

  @Override
  public void updateStoreConsumeType( String storeCode, String consumeType) {
    List<AccountConsumeSceneStoreRelation> accountConsumeSceneStoreRelations = this.getListByStoreCode(storeCode);
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
      if( !sb.toString().equals(accountConsumeSceneStoreRelation.getSceneConsumType())){
        //账号类型发生了改变
        updateList.add(accountConsumeSceneStoreRelation);
      }
    });
    //TODO 修改了选择类型  账户变更表增加记录
    //updateList
    return;
  }


}
