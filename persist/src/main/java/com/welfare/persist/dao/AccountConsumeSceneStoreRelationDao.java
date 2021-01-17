package com.welfare.persist.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.welfare.persist.entity.AccountConsumeSceneStoreRelation;
import com.welfare.persist.mapper.AccountConsumeSceneStoreRelationMapper;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Repository;

/**
 * 员工消费场景关联门店(account_consume_scene_store_relation)数据DAO
 *
 * @author yaox
 * @since 2021-01-09 11:01:12
 * @description 由 Mybatisplus Code Generator 创建
 */
@Slf4j
@Repository
public class AccountConsumeSceneStoreRelationDao extends
    ServiceImpl<AccountConsumeSceneStoreRelationMapper, AccountConsumeSceneStoreRelation> {

    public AccountConsumeSceneStoreRelation getOneBySceneIdAndStoreNo(Long sceneId,String storeNo){
        QueryWrapper<AccountConsumeSceneStoreRelation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(AccountConsumeSceneStoreRelation.ACCOUNT_CONSUME_SCENE_ID,sceneId)
                .eq(AccountConsumeSceneStoreRelation.STORE_CODE,storeNo);
        return getOne(queryWrapper);
    }
}