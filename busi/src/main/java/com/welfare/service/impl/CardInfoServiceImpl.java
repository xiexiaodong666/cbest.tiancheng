package com.welfare.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.common.constants.WelfareConstant;
import com.welfare.common.constants.WelfareConstant.CardEnable;
import com.welfare.common.exception.BusiException;
import com.welfare.common.exception.ExceptionCode;
import com.welfare.persist.dao.CardInfoDao;
import com.welfare.persist.dto.CardInfoApiDTO;
import com.welfare.persist.dto.CardInfoDTO;
import com.welfare.persist.entity.CardInfo;
import com.welfare.persist.mapper.CardInfoMapper;
import com.welfare.service.CardInfoService;
import java.util.Date;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
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
  public List<CardInfoApiDTO> listByApplyCode(String applyCode, Integer status) {

    return cardInfoMapper.listByApplyCode(applyCode, status);
  }

  @Override
  public CardInfo updateWritten(CardInfo cardInfo) {

    cardInfo = cardInfoDao.getById(cardInfo.getId());
    if (cardInfo == null) {
      throw new BusiException(ExceptionCode.DATA_BASE_ERROR, "更新错误", null);
    }
    if (WelfareConstant.CardStatus.NEW.code().equals(cardInfo.getCardStatus())) {
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
  public boolean disableCard(Set<String> cardIdSet) {

    QueryWrapper<CardInfo> queryWrapper = new QueryWrapper<>();
    queryWrapper.in(CardInfo.CARD_ID, cardIdSet);
    List<CardInfo> cardInfoList = cardInfoDao.list(queryWrapper);
    if (CollectionUtils.isEmpty(cardInfoList)) {
      throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "未找到对应卡号", null);
    }

    for (CardInfo cardInfo :
        cardInfoList) {
      cardInfo.setEnabled(CardEnable.DISABLE.code());
    }

    return cardInfoDao.saveOrUpdateBatch(cardInfoList);
  }

  @Override
  public Page<CardInfoDTO> list(Integer currentPage, Integer pageSize, String cardId,
      String applyCode, String cardName,
      String merCode,
      String cardType, String cardMedium, Integer cardStatus, Date writtenStartTime,
      Date writtenEndTime, Date startTime, Date endTime, Date bindStartTime,
      Date bindEndTime) {
    Page<CardInfo> page = new Page<>(currentPage, pageSize);

    return cardInfoMapper.list(page, cardId, applyCode, cardName,
                               merCode, cardType, cardMedium,
                               cardStatus, writtenStartTime,
                               writtenEndTime, startTime,
                               endTime, bindStartTime, bindEndTime
    );
  }

  @Override
  public List<CardInfoDTO> exportCardInfo(String cardId, String cardName, String merCode,
      String cardType,
      String cardMedium, Integer cardStatus, Date writtenStartTime, Date writtenEndTime,
      Date startTime, Date endTime, Date bindStartTime, Date bindEndTime) {
    return cardInfoMapper.exportCardInfo(cardId, cardName,
                                         merCode, cardType, cardMedium,
                                         cardStatus, writtenStartTime,
                                         writtenEndTime, startTime,
                                         endTime, bindStartTime, bindEndTime
    );
  }

  @Override
  public boolean cardIsBind(String cardId) {
    QueryWrapper<CardInfo> cardInfoQueryWrapper = new QueryWrapper();
    cardInfoQueryWrapper.eq(CardInfo.CARD_ID, cardId);
    cardInfoQueryWrapper.isNotNull(CardInfo.ACCOUNT_CODE);
    CardInfo queryCardInfo = cardInfoDao.getOne(cardInfoQueryWrapper);
    if (null == queryCardInfo) {
      return false;
    } else {
      return true;
    }
  }
}