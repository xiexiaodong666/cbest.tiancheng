package com.welfare.persist.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.welfare.common.constants.AccountConsumeSceneStatus;
import com.welfare.persist.entity.AccountConsumeScene;
import com.welfare.persist.mapper.AccountConsumeSceneMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 员工消费场景配置(account_consume_scene)数据DAO
 *
 * @author Yuxiang Li
 * @since 2021-01-06 13:49:25
 * @description 由 Mybatisplus Code Generator 创建
 */
@Slf4j
@Repository
public class AccountConsumeSceneDao extends ServiceImpl<AccountConsumeSceneMapper, AccountConsumeScene> {
    public AccountConsumeScene getOneByAccountTypeAndMerCode(String accountType,String merCode){
        QueryWrapper<AccountConsumeScene> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(AccountConsumeScene.ACCOUNT_TYPE_CODE,accountType)
                .eq(AccountConsumeScene.MER_CODE,merCode);
        return getOne(queryWrapper);
    }

    @Cacheable(value = "getAccountTypeAndMerCode",key = "#accountType+#merCode")
    public List<AccountConsumeScene> getAccountTypeAndMerCode(String accountType, String merCode){
        QueryWrapper<AccountConsumeScene> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(AccountConsumeScene.ACCOUNT_TYPE_CODE,accountType)
                .eq(AccountConsumeScene.MER_CODE,merCode)
                .eq(AccountConsumeScene.STATUS, AccountConsumeSceneStatus.ENABLE.getCode());
        return list(queryWrapper);
    }

    public List<AccountConsumeScene> getAllByMercode(List<String> merCodes){
        QueryWrapper<AccountConsumeScene> queryWrapper = new QueryWrapper<>();
        queryWrapper.in(AccountConsumeScene.MER_CODE, merCodes);
        return list(queryWrapper);
    }

    public Boolean deleteConsumeSceneByIds(List<Long> ids) {
        QueryWrapper<AccountConsumeScene> queryWrapper = new QueryWrapper<>();
        queryWrapper.in(AccountConsumeScene.ID, ids);
        return getBaseMapper().delete(queryWrapper) > 0;
    }
}