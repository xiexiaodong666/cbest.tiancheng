package com.welfare.persist.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.welfare.persist.entity.SupplierStore;
import com.welfare.persist.mapper.SupplierStoreMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${e-welfare-cbest-mercodes:886623,M104,M105,M107}")
    private List<String> cBestMerCodes;


    //@CacheEvict(value = "supplierStore-by-cashierNo",key="#entity.cashierNo")
    public Integer updateAllColumnById(SupplierStore entity){
        return getBaseMapper().alwaysUpdateSomeColumnById(entity);
    }


    public SupplierStore getOneByCashierNoAndStoreCode(String cashierNo,String storeCode){
        QueryWrapper<SupplierStore> queryWrapper = new QueryWrapper<>();
        // TODO
       /* queryWrapper.eq(SupplierStore.CASHIER_NO,cashierNo)
                .eq(SupplierStore.STORE_CODE,storeCode);*/
        return getOne(queryWrapper);
    }

    public List<SupplierStore> listByMerCode(String merCode){
        QueryWrapper<SupplierStore> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SupplierStore.MER_CODE, merCode);
        return list(queryWrapper);
    }

    @Cacheable(value = "supplierStore-by-code",key = "#storeCode")
    public SupplierStore getOneByCode(String storeCode){
        QueryWrapper<SupplierStore> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SupplierStore.STORE_CODE, storeCode);
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
     * 返回所有重百旗下门店，指定列名查询，如果不传，则全列名查询
     * @param columns
     * @return
     */
    public List<SupplierStore> selectAllCbest(String... columns){
        QueryWrapper<SupplierStore> queryWrapper = new QueryWrapper<>();
        if(columns !=null){
            queryWrapper.select(columns);
        }
        queryWrapper.in(SupplierStore.MER_CODE, cBestMerCodes);
        return list(queryWrapper);
    }

    public List<SupplierStore> selectByCodes(List<String> storeCodes,String ...columns){
        QueryWrapper<SupplierStore> queryWrapper = new QueryWrapper<>();
        if(columns !=null){
            queryWrapper.select(columns);
        }
        queryWrapper.in(SupplierStore.STORE_CODE,storeCodes);
        return list(queryWrapper);
    }

}