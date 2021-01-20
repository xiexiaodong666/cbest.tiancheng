package com.welfare.persist.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.welfare.persist.entity.SupplierStore;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 供应商门店(supplier_store)数据Mapper
 *
 * @author Yuxiang Li
 * @since 2021-01-06 13:49:25
 * @description 由 Mybatisplus Code Generator 创建
*/
@Mapper
public interface SupplierStoreMapper extends BaseMapper<SupplierStore> {
    /**
     * 更新所有字段（除了deleted）
     * @return
     */
    Integer alwaysUpdateSomeColumnById(@Param(Constants.ENTITY) SupplierStore supplierStore);

}
