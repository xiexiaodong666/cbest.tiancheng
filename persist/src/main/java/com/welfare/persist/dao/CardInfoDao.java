package com.welfare.persist.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.welfare.persist.entity.CardInfo;
import com.welfare.persist.entity.MerchantAccountType;
import com.welfare.persist.mapper.CardInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 卡信息(card_info)数据DAO
 *
 * @author Yuxiang Li
 * @since 2021-01-06 13:49:25
 * @description 由 Mybatisplus Code Generator 创建
 */
@Slf4j
@Repository
public class CardInfoDao extends ServiceImpl<CardInfoMapper, CardInfo> {
    public CardInfo getOneByMagneticStripe(String magneticStripe){
        QueryWrapper<CardInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(CardInfo.MAGNETIC_STRIPE,magneticStripe);
        return getOne(queryWrapper);
    }

    public CardInfo getOneByCardId(String cardId){
        QueryWrapper<CardInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(CardInfo.CARD_ID,cardId);
        return getOne(queryWrapper);
    }

    public Integer updateAllColumnById(@Param(Constants.ENTITY) CardInfo entity){
        return getBaseMapper().alwaysUpdateSomeColumnById(entity);
    }

    public CardInfo getOneByAccountCode(Long accountCode){
        QueryWrapper<CardInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(CardInfo.ACCOUNT_CODE,accountCode).last("limit 1");
        return getOne(queryWrapper);
    }
}