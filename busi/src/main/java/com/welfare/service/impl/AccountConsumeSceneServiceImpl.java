package com.welfare.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.welfare.common.exception.BusiException;
import com.welfare.common.exception.ExceptionCode;
import  com.welfare.persist.dao.AccountConsumeSceneDao;
import com.welfare.service.dto.AccountConsumeSceneDTO;
import com.welfare.persist.dto.AccountConsumeSceneMapperDTO;
import com.welfare.persist.dto.AccountConsumeScenePageDTO;
import com.welfare.service.dto.AccountConsumeStoreDTO;
import com.welfare.persist.dto.query.AccountConsumePageQuery;
import com.welfare.persist.entity.AccountConsumeScene;
import com.welfare.persist.mapper.AccountConsumeSceneCustomizeMapper;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.welfare.service.AccountConsumeSceneService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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

    @Override
    public AccountConsumeScene getAccountConsumeScene(Long id) {
        return accountConsumeSceneDao.getById(id);
    }

    @Override
    public Boolean save(AccountConsumeScene accountConsumeScene) {
        return accountConsumeSceneDao.save(accountConsumeScene);
    }

    @Override
    public Boolean saveList(List<AccountConsumeScene> accountConsumeSceneList) {
        return accountConsumeSceneDao.saveBatch(accountConsumeSceneList);
    }

    @Override
    public Boolean updateList(List<AccountConsumeScene> accountConsumeSceneList) {
        return accountConsumeSceneDao.saveOrUpdateBatch(accountConsumeSceneList);
    }

    @Override
    public Boolean update(AccountConsumeScene accountConsumeScene) {
        return accountConsumeSceneDao.updateById(accountConsumeScene);
    }

    @Override
    public Boolean delete(Long id) {
        UpdateWrapper<AccountConsumeScene> updateWrapper = new UpdateWrapper();
        updateWrapper.eq(AccountConsumeScene.ID,id);
        AccountConsumeScene accountConsumeScene = new AccountConsumeScene();
        accountConsumeScene.setDeleted(true);
        return accountConsumeSceneDao.update(accountConsumeScene,updateWrapper);
    }

    @Override
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
            accountConsumePageReq.getAccountTypeId(),accountConsumePageReq.getStatus(),
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