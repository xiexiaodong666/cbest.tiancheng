package com.welfare.service.sync.handler;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.google.gson.Gson;
import com.welfare.common.enums.ShoppingActionTypeEnum;
import com.welfare.common.exception.BusiException;
import com.welfare.persist.dao.AccountConsumeSceneDao;
import com.welfare.persist.entity.AccountConsumeScene;
import com.welfare.persist.entity.AccountConsumeSceneStoreRelation;
import com.welfare.service.dto.AccountConsumeSceneDTO;
import com.welfare.service.remote.ShoppingFeignClient;
import com.welfare.service.remote.entity.RoleConsumptionResp;
import com.welfare.service.remote.entity.StoreBinding;
import com.welfare.service.remote.entity.UserRoleBinding;
import com.welfare.service.remote.entity.UserRoleBindingReqDTO;
import com.welfare.service.sync.event.AccountConsumeSceneEvt;
import com.welfare.service.sync.event.AccountEvt;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

  private Gson gson= new Gson();
  @Autowired
  private AccountConsumeSceneDao accountConsumeSceneDao;


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
    List<AccountConsumeSceneStoreRelation> relationList =  accountConsumeSceneEvt.getRelationList();
    if (CollectionUtils.isEmpty(relationList)) {
      return;
    }
    UserRoleBindingReqDTO userRoleBindingReqDTO = new UserRoleBindingReqDTO();
    userRoleBindingReqDTO.setActionType(actionTypeEnum);
    userRoleBindingReqDTO.setRequestId(UUID.randomUUID().toString());
    userRoleBindingReqDTO.setTimestamp(new Date());
    List<UserRoleBinding> userRoleBindingList = assemableUserRoleBindings(relationList);
    userRoleBindingReqDTO.setList(userRoleBindingList);

    log.info("批量添加、修改员工账号 addOrUpdateEmployer:{}",
        gson.toJson(userRoleBindingReqDTO));
    RoleConsumptionResp roleConsumptionResp = shoppingFeignClient
        .addOrUpdateUserRoleBinding(userRoleBindingReqDTO);
    if (!("0000").equals(roleConsumptionResp.getCode())) {
      throw new BusiException("同步员工类型数据到商城中心失败msg【" + roleConsumptionResp.getMsg() + "】");
    }
  }

  private List<UserRoleBinding> assemableUserRoleBindings(List<AccountConsumeSceneStoreRelation> relationList) {
    UserRoleBinding userRoleBinding = new UserRoleBinding();

    List<StoreBinding> bindings = new LinkedList<>();
    List<String> employeeRoles = new LinkedList<>();
    AccountConsumeScene accountConsumeScene =null;
    for( AccountConsumeSceneStoreRelation accountConsumeSceneStoreRelation :  relationList){
      Long sceneId = accountConsumeSceneStoreRelation.getAccountConsumeSceneId();
      accountConsumeScene = accountConsumeSceneDao.getById(sceneId);
      employeeRoles.add(accountConsumeScene.getAccountTypeCode());
      StoreBinding storeBinding = new StoreBinding();
      String[] array = accountConsumeSceneStoreRelation.getSceneConsumType().split(",");
      storeBinding.setConsumeTypes(Arrays.asList(array));
      storeBinding.setStoreCode(accountConsumeSceneStoreRelation.getStoreCode());
      bindings.add(storeBinding);
    }
    userRoleBinding.setEmployeeRoles(employeeRoles);
    userRoleBinding.setBindings(bindings);
    userRoleBinding.setMerchantCode(accountConsumeScene.getMerCode());
    userRoleBinding.setEnabled(accountConsumeScene.getStatus() == 0 ? true : false);
    return Arrays.asList(userRoleBinding);
  }

  private List<StoreBinding> assemableStoreBindings(List<AccountConsumeSceneStoreRelation> relationList){
    if( CollectionUtils.isEmpty(relationList)){
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
