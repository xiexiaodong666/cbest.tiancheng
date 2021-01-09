package com.welfare.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.common.util.ApiUserHolder;
import com.welfare.common.util.GenerateCodeUtil;
import com.welfare.persist.dao.CardApplyDao;
import com.welfare.persist.dto.query.CardApplyAddReq;
import com.welfare.persist.dto.query.CardApplyUpdateReq;
import com.welfare.persist.entity.CardApply;
import com.welfare.persist.mapper.CardApplyMapper;
import com.welfare.service.CardApplyService;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

/**
 * 制卡信息服务接口实现
 *
 * @author Yuxiang Li
 * @description 由 Mybatisplus Code Generator 创建
 * @since 2021-01-06 13:49:25
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class CardApplyServiceImpl implements CardApplyService {

  private final CardApplyDao cardApplyDao;
  private final CardApplyMapper cardApplyMapper;

  @Override
  public Page<CardApply> pageQuery(Page<CardApply> page, String cardName, String merCode,
      String cardType, String cardMedium,
      Integer status, Date startTime, Date endTime) {

    return cardApplyMapper.searchCardApplys(page, cardName, merCode, cardType, cardMedium,
                                            status, startTime, endTime
    );
  }

  @Override
  public CardApply getMerchantStoreRelationById(QueryWrapper<CardApply> queryWrapper) {
    return cardApplyMapper.selectOne(queryWrapper);
  }

  @Override
  public boolean add(CardApplyAddReq cardApplyAddReq) {
    CardApply cardApply = new CardApply();

    // uuid 生成16位不重复的code, 暂不考虑分布式情况下的并发
    cardApply.setApplyCode(GenerateCodeUtil.getAccountIdByUUId());
    cardApply.setMerCode(cardApplyAddReq.getMerCode());
    cardApply.setCardName(cardApplyAddReq.getCardName());
    cardApply.setCardType(cardApplyAddReq.getCardType());
    cardApply.setCardMedium(cardApplyAddReq.getCardMedium());
    cardApply.setCardNum(cardApplyAddReq.getCardNum());
    cardApply.setIdentificationCode(cardApplyAddReq.getIdentificationCode());
    cardApply.setIdentificationLength(cardApplyAddReq.getIdentificationLength());
    cardApply.setRemark(cardApplyAddReq.getRemark());
    if (ApiUserHolder.getUserInfo() != null) {
      cardApply.setCreateUser(ApiUserHolder.getUserInfo().getUserName());
    }

    cardApply.setDeleted(false);
    cardApply.setStatus(0);

    // TODO 批量写入 cardInfo表
    return cardApplyDao.save(cardApply);
  }

  @Override
  public boolean update(CardApplyUpdateReq cardApplyUpdateReq) {
    CardApply cardApply = cardApplyDao.getById(cardApplyUpdateReq.getId());

    if(Strings.isNotEmpty(cardApplyUpdateReq.getCardName())) {
      cardApply.setCardName(cardApplyUpdateReq.getCardName());
    }

    if(Strings.isNotEmpty(cardApplyUpdateReq.getCardType())) {
      cardApply.setCardType(cardApplyUpdateReq.getCardType());
    }

    if(Strings.isNotEmpty(cardApplyUpdateReq.getCardMedium())) {
      cardApply.setCardMedium(cardApplyUpdateReq.getCardMedium());
    }

    if(cardApplyUpdateReq.getCardNum() != null) {
      cardApply.setCardNum(cardApplyUpdateReq.getCardNum());
    }

    if(Strings.isNotEmpty(cardApplyUpdateReq.getIdentificationCode())) {
      cardApply.setIdentificationCode(cardApplyUpdateReq.getIdentificationCode());
    }

    if(cardApplyUpdateReq.getIdentificationLength() != null) {
      cardApply.setIdentificationLength(cardApplyUpdateReq.getIdentificationLength());
    }

    if(Strings.isNotEmpty(cardApplyUpdateReq.getMerCode())) {
      cardApply.setMerCode(cardApplyUpdateReq.getMerCode());
    }

    if(Strings.isNotEmpty(cardApplyUpdateReq.getRemark())) {
      cardApply.setRemark(cardApplyUpdateReq.getRemark());
    }

    // TODO 批量修改 cardInfo
    cardApplyDao.saveOrUpdate(cardApply);
    return false;
  }

  @Override
  public boolean updateStatus(Long id, Integer delete, Integer status) {
    CardApply cardApply = cardApplyDao.getById(id);
    if(delete != null) {
      cardApply.setDeleted(delete!=0);
    }
    if(status != null) {
      cardApply.setStatus(status);
    }

    return cardApplyDao.saveOrUpdate(cardApply);
  }
}