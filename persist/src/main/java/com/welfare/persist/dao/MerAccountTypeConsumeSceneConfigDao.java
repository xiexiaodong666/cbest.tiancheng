package com.welfare.persist.dao;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import com.welfare.persist.entity.MerAccountTypeConsumeSceneConfig;
import com.welfare.persist.mapper.MerAccountTypeConsumeSceneConfigMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 福利类型消费门店场景配置(mer_account_type_consume_scene_config)数据DAO
 *
 * @author Yuxiang Li
 * @since 2021-04-12 10:38:04
 * @description 由 Mybatisplus Code Generator 创建
 */
@Slf4j
@Repository
public class MerAccountTypeConsumeSceneConfigDao extends ServiceImpl<MerAccountTypeConsumeSceneConfigMapper, MerAccountTypeConsumeSceneConfig> {
    public List<MerAccountTypeConsumeSceneConfig> query(String merCode, String storeCode, String sceneConsumeType){
        return list(
                Wrappers.<MerAccountTypeConsumeSceneConfig>lambdaQuery()
                .eq(MerAccountTypeConsumeSceneConfig::getMerCode,merCode)
                .eq(MerAccountTypeConsumeSceneConfig::getStoreCode,storeCode)
                .eq(MerAccountTypeConsumeSceneConfig::getSceneConsumeType,sceneConsumeType)
        );
    }
}