package com.welfare.servicemerchant.service.sync.handler;

import com.alibaba.fastjson.JSON;
import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.google.gson.Gson;
import com.welfare.common.constants.AccountConsumeSceneStatus;
import com.welfare.common.enums.ShoppingActionTypeEnum;
import com.welfare.common.exception.BizException;
import com.welfare.persist.dao.AccountConsumeSceneDao;
import com.welfare.persist.entity.AccountConsumeScene;
import com.welfare.persist.entity.AccountConsumeSceneStoreRelation;
import com.welfare.persist.mapper.AccountConsumeSceneCustomizeMapper;
import com.welfare.service.remote.ShoppingFeignClient;
import com.welfare.service.remote.entity.RoleConsumptionResp;
import com.welfare.service.remote.entity.StoreBinding;
import com.welfare.service.remote.entity.UserRoleBinding;
import com.welfare.service.remote.entity.UserRoleBindingReqDTO;
import com.welfare.service.sync.event.AccountConsumeSceneEvt;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.killbill.bus.api.PersistentBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author yaoxiao
 * @version 0.0.1
 * @date 2021/1/15 15:25
 */
@Component
@Slf4j
public class AccountConsumeSceneHandler {

  @Autowired
  PersistentBus persistentBus;
  @Autowired(required = false)
  ShoppingFeignClient shoppingFeignClient;

  private Gson gson = new Gson();
  @Autowired
  private AccountConsumeSceneDao accountConsumeSceneDao;
  @Autowired
  private AccountConsumeSceneCustomizeMapper accountConsumeSceneCustomizeMapper;


  @PostConstruct
  private void register() {
    try {
      persistentBus.register(this);
    } catch (PersistentBus.EventBusException e) {
      log.error(e.getMessage(), e);
    }
  }

  @AllowConcurrentEvents
  @Subscribe
  public void accountConsumeSceneEvt(AccountConsumeSceneEvt accountConsumeSceneEvt) {

    ShoppingActionTypeEnum actionTypeEnum = accountConsumeSceneEvt.getTypeEnum();
    UserRoleBindingReqDTO userRoleBindingReqDTO = new UserRoleBindingReqDTO();
    userRoleBindingReqDTO.setRequestId(UUID.randomUUID().toString());
    userRoleBindingReqDTO.setTimestamp(new Date());
    List<UserRoleBinding> userRoleBindingList = assemableUserRoleBindings(actionTypeEnum,accountConsumeSceneEvt);
    //这边没有删除 删除和修改传同样的type bindings 为空
    userRoleBindingReqDTO.setActionType(
        actionTypeEnum.getCode().equals(ShoppingActionTypeEnum.DELETE.getCode()) || actionTypeEnum.getCode().equals(ShoppingActionTypeEnum.ACCOUNT_CONSUME_SCENE_BATCH_DELETE.getCode())
            ? ShoppingActionTypeEnum.UPDATE : actionTypeEnum);
    userRoleBindingReqDTO.setList(userRoleBindingList);

    log.info("同步员工类型数据 userRoleBindingReqDTO:{}",
        gson.toJson(userRoleBindingReqDTO));
    RoleConsumptionResp roleConsumptionResp = shoppingFeignClient
        .addOrUpdateUserRoleBinding(userRoleBindingReqDTO);
    log.info("同步员工类型数据，resp【{}】req【{}】", JSON.toJSONString(roleConsumptionResp), JSON.toJSONString(userRoleBindingReqDTO));

    if (!("0000").equals(roleConsumptionResp.getCode())) {
      throw new BizException("同步员工类型数据到商城中心失败msg【" + roleConsumptionResp.getMsg() + "】");
    }
  }

  private List<UserRoleBinding> assemableUserRoleBindings(ShoppingActionTypeEnum shoppingActionTypeEnum,AccountConsumeSceneEvt accountConsumeSceneEvt) {
    List<UserRoleBinding> userRoleBindingList = new LinkedList<UserRoleBinding>();

    List<AccountConsumeScene> accountConsumeSceneList = null;

    //获取对应的的accountConsumeSceneList
    if (shoppingActionTypeEnum.getCode()
        .equals(ShoppingActionTypeEnum.ACCOUNT_CONSUME_SCENE_BATCH_DELETE.getCode())){
      accountConsumeSceneList = accountConsumeSceneCustomizeMapper
          .queryDeleteScene(accountConsumeSceneEvt.getMerCode());
    }else{
      List<Long> sceneIdList = accountConsumeSceneEvt.getRelationList().stream().map(accountConsumeSceneStoreRelation -> {
        return accountConsumeSceneStoreRelation.getAccountConsumeSceneId();
      }).collect(Collectors.toList());
      accountConsumeSceneList = accountConsumeSceneCustomizeMapper.queryByIdList(sceneIdList);
    }

    //封装map key accountConsumeSceneId value List<AccountConsumeSceneStoreRelation>
    Map<Long,List<AccountConsumeSceneStoreRelation>> sceneRelationMap = new HashMap<Long,List<AccountConsumeSceneStoreRelation>>();
    accountConsumeSceneEvt.getRelationList().forEach(accountConsumeSceneStoreRelation -> {
      List<AccountConsumeSceneStoreRelation> relations = sceneRelationMap.get(accountConsumeSceneStoreRelation.getAccountConsumeSceneId());
      if(CollectionUtils.isEmpty(relations)){
        relations = new LinkedList<AccountConsumeSceneStoreRelation>();
        relations.add(accountConsumeSceneStoreRelation);
        sceneRelationMap.put(accountConsumeSceneStoreRelation.getAccountConsumeSceneId(),relations);
      }else{
        relations.add(accountConsumeSceneStoreRelation);
        sceneRelationMap.put(accountConsumeSceneStoreRelation.getAccountConsumeSceneId(),relations);
      }
    });
    //封装userRoleBindingList
    accountConsumeSceneList.forEach( accountConsumeScene -> {
      UserRoleBinding userRoleBinding = new UserRoleBinding();
      userRoleBinding.setEmployeeRole(accountConsumeScene.getAccountTypeCode());
      userRoleBinding.setEnabled(accountConsumeScene.getStatus() == AccountConsumeSceneStatus.ENABLE.getCode());
      userRoleBinding.setMerchantCode(accountConsumeScene.getMerCode());
      if(!sceneRelationMap.isEmpty()){
        List<StoreBinding> bindings = assemableBindings(sceneRelationMap.get(accountConsumeScene.getId()));
        userRoleBinding.setBindings(bindings);
      }
      userRoleBindingList.add(userRoleBinding);
    });

    return userRoleBindingList;
  }


  private List<StoreBinding> assemableBindings(List<AccountConsumeSceneStoreRelation> relationList) {
    if( relationList == null ){
      return null;
    }
    List<StoreBinding> bindings = new LinkedList<>();
    for (AccountConsumeSceneStoreRelation accountConsumeSceneStoreRelation : relationList) {
      if(StringUtils.isBlank(accountConsumeSceneStoreRelation.getSceneConsumType())){
        //员工类型在该门店的唯一消费方式被删除(商户门店修改了该门店的消费类型)
        continue;
      }
      StoreBinding storeBinding = new StoreBinding();
      String[] array = accountConsumeSceneStoreRelation.getSceneConsumType().split(",");
      storeBinding.setConsumeTypes(Arrays.asList(array));
      storeBinding.setStoreCode(accountConsumeSceneStoreRelation.getStoreCode());
      bindings.add(storeBinding);
    }
    return bindings;
  }
}
