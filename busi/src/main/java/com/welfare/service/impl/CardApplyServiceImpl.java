package com.welfare.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.common.constants.WelfareConstant;
import com.welfare.common.enums.SequenceTypeEnum;
import com.welfare.common.exception.BusiException;
import com.welfare.common.exception.ExceptionCode;
import com.welfare.common.util.UserInfoHolder;
import com.welfare.common.util.GenerateCodeUtil;
import com.welfare.persist.dao.CardApplyDao;
import com.welfare.persist.dao.CardInfoDao;
import com.welfare.persist.dto.query.CardApplyAddReq;
import com.welfare.persist.dto.query.CardApplyUpdateReq;
import com.welfare.persist.entity.CardApply;
import com.welfare.persist.entity.CardInfo;
import com.welfare.persist.mapper.CardApplyMapper;
import com.welfare.persist.mapper.CardInfoMapper;
import com.welfare.service.CardApplyService;
import com.welfare.service.SequenceService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

  private final CardInfoMapper cardInfoMapper;
  private final CardInfoDao cardInfoDao;

  private final SequenceService sequenceService;

  // 卡片规则 TC01 + 客户商户编号（4位）+  9位自增（从100000001开始

  private final static String prefix = "TC01";
  private final static Long startId = 100000001L;


  @Override
  public Page<CardApply> pageQuery(Page<CardApply> page, String cardName, String merCode,
      String cardType, String cardMedium,
      Integer status, Date startTime, Date endTime) {

    return cardApplyMapper.searchCardApplys(page, cardName, merCode, cardType, cardMedium,
                                            status, startTime, endTime
    );
  }

  @Override
  public List<CardApply> exportCardApplys(String cardName, String merCode, String cardType,
      String cardMedium, Integer status, Date startTime, Date endTime) {

    return cardApplyMapper.exportCardApplys(cardName, merCode, cardType, cardMedium,
                                            status, startTime, endTime
    );
  }

  @Override
  public CardApply getMerchantStoreRelationById(QueryWrapper<CardApply> queryWrapper) {
    return cardApplyMapper.selectOne(queryWrapper);
  }

  @Override
  @Transactional(rollbackFor = {Exception.class})
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
    if (UserInfoHolder.getUserInfo() != null) {
      cardApply.setCreateUser(UserInfoHolder.getUserInfo().getUserName());
    }

    cardApply.setDeleted(false);
    cardApply.setStatus(1);

    List<CardInfo> cardInfoList = new ArrayList<>();
    // String cardId = cardInfoMapper.getCardId(cardApplyAddReq.getMerCode());
    for (int i = 0; i < cardApplyAddReq.getCardNum(); i++) {
      CardInfo cardInfo = new CardInfo();
      cardInfo.setApplyCode(cardApply.getApplyCode());
      Long writeCardId = sequenceService.nextNo(
          SequenceTypeEnum.CARID.getCode(), cardApplyAddReq.getMerCode(), startId);
      cardInfo.setCardId(prefix + cardApplyAddReq.getMerCode() + writeCardId);
      cardInfo.setCardType(cardApply.getCardType());
      cardInfo.setMagneticStripe(prefix + GenerateCodeUtil.UUID());
      cardInfo.setCardStatus(WelfareConstant.CardStatus.NEW.code());
      cardInfo.setDeleted(false);
      cardInfo.setCreateUser(cardApply.getCreateUser());

      cardInfoList.add(cardInfo);
    }

    return cardApplyDao.save(cardApply) && cardInfoDao.saveBatch(cardInfoList);
  }


  @Override
  public boolean update(CardApplyUpdateReq cardApplyUpdateReq) {
    CardApply cardApply = cardApplyDao.getById(cardApplyUpdateReq.getId());
    QueryWrapper<CardInfo> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq(CardInfo.APPLY_CODE, cardApply.getApplyCode());
    queryWrapper.eq(CardInfo.CARD_STATUS, WelfareConstant.CardStatus.WRITTEN.code());
    List<CardInfo> cardInfoList = cardInfoDao.list(queryWrapper);
    if (CollectionUtils.isNotEmpty(cardInfoList)) {
      throw new BusiException(ExceptionCode.BUSI_ERROR_NO_PERMISSION, "卡片已写入，无法再更改信息", null);
    }

    if (Strings.isNotEmpty(cardApplyUpdateReq.getCardName())) {
      cardApply.setCardName(cardApplyUpdateReq.getCardName());
    }

    if (Strings.isNotEmpty(cardApplyUpdateReq.getCardType())) {
      cardApply.setCardType(cardApplyUpdateReq.getCardType());
    }

    if (Strings.isNotEmpty(cardApplyUpdateReq.getCardMedium())) {
      cardApply.setCardMedium(cardApplyUpdateReq.getCardMedium());
    }

    if (cardApplyUpdateReq.getCardNum() != null) {
      cardApply.setCardNum(cardApplyUpdateReq.getCardNum());
    }

    if (Strings.isNotEmpty(cardApplyUpdateReq.getIdentificationCode())) {
      cardApply.setIdentificationCode(cardApplyUpdateReq.getIdentificationCode());
    }

    if (cardApplyUpdateReq.getIdentificationLength() != null) {
      cardApply.setIdentificationLength(cardApplyUpdateReq.getIdentificationLength());
    }

    if (Strings.isNotEmpty(cardApplyUpdateReq.getMerCode())) {
      cardApply.setMerCode(cardApplyUpdateReq.getMerCode());
    }

    if (Strings.isNotEmpty(cardApplyUpdateReq.getRemark())) {
      cardApply.setRemark(cardApplyUpdateReq.getRemark());
    }

    queryWrapper.clear();
    queryWrapper.eq(CardInfo.APPLY_CODE, cardApply.getApplyCode());
    cardInfoList = cardInfoDao.list(queryWrapper);
    boolean saveCardApply = cardApplyDao.saveOrUpdate(cardApply);
    boolean updateCardInfo = true;
    if(CollectionUtils.isNotEmpty(cardInfoList)) {
      for (CardInfo cardInfo:
      cardInfoList) {
        cardInfo.setCardType(cardApply.getCardType());
      }
      updateCardInfo = cardInfoDao.saveOrUpdateBatch(cardInfoList);
    }
    return saveCardApply && updateCardInfo;
  }

  @Override
  public boolean updateStatus(Long id, Integer delete, Integer status) {
    CardApply cardApply = cardApplyDao.getById(id);
    if (delete != null) {
      cardApply.setDeleted(delete != 0);
    }
    if (status != null) {
      cardApply.setStatus(status);
    }

    return cardApplyDao.saveOrUpdate(cardApply);
  }

  @Override
  public CardApply queryByApplyCode(String applyCode) {
    QueryWrapper<CardApply> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq(CardApply.APPLY_CODE, applyCode);
    return cardApplyDao.getOne(queryWrapper);
  }

  /**
   * 自增卡号id
   */
  private String setId(String id) {
    //截取头部字母编号
    String head = id.substring(0, 9);
    //截取尾部数字
    String tail = id.substring(head.length(), id.length());
    //尾部数字 +1
    int num = Integer.valueOf(tail) + 1;
    //填充 0
    String s = null;
    for (int i = 0; i <= id.length(); i++) {
      s += "0";
    }
    //合并字符串
    s = s + num;
    s = s.substring(s.length() - tail.length(), s.length());
    return head + s;
  }

}