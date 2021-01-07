package com.welfare.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import  com.welfare.persist.dao.MerchantStoreRelationDao;
import com.welfare.persist.entity.MerchantStoreRelation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.welfare.service.MerchantStoreRelationService;
import org.springframework.stereotype.Service;

/**
 * 商户消费场景配置服务接口实现
 *
 * @author Yuxiang Li
 * @since 2021-01-06 13:49:25
 * @description 由 Mybatisplus Code Generator 创建
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class MerchantStoreRelationServiceImpl implements MerchantStoreRelationService {
    private final MerchantStoreRelationDao merchantStoreRelationDao;

    @Override
    public Page<MerchantStoreRelation> pageQuery(Page<MerchantStoreRelation> page,
        QueryWrapper<MerchantStoreRelation> queryWrapper) {
        Page<MerchantStoreRelation> resultPage = merchantStoreRelationDao.page(page, queryWrapper);
        return resultPage;
    }
}