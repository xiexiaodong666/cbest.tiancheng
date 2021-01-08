package com.welfare.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import  com.welfare.persist.dao.AccountConsumeSceneDao;
import com.welfare.persist.entity.AccountConsumeScene;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.welfare.service.AccountConsumeSceneService;
import org.springframework.stereotype.Service;

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

    @Override
    public AccountConsumeScene getAccountConsumeScene(Long id) {
        return accountConsumeSceneDao.getById(id);
    }

    @Override
    public Boolean save(AccountConsumeScene accountConsumeScene) {
        return accountConsumeSceneDao.save(accountConsumeScene);
    }

    @Override
    public Boolean update(AccountConsumeScene accountConsumeScene) {
        return accountConsumeSceneDao.updateById(accountConsumeScene);
    }

    @Override
    public Boolean delete(Long id) {
        UpdateWrapper<AccountConsumeScene> updateWrapper = new UpdateWrapper();
        updateWrapper.eq(AccountConsumeScene.ID,id);
        AccountConsumeScene accountType = new AccountConsumeScene();
        accountType.setDeleted(true);
        return accountConsumeSceneDao.update(accountType,updateWrapper);
    }
}