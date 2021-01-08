package com.welfare.persist.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.persist.dto.MerchantStoreRelationDTO;
import com.welfare.persist.entity.MerchantStoreRelation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 商户消费场景配置(merchant_store_relation)数据Mapper
 *
 * @author Yuxiang Li
 * @since 2021-01-06 13:49:25
 * @description 由 Mybatisplus Code Generator 创建
*/
@Mapper
public interface MerchantStoreRelationMapper extends BaseMapper<MerchantStoreRelation> {

  Page<MerchantStoreRelationDTO> searchMerchantStoreRelations(
      Page<MerchantStoreRelation> page,
      @Param("merName")String merName, @Param("status") String status);
}
