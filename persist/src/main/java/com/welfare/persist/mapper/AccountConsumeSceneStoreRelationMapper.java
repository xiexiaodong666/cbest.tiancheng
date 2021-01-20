package com.welfare.persist.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.welfare.persist.entity.AccountConsumeSceneStoreRelation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 员工消费场景关联门店(account_consume_scene_store_relation)数据Mapper
 *
 * @author yaox
 * @since 2021-01-09 11:01:12
 * @description 由 Mybatisplus Code Generator 创建
*/
@Mapper
public interface AccountConsumeSceneStoreRelationMapper extends BaseMapper<AccountConsumeSceneStoreRelation> {
  public List<AccountConsumeSceneStoreRelation> queryRelationDetail(
      @Param("merCode")String merCode,@Param("storeCode")String storeCode);
}
