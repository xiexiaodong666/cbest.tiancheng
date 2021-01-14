package com.welfare.persist.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.persist.dto.MerchantStoreRelationDTO;
import com.welfare.persist.entity.MerchantStoreRelation;
import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 商户消费场景配置(merchant_store_relation)数据Mapper
 *
 * @author Yuxiang Li
 * @description 由 Mybatisplus Code Generator 创建
 * @since 2021-01-06 13:49:25
 */
@Mapper
public interface MerchantStoreRelationMapper extends BaseMapper<MerchantStoreRelation> {

  Page<MerchantStoreRelationDTO> searchMerchantStoreRelations(
      Page<MerchantStoreRelation> page,
      @Param("merName") String merName, @Param("status") String status,
      @Param("startTime") Date startTime, @Param("endTime") Date endTime);

  Boolean updateMerchantStoreRelationStatus(@Param("id") Long id);

  List<MerchantStoreRelationDTO> exportMerchantStoreRelations(
      @Param("merName") String merName, @Param("status") String status,
      @Param("startTime") Date startTime, @Param("endTime") Date endTime);

  List<MerchantStoreRelation> getMerchantStoreRelationListBySyncStatus(@Param("syncStatus") Integer syncStatus);
}
