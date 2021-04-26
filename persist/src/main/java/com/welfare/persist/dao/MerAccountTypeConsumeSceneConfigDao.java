package com.welfare.persist.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.welfare.persist.entity.AccountConsumeScene;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import com.welfare.persist.entity.MerAccountTypeConsumeSceneConfig;
import com.welfare.persist.mapper.MerAccountTypeConsumeSceneConfigMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.Future;

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

    @SneakyThrows
    public List<MerAccountTypeConsumeSceneConfig> query(String merCode, String storeCode, String sceneConsumeType){
        return list(
                Wrappers.<MerAccountTypeConsumeSceneConfig>lambdaQuery()
                .eq(MerAccountTypeConsumeSceneConfig::getMerCode,merCode)
                .eq(MerAccountTypeConsumeSceneConfig::getStoreCode,storeCode)
                .eq(MerAccountTypeConsumeSceneConfig::getSceneConsumeType,sceneConsumeType)
        );
    }

    public List<MerAccountTypeConsumeSceneConfig> getAllByMercode(List<String> merCodes){
        QueryWrapper<MerAccountTypeConsumeSceneConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.in(AccountConsumeScene.MER_CODE, merCodes);
        return list(queryWrapper);
    }
}