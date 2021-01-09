package com.welfare.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.persist.dto.MerchantStoreRelationDTO;
import com.welfare.persist.dto.MerchantStoreRelationDetailDTO;
import com.welfare.persist.dto.query.MerchantStoreRelationAddReq;
import com.welfare.persist.entity.MerchantStoreRelation;
import java.util.List;
import java.util.Map;

/**
 * 商户消费场景配置服务接口
 *
 * @author Yuxiang Li
 * @description 由 Mybatisplus Code Generator 创建
 * @since 2021-01-06 13:49:25
 */
public interface MerchantStoreRelationService {

  Page<MerchantStoreRelation> pageQuery(Page<MerchantStoreRelation> page,
      QueryWrapper<MerchantStoreRelation> queryWrapper);

  Page<MerchantStoreRelationDTO> searchMerchantStoreRelations(Page<MerchantStoreRelation> page,
      String merName, String status);

  MerchantStoreRelation getMerchantStoreRelationById(QueryWrapper<MerchantStoreRelation> queryWrapper);

  List<MerchantStoreRelation> getMerchantStoreRelationListByMerCode(QueryWrapper<MerchantStoreRelation> queryWrapper);

  boolean add(MerchantStoreRelationAddReq relationAddReq);
}
