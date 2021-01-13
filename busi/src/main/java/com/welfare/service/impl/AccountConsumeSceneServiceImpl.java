package com.welfare.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.welfare.common.enums.ShoppingActionTypeEnum;
import com.welfare.common.exception.BusiException;
import com.welfare.common.exception.ExceptionCode;
import com.welfare.persist.dao.AccountConsumeSceneDao;
import com.welfare.persist.dao.AccountConsumeSceneStoreRelationDao;
import com.welfare.persist.dto.AccountConsumeSceneMapperDTO;
import com.welfare.persist.dto.AccountConsumeScenePageDTO;
import com.welfare.persist.dto.query.AccountConsumePageQuery;
import com.welfare.persist.entity.AccountConsumeScene;
import com.welfare.persist.entity.AccountConsumeSceneStoreRelation;
import com.welfare.persist.entity.AccountType;
import com.welfare.persist.entity.Merchant;
import com.welfare.persist.mapper.AccountConsumeSceneCustomizeMapper;
import com.welfare.service.AccountConsumeSceneService;
import com.welfare.service.AccountConsumeSceneStoreRelationService;
import com.welfare.service.AccountTypeService;
import com.welfare.service.MerchantService;
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
import java.util.HashMap;
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
  private final MerchantService merchantService;
  private final AccountTypeService accountTypeService;
  private final AccountConsumeSceneStoreRelationService accountConsumeSceneStoreRelationList;

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

    accountConsumeSceneSet.forEach(accountConsumeScene -> {
      List<StoreBinding> bindings = new LinkedList<StoreBinding>() ;
      List<String> employeeRoles = new LinkedList<String>();
      employeeRoles.add(accountConsumeScene.getAccountTypeCode());
      bindings.addAll(assemableStoreBindings(accountConsumeSceneMap.get(accountConsumeScene)));

      if( userRoleBinding.getBindings() == null  ){
        userRoleBinding.setBindings(bindings);
      }else{
        userRoleBinding.getBindings().addAll(bindings);
      }
      if( userRoleBinding.getEmployeeRoles() == null  ){
        userRoleBinding.setEmployeeRoles(employeeRoles);
      }else{
        userRoleBinding.getEmployeeRoles().addAll(employeeRoles);
      }
      userRoleBinding.setEnabled(accountConsumeScene.getStatus() == 0 ? true : false);
      userRoleBinding.setMerchantCode(accountConsumeScene.getMerCode());
    });
    resultList.add(userRoleBinding);

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
    Map<AccountConsumeScene,List<AccountConsumeSceneStoreRelation>> accountConsumeSceneMap = new HashMap<>();
    List<String> accountTypeCodeList = accountConsumeSceneAddReq.getAccountTypeCodeList();
    accountTypeCodeList.forEach(accountTypeCode -> {
      AccountConsumeScene accountConsumeScene = new AccountConsumeScene();
      BeanUtils.copyProperties(accountConsumeSceneAddReq, accountConsumeScene);
      accountConsumeScene.setAccountTypeCode(accountTypeCode);
      accountConsumeScene.setStatus(0);
      validationAccountConsumeScene(accountConsumeScene,true);
      accountConsumeSceneDao.save(accountConsumeScene);
      List<AccountConsumeSceneStoreRelation> accountConsumeSceneStoreRelationList = getAccountConsumeSceneStoreRelations(
          accountConsumeSceneAddReq, accountConsumeScene);
      accountConsumeSceneStoreRelationDao.saveBatch(accountConsumeSceneStoreRelationList);
      //下发数据Map
      accountConsumeSceneMap.put(accountConsumeScene,accountConsumeSceneStoreRelationList);
    });
    //下发数据
    syncAccountConsumeScene(ShoppingActionTypeEnum.ADD,
        accountConsumeSceneMap);
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
    if( isNew ){
      AccountConsumeScene queryAccountConsumeScene = queryAccountConsumeScene(accountConsumeScene.getMerCode(),accountConsumeScene.getAccountTypeCode());
      if(null != queryAccountConsumeScene){
        throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS,"该商户已经存在相同类型的消费场景配置",null);
      }
    }else{
      AccountConsumeScene queryAccountConsumeScene = accountConsumeSceneDao.getById(accountConsumeScene.getId());
      if( null == queryAccountConsumeScene ){
        throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS,"员工类型消费场景不存在",null);
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
    Map<AccountConsumeScene,List<AccountConsumeSceneStoreRelation>> accountConsumeSceneMap = new HashMap<>();
    AccountConsumeScene accountConsumeScene = new AccountConsumeScene();
    BeanUtils.copyProperties(accountConsumeSceneReq, accountConsumeScene);
    validationAccountConsumeScene(accountConsumeScene,false);
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
    //下发数据
    accountConsumeSceneMap.put(accountConsumeScene,accountConsumeSceneStoreRelationList);
    syncAccountConsumeScene(ShoppingActionTypeEnum.UPDATE,
        accountConsumeSceneMap);
    return true;
  }


  @Override
  @Transactional(rollbackFor = Exception.class)
  public Boolean delete(Long id) {
    UpdateWrapper<AccountConsumeScene> updateWrapper = new UpdateWrapper();
    updateWrapper.eq(AccountConsumeScene.ID, id);
    AccountConsumeScene accountConsumeScene = new AccountConsumeScene();
    accountConsumeScene.setDeleted(true);
    validationAccountConsumeScene(accountConsumeScene,false);
    Map<AccountConsumeScene,List<AccountConsumeSceneStoreRelation>> accountConsumeSceneMap = new HashMap<>();
    //下发数据
    List<AccountConsumeSceneStoreRelation> relationList = accountConsumeSceneStoreRelationList.getListByConsumeSceneId(id);
    accountConsumeSceneMap.put(accountConsumeScene,relationList);
    syncAccountConsumeScene(ShoppingActionTypeEnum.UPDATE,
        accountConsumeSceneMap);
    return accountConsumeSceneDao.update(accountConsumeScene, updateWrapper);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public Boolean updateStatus(Long id, Integer status) {
    UpdateWrapper<AccountConsumeScene> updateWrapper = new UpdateWrapper();
    updateWrapper.eq(AccountConsumeScene.ID, id);
    AccountConsumeScene accountConsumeScene = new AccountConsumeScene();
    accountConsumeScene.setStatus(status);
    validationAccountConsumeScene(accountConsumeScene,false);
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