package com.welfare.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.welfare.common.constants.WelfareConstant;
import com.welfare.common.exception.BusiException;
import com.welfare.common.exception.ExceptionCode;
import  com.welfare.persist.dao.CardInfoDao;
import com.welfare.persist.entity.CardApply;
import com.welfare.persist.entity.CardInfo;
import com.welfare.service.CardApplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.welfare.service.CardInfoService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 卡信息服务接口实现
 *
 * @author Yuxiang Li
 * @since 2021-01-06 13:49:25
 * @description 由 Mybatisplus Code Generator 创建
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class CardInfoServiceImpl implements CardInfoService {
    private final CardInfoDao cardInfoDao;
    @Override
    public CardInfo getByCardNo(String cardNo) {
        QueryWrapper<CardInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(CardInfo.CARD_ID,cardNo);
        return cardInfoDao.getOne(queryWrapper);
    }

    @Override
    public List<CardInfo> listByApplyCode(String applyCode, Integer status) {
        QueryWrapper<CardInfo> wrapper = new QueryWrapper<>();
        wrapper.eq(CardInfo.APPLY_CODE,applyCode).eq(CardInfo.CARD_STATUS,status);
        return cardInfoDao.list(wrapper);
    }

    @Override
    public CardInfo updateWritten(CardInfo cardInfo) {
        cardInfo.setCardStatus(WelfareConstant.CardStatus.WRITTEN.code());
        if(cardInfoDao.updateById(cardInfo)){
            return cardInfo;
        }else{
            throw new BusiException(ExceptionCode.DATA_BASE_ERROR,"更新错误",null);
        }
    }
}