package com.welfare.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.persist.dto.MerchantStoreRelationDTO;
import com.welfare.persist.dto.query.MerchantStoreRelationAddReq;
import com.welfare.persist.dto.query.MerchantStoreRelationUpdateReq;
import com.welfare.persist.entity.MerchantStoreRelation;
import java.util.Date;
import java.util.List;

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
      String merName, String status, Date startTime,Date endTime);

  List<MerchantStoreRelationDTO> exportMerchantStoreRelations(String merName, String status, Date startTime,Date endTime);

  MerchantStoreRelation getMerchantStoreRelationById(
      QueryWrapper<MerchantStoreRelation> queryWrapper);

  List<MerchantStoreRelation> getMerchantStoreRelationListByMerCode(
      QueryWrapper<MerchantStoreRelation> queryWrapper);

  boolean add(MerchantStoreRelationAddReq relationAddReq);

  boolean update(MerchantStoreRelationUpdateReq relationAddReq);

  boolean updateStatus(Long id, Integer delete, Integer status);

}
