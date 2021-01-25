package com.welfare.persist.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.welfare.common.enums.MerchantAccountTypeShowStatusEnum;
import com.welfare.persist.entity.MerchantAccountType;
import com.welfare.persist.mapper.MerchantAccountTypeMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * (商户福利类型)数据DAO
 *
 * @author hao.yin
 * @since 2021-01-09 11:54:16
 * @description 由 Mybatisplus Code Generator 创建
 */
@Slf4j
@Repository
public class MerchantAccountTypeDao extends ServiceImpl<MerchantAccountTypeMapper, MerchantAccountType> {
    public Integer updateAllColumnById(MerchantAccountType entity){
        return getBaseMapper().alwaysUpdateSomeColumnById(entity);
    }

    public List<MerchantAccountType> queryAllByMerCode(String merCode) {
        QueryWrapper<MerchantAccountType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(MerchantAccountType.MER_CODE,merCode);
        return list(queryWrapper);
    }


}