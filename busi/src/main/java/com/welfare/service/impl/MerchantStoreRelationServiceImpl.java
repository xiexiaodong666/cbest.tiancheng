package com.welfare.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.common.util.ApiUserHolder;
import  com.welfare.persist.dao.MerchantStoreRelationDao;
import com.welfare.persist.dto.AdminMerchantStore;
import com.welfare.persist.dto.MerchantStoreRelationDTO;
import com.welfare.persist.dto.MerchantStoreRelationDetailDTO;
import com.welfare.persist.dto.query.MerchantStoreRelationAddReq;
import com.welfare.persist.entity.MerchantStoreRelation;
import com.welfare.persist.mapper.MerchantStoreRelationMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.welfare.service.MerchantStoreRelationService;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final MerchantStoreRelationMapper merchantStoreRelationMapper;

    @Override
    public Page<MerchantStoreRelation> pageQuery(Page<MerchantStoreRelation> page,
        QueryWrapper<MerchantStoreRelation> queryWrapper) {
        Page<MerchantStoreRelation> resultPage = merchantStoreRelationDao.page(page, queryWrapper);
        return resultPage;
    }

    @Override
    public Page<MerchantStoreRelationDTO> searchMerchantStoreRelations(
        Page<MerchantStoreRelation> page,
        String merName, String status) {

        return merchantStoreRelationMapper.searchMerchantStoreRelations(page, merName, status);
    }

    @Override
    public MerchantStoreRelation getMerchantStoreRelationById(QueryWrapper<MerchantStoreRelation> queryWrapper) {
        return merchantStoreRelationDao.getOne(queryWrapper);
    }

    @Override
    public List<MerchantStoreRelation> getMerchantStoreRelationListByMerCode(
        QueryWrapper<MerchantStoreRelation> queryWrapper) {
        return merchantStoreRelationDao.list(queryWrapper);
    }

    @Override
    public boolean add(MerchantStoreRelationAddReq relationAddReq) {
        List<MerchantStoreRelation> merchantStoreRelationList = new ArrayList<>();
        List<AdminMerchantStore> adminMerchantStoreList = relationAddReq.getAdminMerchantStoreList();

        for (AdminMerchantStore store:
        adminMerchantStoreList) {
            MerchantStoreRelation merchantStoreRelation = new MerchantStoreRelation();
            merchantStoreRelation.setMerCode(relationAddReq.getMerCode());

            merchantStoreRelation.setRamark(relationAddReq.getRamark());
            merchantStoreRelation.setDeleted(false);
            merchantStoreRelation.setStatus(0);
            merchantStoreRelation.setStoreCode(store.getStoreCode());
            merchantStoreRelation.setConsumType(store.getConsumType());
            merchantStoreRelation.setStoreAlias(store.getStoreAlias());
            merchantStoreRelation.setIsRebate(store.getIsRebate());
            merchantStoreRelation.setRebateType(store.getRebateType());
            merchantStoreRelation.setRebateRatio(store.getRebateRatio());

            if(ApiUserHolder.getUserInfo() != null) {
                merchantStoreRelation.setCreateUser(ApiUserHolder.getUserInfo().getUserName());
            }

            merchantStoreRelationList.add(merchantStoreRelation);
        }
        return merchantStoreRelationDao.saveBatch(merchantStoreRelationList);
    }
}