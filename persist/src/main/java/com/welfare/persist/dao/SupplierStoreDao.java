package com.welfare.persist.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.welfare.persist.entity.SupplierStore;
import com.welfare.persist.mapper.SupplierStoreMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    //@CacheEvict(value = "supplierStore-by-cashierNo",key="#entity.cashierNo")
    public Integer updateAllColumnById(SupplierStore entity){
        return getBaseMapper().alwaysUpdateSomeColumnById(entity);
    }

    //@Cacheable(value = "supplierStore-by-cashierNo",key="#cashierNo")
    public SupplierStore getOneByCashierNo(String cashierNo){
        QueryWrapper<SupplierStore> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SupplierStore.CASHIER_NO,cashierNo);
        return getOne(queryWrapper);
    }

    public SupplierStore getOneByCashierNoAndStoreCode(String cashierNo,String storeCode){
        QueryWrapper<SupplierStore> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SupplierStore.CASHIER_NO,cashierNo)
                .eq(SupplierStore.STORE_CODE,storeCode);
        return getOne(queryWrapper);
    }

    @Override
    public boolean save(SupplierStore entity){
        return super.save(entity);
    }

    @Override
    public boolean updateById(SupplierStore entity){
        return super.updateById(entity);
    }

    /**
     * 返回门店列表，指定列
     * @param columns
     * @return
     */
    public List<SupplierStore> selectAll(String... columns){
        QueryWrapper<SupplierStore> queryWrapper = new QueryWrapper<>();
        queryWrapper.select(columns);
        return list(queryWrapper);
    }

}