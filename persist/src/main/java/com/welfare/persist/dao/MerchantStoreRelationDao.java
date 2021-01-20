package com.welfare.persist.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.welfare.persist.entity.MerchantStoreRelation;
import com.welfare.persist.mapper.MerchantStoreRelationMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

/**
 * 商户消费场景配置(merchant_store_relation)数据DAO
 *
 * @author Yuxiang Li
 * @since 2021-01-06 13:49:25
 * @description 由 Mybatisplus Code Generator 创建
 */
@Slf4j
@Repository
public class MerchantStoreRelationDao extends ServiceImpl<MerchantStoreRelationMapper, MerchantStoreRelation> {

    @Cacheable(value = "merchantStoreRelation-by-storeCode-merCode",key="#storeCode+#merCode")
    public MerchantStoreRelation getOneByStoreCodeAndMerCode(String storeCode, String merCode){
        QueryWrapper<MerchantStoreRelation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(MerchantStoreRelation.STORE_CODE,storeCode).eq(MerchantStoreRelation.MER_CODE,merCode);
        return getOne(queryWrapper);
    }
}