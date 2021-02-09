package com.welfare.persist.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import com.welfare.persist.entity.StoreConsumeType;
import com.welfare.persist.mapper.StoreConsumeTypeMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Repository;

/**
 * (store_consume_type)数据DAO
 *
 * @author Yuxiang Li
 * @since 2021-02-01 11:20:41
 * @description 由 Mybatisplus Code Generator 创建
 */
@Slf4j
@Repository
public class StoreConsumeTypeDao extends ServiceImpl<StoreConsumeTypeMapper, StoreConsumeType> {
    /**
     * 根据收银机号获取
     *
     * @param cashierNo
     * @param storeCode
     * @return
     */
    public StoreConsumeType getOneByCashierNoAndStoreNo( String cashierNo,String storeCode){
        QueryWrapper<StoreConsumeType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StoreConsumeType.CASHIER_NO,cashierNo).eq(StoreConsumeType.STORE_CODE,storeCode);
        return getOne(queryWrapper);
    }
}