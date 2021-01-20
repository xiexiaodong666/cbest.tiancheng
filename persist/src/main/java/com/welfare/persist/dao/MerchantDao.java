package com.welfare.persist.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.welfare.persist.entity.Merchant;
import com.welfare.persist.mapper.MerchantMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * 商户信息(merchant)数据DAO
 *
 * @author Yuxiang Li
 * @since 2021-01-06 13:49:25
 * @description 由 Mybatisplus Code Generator 创建
 */
@Slf4j
@Repository
public class MerchantDao extends ServiceImpl<MerchantMapper, Merchant> {
    public Integer updateAllColumnById(Merchant entity){
        return getBaseMapper().alwaysUpdateSomeColumnById(entity);
    }
}