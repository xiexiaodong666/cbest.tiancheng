package com.welfare.service.sync.handler;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.google.gson.Gson;
import com.welfare.common.constants.AccountConsumeSceneStatus;
import com.welfare.common.enums.ShoppingActionTypeEnum;
import com.welfare.common.exception.BusiException;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
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
  @Autowired
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
    List<AccountConsumeSceneStoreRelation> relationList = accountConsumeSceneEvt.getRelationList();
    if (CollectionUtils.isEmpty(relationList)) {
      return;
    }
    UserRoleBindingReqDTO userRoleBindingReqDTO = new UserRoleBindingReqDTO();
    //这边没有删除 删除和修改传同样的type bindings 为空
    userRoleBindingReqDTO.setActionType(
        actionTypeEnum.getCode().equals(ShoppingActionTypeEnum.DELETE.getCode())
            ? ShoppingActionTypeEnum.UPDATE : actionTypeEnum);
    userRoleBindingReqDTO.setRequestId(UUID.randomUUID().toString());
    userRoleBindingReqDTO.setTimestamp(new Date());
    List<UserRoleBinding> userRoleBindingList = assemableUserRoleBindings(relationList,
        actionTypeEnum);
    userRoleBindingReqDTO.setList(userRoleBindingList);

    log.info("同步员工类型数据 userRoleBindingReqDTO:{}",
        gson.toJson(userRoleBindingReqDTO));
    RoleConsumptionResp roleConsumptionResp = shoppingFeignClient
        .addOrUpdateUserRoleBinding(userRoleBindingReqDTO);
    if (!("0000").equals(roleConsumptionResp.getCode())) {
      throw new BusiException("同步员工类型数据到商城中心失败msg【" + roleConsumptionResp.getMsg() + "】");
    }
  }

  private List<UserRoleBinding> assemableUserRoleBindings(
      List<AccountConsumeSceneStoreRelation> relationList,
      ShoppingActionTypeEnum shoppingActionTypeEnum) {
    if (CollectionUtils.isEmpty(relationList)) {
      return null;
    }
    UserRoleBinding userRoleBinding = new UserRoleBinding();
    List<String> employeeRoles = assemableEmployeeRoles(relationList);
    userRoleBinding.setEmployeeRoles(employeeRoles);
    List<StoreBinding> bindings = assemableBindings(relationList, shoppingActionTypeEnum);
    userRoleBinding.setBindings(bindings);
    AccountConsumeScene accountConsumeScene = accountConsumeSceneCustomizeMapper
        .queryById(relationList.get(0).getAccountConsumeSceneId());
    userRoleBinding.setMerchantCode(accountConsumeScene.getMerCode());
    userRoleBinding
        .setEnabled(accountConsumeScene.getStatus() == AccountConsumeSceneStatus.ENABLE.getCode());
    return Arrays.asList(userRoleBinding);
  }

  private List<String> assemableEmployeeRoles(List<AccountConsumeSceneStoreRelation> relationList){
    HashMap<String, String> employeeRoles = new HashMap<String, String>();
    for (AccountConsumeSceneStoreRelation accountConsumeSceneStoreRelation : relationList) {
      Long sceneId = accountConsumeSceneStoreRelation.getAccountConsumeSceneId();
      AccountConsumeScene accountConsumeScene = accountConsumeSceneCustomizeMapper
          .queryById(sceneId);
      employeeRoles.put(accountConsumeScene.getAccountTypeCode(),
          accountConsumeScene.getAccountTypeCode());
    }
    return new ArrayList<>(employeeRoles.keySet());
  }

  private List<StoreBinding> assemableBindings(List<AccountConsumeSceneStoreRelation> relationList,
      ShoppingActionTypeEnum shoppingActionTypeEnum) {
    List<StoreBinding> bindings = new LinkedList<>();
    if (!shoppingActionTypeEnum.getCode().equals(ShoppingActionTypeEnum.DELETE.getCode())) {
      for (AccountConsumeSceneStoreRelation accountConsumeSceneStoreRelation : relationList) {
        StoreBinding storeBinding = new StoreBinding();
        String[] array = accountConsumeSceneStoreRelation.getSceneConsumType().split(",");
        storeBinding.setConsumeTypes(Arrays.asList(array));
        storeBinding.setStoreCode(accountConsumeSceneStoreRelation.getStoreCode());
        bindings.add(storeBinding);
      }
    }
    return bindings;
  }

  private List<StoreBinding> assemableStoreBindings(
      List<AccountConsumeSceneStoreRelation> relationList) {
    if (CollectionUtils.isEmpty(relationList)) {
      return null;
    }
    List<StoreBinding> storeBindingList = new LinkedList<StoreBinding>();
    relationList.forEach(accountConsumeSceneStoreRelation -> {
      StoreBinding storeBinding = new StoreBinding();
      storeBinding.setStoreCode(accountConsumeSceneStoreRelation.getStoreCode());
      String[] types = accountConsumeSceneStoreRelation.getSceneConsumType().split(",");
      storeBinding.setConsumeTypes(Arrays.asList(types));
      storeBindingList.add(storeBinding);
    });
    return storeBindingList;
  }
}
