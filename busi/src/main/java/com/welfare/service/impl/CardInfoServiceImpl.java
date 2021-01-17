package com.welfare.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.common.constants.WelfareConstant;
import com.welfare.common.exception.BusiException;
import com.welfare.common.exception.ExceptionCode;
import com.welfare.persist.dao.CardInfoDao;
import com.welfare.persist.dto.CardInfoDTO;
import com.welfare.persist.entity.CardInfo;
import com.welfare.persist.mapper.CardInfoMapper;
import com.welfare.service.CardInfoService;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 卡信息服务接口实现
 *
 * @author Yuxiang Li
 * @description 由 Mybatisplus Code Generator 创建
 * @since 2021-01-06 13:49:25
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class CardInfoServiceImpl implements CardInfoService {

  private final CardInfoDao cardInfoDao;
  private final CardInfoMapper cardInfoMapper;

  @Override
  public CardInfo getByCardNo(String cardNo) {
    QueryWrapper<CardInfo> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq(CardInfo.CARD_ID, cardNo);
    return cardInfoDao.getOne(queryWrapper);
  }

  @Override
  public CardInfo getByMagneticStripe(String magneticStripe) {
    QueryWrapper<CardInfo> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq(CardInfo.MAGNETIC_STRIPE, magneticStripe);
    return cardInfoDao.getOne(queryWrapper);
  }

  @Override
  public List<CardInfo> listByApplyCode(String applyCode, Integer status) {
    QueryWrapper<CardInfo> wrapper = new QueryWrapper<>();
    wrapper.eq(CardInfo.APPLY_CODE, applyCode).eq(CardInfo.CARD_STATUS, status);
    return cardInfoDao.list(wrapper);
  }

  @Override
  public CardInfo updateWritten(CardInfo cardInfo) {

    cardInfo = cardInfoDao.getById(cardInfo.getId());
    if(cardInfo == null) {
      throw new BusiException(ExceptionCode.DATA_BASE_ERROR, "更新错误", null);
    }
    if(WelfareConstant.CardStatus.NEW.code().equals(cardInfo.getCardStatus())) {
      cardInfo.setCardStatus(WelfareConstant.CardStatus.WRITTEN.code());
      cardInfo.setWrittenTime(new Date());

      if (cardInfoDao.saveOrUpdate(cardInfo)) {
        return cardInfo;
      } else {
        throw new BusiException(ExceptionCode.DATA_BASE_ERROR, "更新错误", null);
      }
    }

    return cardInfo;
  }

  @Override
  public Page<CardInfoDTO> list(Integer currentPage, Integer pageSize, String applyCode, String cardName,
      String merCode,
      String cardType, String cardMedium, Integer cardStatus, Date writtenStartTime,
      Date writtenEndTime, Date startTime, Date endTime, Date bindStartTime,
      Date bindEndTime) {
    Page<CardInfo> page = new Page<>(currentPage, pageSize);

    return cardInfoMapper.list(page, applyCode, cardName,
                               merCode, cardType, cardMedium,
                               cardStatus, writtenStartTime,
                               writtenEndTime, startTime,
                               endTime, bindStartTime, bindEndTime
    );
  }

  @Override
  public List<CardInfoDTO> exportCardInfo(String cardName, String merCode, String cardType,
      String cardMedium, Integer cardStatus, Date writtenStartTime, Date writtenEndTime,
      Date startTime, Date endTime, Date bindStartTime, Date bindEndTime) {
    return cardInfoMapper.exportCardInfo(cardName,
                               merCode, cardType, cardMedium,
                               cardStatus, writtenStartTime,
                               writtenEndTime, startTime,
                               endTime, bindStartTime, bindEndTime
    );
  }
}