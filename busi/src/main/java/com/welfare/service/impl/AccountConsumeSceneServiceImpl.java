package com.welfare.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 员工消费场景配置服务接口实现
 *
 * @author Yuxiang Li
 * @since 2021-01-06 13:49:25
 * @description 由 Mybatisplus Code Generator 创建
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class AccountConsumeSceneServiceImpl implements AccountConsumeSceneService {
    private final AccountConsumeSceneDao accountConsumeSceneDao;
    private final AccountConsumeSceneCustomizeMapper accountConsumeSceneCustomizeMapper;
    private final AccountConsumeSceneStoreRelationDao accountConsumeSceneStoreRelationDao;

    @Override
    public AccountConsumeScene getAccountConsumeScene(Long id) {
        return accountConsumeSceneDao.getById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean save(AccountConsumeSceneAddReq accountConsumeSceneAddReq) {
        List<Long> accountTypeIdList = accountConsumeSceneAddReq.getAccountTypeIdList();
        accountTypeIdList.forEach(accountTypeId -> {
            AccountConsumeScene accountConsumeScene = new AccountConsumeScene();
            BeanUtils.copyProperties(accountConsumeSceneAddReq,accountConsumeScene);
            accountConsumeScene.setAccountTypeId(accountTypeId);
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
        List<AccountConsumeSceneStoreRelationReq> accountConsumeSceneStoreRelationReqList = accountConsumeSceneAddReq.getAccountConsumeSceneStoreRelationReqList();
        List<AccountConsumeSceneStoreRelation> accountConsumeSceneStoreRelationList = new ArrayList<>(accountConsumeSceneStoreRelationReqList.size());
        accountConsumeSceneStoreRelationReqList.forEach(accountConsumeSceneStoreRelationReq -> {
            AccountConsumeSceneStoreRelation accountConsumeSceneStoreRelation = new AccountConsumeSceneStoreRelation();
            BeanUtils
                .copyProperties(accountConsumeSceneStoreRelationReq,accountConsumeSceneStoreRelation);
            accountConsumeSceneStoreRelation.setAccountConsumeSceneId(accountConsumeScene.getId());
            accountConsumeSceneStoreRelationList.add(accountConsumeSceneStoreRelation);
        });
        return accountConsumeSceneStoreRelationList;
    }



    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean update(AccountConsumeSceneReq accountConsumeSceneReq) {
        AccountConsumeScene accountConsumeScene = new AccountConsumeScene();
        BeanUtils.copyProperties(accountConsumeSceneReq,accountConsumeScene);
        accountConsumeSceneDao.updateById(accountConsumeScene);
        List<AccountConsumeSceneStoreRelation> accountConsumeSceneStoreRelationList = new ArrayList<>();
        accountConsumeSceneReq.getAccountConsumeSceneStoreRelationReqList().stream().forEach(accountConsumeSceneStoreRelationReq -> {
            AccountConsumeSceneStoreRelation accountConsumeSceneStoreRelation = new AccountConsumeSceneStoreRelation();
            BeanUtils.copyProperties(accountConsumeSceneStoreRelationReq,accountConsumeSceneStoreRelation);
            accountConsumeSceneStoreRelationList.add(accountConsumeSceneStoreRelation);
        });
        accountConsumeSceneStoreRelationDao.updateBatchById(accountConsumeSceneStoreRelationList);
        return true;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean delete(Long id) {
        UpdateWrapper<AccountConsumeScene> updateWrapper = new UpdateWrapper();
        updateWrapper.eq(AccountConsumeScene.ID,id);
        AccountConsumeScene accountConsumeScene = new AccountConsumeScene();
        accountConsumeScene.setDeleted(true);
        return accountConsumeSceneDao.update(accountConsumeScene,updateWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateStatus(Long id, Integer status) {
        UpdateWrapper<AccountConsumeScene> updateWrapper = new UpdateWrapper();
        updateWrapper.eq(AccountConsumeScene.ID,id);
        AccountConsumeScene accountConsumeScene = new AccountConsumeScene();
        accountConsumeScene.setStatus(status);
        return accountConsumeSceneDao.update(accountConsumeScene,updateWrapper);
    }

    @Override
    public IPage<AccountConsumeScenePageDTO> getPageDTO(Page<AccountConsumeScenePageDTO> page,
        AccountConsumePageQuery accountConsumePageReq) {
        return accountConsumeSceneCustomizeMapper.getPageDTO(page,accountConsumePageReq.getMerCode(),
            accountConsumePageReq.getAccountTypeName(),accountConsumePageReq.getStatus(),
            accountConsumePageReq.getCreateTimeStart(),accountConsumePageReq.getCreateTimeEnd());
    }

    @Override
    public List<AccountConsumeScenePageDTO> export(AccountConsumePageQuery accountConsumePageReq) {
        return accountConsumeSceneCustomizeMapper.getPageDTO(accountConsumePageReq.getMerCode(),
            accountConsumePageReq.getAccountTypeName(),accountConsumePageReq.getStatus(),
            accountConsumePageReq.getCreateTimeStart(),accountConsumePageReq.getCreateTimeEnd());
    }

    @Override
    public AccountConsumeSceneDTO findAccountConsumeSceneDTOById(Long id) {
        AccountConsumeSceneDTO accountConsumeSceneDTO = new AccountConsumeSceneDTO();
        AccountConsumeSceneMapperDTO accountConsumeSceneMapperDTO = accountConsumeSceneCustomizeMapper.queryAccountConsumerScene4Detail(id);
        BeanUtils.copyProperties(accountConsumeSceneMapperDTO,accountConsumeSceneDTO);
        return accountConsumeSceneDTO;
    }
}