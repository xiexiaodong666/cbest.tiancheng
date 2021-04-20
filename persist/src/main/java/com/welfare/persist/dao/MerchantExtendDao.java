package com.welfare.persist.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import com.welfare.persist.entity.MerchantExtend;
import com.welfare.persist.mapper.MerchantExtendMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Repository;

/**
 * 商户扩展表(merchant_extend)数据DAO
 *
 * @author Yuxiang Li
 * @since 2021-04-12 10:38:04
 * @description 由 Mybatisplus Code Generator 创建
 */
@Slf4j
@Repository
public class MerchantExtendDao extends ServiceImpl<MerchantExtendMapper, MerchantExtend> {

    public MerchantExtend getByMerCode(String merCode) {
        QueryWrapper<MerchantExtend> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(MerchantExtend.MER_CODE, merCode);
        return getOne(queryWrapper);
    }
}