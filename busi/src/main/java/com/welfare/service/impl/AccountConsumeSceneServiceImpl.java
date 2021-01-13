package com.welfare.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.welfare.common.enums.ShoppingActionTypeEnum;
import com.welfare.persist.dao.AccountConsumeSceneDao;
import com.welfare.persist.dao.AccountConsumeSceneStoreRelationDao;
import com.welfare.persist.dto.AccountConsumeSceneMapperDTO;
import com.welfare.persist.dto.AccountConsumeScenePageDTO;
import com.welfare.persist.dto.query.AccountConsumePageQuery;
import com.welfare.persist.entity.AccountConsumeScene;
import com.welfare.persist.entity.AccountConsumeSceneStoreRelation;
import com.welfare.persist.mapper.AccountConsumeSceneCustomizeMapper;
import com.welfare.service.AccountConsumeSceneService;
import com.welfare.service.dto.AccountConsumeSceneAddReq;
import com.welfare.service.dto.AccountConsumeSceneDTO;
import com.welfare.service.dto.AccountConsumeSceneReq;
import com.welfare.service.dto.AccountConsumeSceneStoreRelationReq;
import com.welfare.service.remote.ShoppingFeignClient;
import com.welfare.service.remote.entity.RoleConsumptionResp;
import com.welfare.service.remote.entity.StoreBinding;
import com.welfare.service.remote.entity.UserRoleBinding;
import com.welfare.service.remote.entity.UserRoleBindingReqDTO;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

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
  private final ObjectMapper mapper;
  private final ShoppingFeignClient shoppingFeignClient;

  @Override
  public void syncAccountConsumeScene(ShoppingActionTypeEnum actionTypeEnum,
      Map<AccountConsumeScene,List<AccountConsumeSceneStoreRelation>> accountConsumeSceneMap) {

    if (accountConsumeSceneMap.isEmpty()) {
      return;
    }
    UserRoleBindingReqDTO userRoleBindingReqDTO = new UserRoleBindingReqDTO();
    userRoleBindingReqDTO.setActionType(actionTypeEnum);

    userRoleBindingReqDTO.setRequestId(UUID.randomUUID().toString());
    userRoleBindingReqDTO.setTimestamp(String.valueOf(new Date().getTime()));
    List<UserRoleBinding> userRoleBindingList = assemableUserRoleBindings(accountConsumeSceneMap);
    userRoleBindingReqDTO.setList(userRoleBindingList);

    // send after tx commit but is async
    TransactionSynchronizationManager.registerSynchronization(
        new TransactionSynchronizationAdapter() {
          @Override
          public void afterCommit() {
            try {
              log.info("批量添加、修改员工账号 addOrUpdateEmployer:{}",
                  mapper.writeValueAsString(userRoleBindingReqDTO));
              RoleConsumptionResp roleConsumptionResp = shoppingFeignClient
                  .addOrUpdateUserRoleBinding(userRoleBindingReqDTO);
              if (roleConsumptionResp.equals("200")) {
                // 写入
                List<AccountConsumeScene> accountConsumeSceneList = accountConsumeSceneMap.keySet().stream().collect(
                    Collectors.toList());
                
                for (AccountConsumeScene accountConsumeScene : accountConsumeSceneList) {
                  accountConsumeScene.setSyncStatus(1);
                }
                accountConsumeSceneDao.saveOrUpdateBatch(accountConsumeSceneList);
              }
            } catch (Exception e) {
              log.error("[afterCommit] call addOrUpdateEmployer error", e.getMessage());
            }
          }
        }
    );

  }

  private List<UserRoleBinding> assemableUserRoleBindings(Map<AccountConsumeScene,List<AccountConsumeSceneStoreRelation>> accountConsumeSceneMap) {
    List<UserRoleBinding> resultList = new LinkedList<UserRoleBinding>();
    Set<AccountConsumeScene> accountConsumeSceneSet = accountConsumeSceneMap.keySet();
    UserRoleBinding userRoleBinding = new UserRoleBinding();
    List<StoreBinding> bindings = new LinkedList<StoreBinding>() ;
    List<String> employeeRoles = new LinkedList<String>();
    accountConsumeSceneSet.forEach(accountConsumeScene -> {
      List<AccountConsumeSceneStoreRelation> relationList = accountConsumeSceneMap.get(accountConsumeScene);
      employeeRoles.add(accountConsumeScene.getAccountTypeCode());
      bindings.addAll(assemableStoreBindings(relationList));
      resultList.add(userRoleBinding);
    });


    return resultList;
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

  @Override
  public AccountConsumeScene getAccountConsumeScene(Long id) {
    return accountConsumeSceneDao.getById(id);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public Boolean save(AccountConsumeSceneAddReq accountConsumeSceneAddReq) {
    List<String> accountTypeCodeList = accountConsumeSceneAddReq.getAccountTypeCodeList();
    accountTypeCodeList.forEach(accountTypeCode -> {
      AccountConsumeScene accountConsumeScene = new AccountConsumeScene();
      BeanUtils.copyProperties(accountConsumeSceneAddReq, accountConsumeScene);
      accountConsumeScene.setAccountTypeCode(accountTypeCode);
      accountConsumeSceneDao.save(accountConsumeScene);
      List<AccountConsumeSceneStoreRelation> accountConsumeSceneStoreRelationList = getAccountConsumeSceneStoreRelations(
          accountConsumeSceneAddReq, accountConsumeScene);
      accountConsumeSceneStoreRelationDao.saveBatch(accountConsumeSceneStoreRelationList);
    });
    return true;
  }

  private List<AccountConsumeSceneStoreRelation> getAccountConsumeSceneStoreRelations(
      AccountConsumeSceneAddReq accountConsumeSceneAddReq,
      AccountConsumeScene accountConsumeScene) {
    List<AccountConsumeSceneStoreRelationReq> accountConsumeSceneStoreRelationReqList = accountConsumeSceneAddReq
        .getAccountConsumeSceneStoreRelationReqList();
    List<AccountConsumeSceneStoreRelation> accountConsumeSceneStoreRelationList = new ArrayList<>(
        accountConsumeSceneStoreRelationReqList.size());
    accountConsumeSceneStoreRelationReqList.forEach(accountConsumeSceneStoreRelationReq -> {
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
    accountConsumeSceneDao.updateById(accountConsumeScene);
    List<AccountConsumeSceneStoreRelation> accountConsumeSceneStoreRelationList = new ArrayList<>();
    accountConsumeSceneReq.getAccountConsumeSceneStoreRelationReqList().stream()
        .forEach(accountConsumeSceneStoreRelationReq -> {
          AccountConsumeSceneStoreRelation accountConsumeSceneStoreRelation = new AccountConsumeSceneStoreRelation();
          BeanUtils.copyProperties(accountConsumeSceneStoreRelationReq,
              accountConsumeSceneStoreRelation);
          accountConsumeSceneStoreRelationList.add(accountConsumeSceneStoreRelation);
        });
    accountConsumeSceneStoreRelationDao.updateBatchById(accountConsumeSceneStoreRelationList);
    return true;
  }


  @Override
  @Transactional(rollbackFor = Exception.class)
  public Boolean delete(Long id) {
    UpdateWrapper<AccountConsumeScene> updateWrapper = new UpdateWrapper();
    updateWrapper.eq(AccountConsumeScene.ID, id);
    AccountConsumeScene accountConsumeScene = new AccountConsumeScene();
    accountConsumeScene.setDeleted(true);
    return accountConsumeSceneDao.update(accountConsumeScene, updateWrapper);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public Boolean updateStatus(Long id, Integer status) {
    UpdateWrapper<AccountConsumeScene> updateWrapper = new UpdateWrapper();
    updateWrapper.eq(AccountConsumeScene.ID, id);
    AccountConsumeScene accountConsumeScene = new AccountConsumeScene();
    accountConsumeScene.setStatus(status);
    return accountConsumeSceneDao.update(accountConsumeScene, updateWrapper);
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
}