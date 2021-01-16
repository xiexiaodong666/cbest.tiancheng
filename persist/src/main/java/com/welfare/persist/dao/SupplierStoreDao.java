package com.welfare.persist.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.welfare.persist.entity.SupplierStore;
import lombok.extern.slf4j.Slf4j;
import com.welfare.persist.mapper.SupplierStoreMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

/**
 * 供应商门店(supplier_store)数据DAO
 *
 * @author Yuxiang Li
 * @since 2021-01-06 13:49:25
 * @description 由 Mybatisplus Code Generator 创建
 */
@Slf4j
@Repository
public class SupplierStoreDao extends ServiceImpl<SupplierStoreMapper, SupplierStore> {

    @CacheEvict(value = "supplierStore-by-cashierNo",key="#entity.cashierNo")
    public Integer updateAllColumnById(SupplierStore entity){
        return getBaseMapper().alwaysUpdateSomeColumnById(entity);
    }

    @Cacheable(value = "supplierStore-by-cashierNo",key="#cashierNo")
    public SupplierStore getOneByCashierNo(String cashierNo){
        QueryWrapper<SupplierStore> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SupplierStore.CASHIER_NO,cashierNo);
        return getOne(queryWrapper);
    }
}