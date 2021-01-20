package com.welfare.persist.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.welfare.persist.entity.MerchantAccountType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * (商户福利类型)数据Mapper
 *
 * @author hao.yin
 * @since 2021-01-09 11:54:16
 * @description 由 Mybatisplus Code Generator 创建
*/
@Mapper
public interface MerchantAccountTypeMapper extends BaseMapper<MerchantAccountType> {
    /**
     * 更新所有字段（除了deleted）
     * @return
     */
    Integer alwaysUpdateSomeColumnById(@Param(Constants.ENTITY) MerchantAccountType merchantAccountType);
}
