package com.welfare.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.common.constants.WelfareConstant;
import com.welfare.common.enums.SequenceTypeEnum;
import com.welfare.common.exception.BusiException;
import com.welfare.common.exception.ExceptionCode;
import com.welfare.common.util.GenerateCodeUtil;
import com.welfare.common.util.UserInfoHolder;
import com.welfare.persist.dao.CardApplyDao;
import com.welfare.persist.dao.CardInfoDao;
import com.welfare.persist.dto.CardApplyDTO;
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
import java.util.stream.Collectors;
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
  private final static Long startId = 100000000L;


  @Override
  public Page<CardApplyDTO> pageQuery(Page<CardApply> page, String cardName, String merCode,
      String cardType, String cardMedium,
      Integer status, Date startTime, Date endTime) {

    return cardApplyMapper.searchCardApplys(page, cardName, merCode, cardType, cardMedium,
                                            status, startTime, endTime
    );
  }

  @Override
  public List<CardApplyDTO> exportCardApplys(String cardName, String merCode, String cardType,
      String cardMedium, Integer status, Date startTime, Date endTime) {

    return cardApplyMapper.exportCardApplys(cardName, merCode, cardType, cardMedium,
                                            status, startTime, endTime
    );
  }

  @Override
  public CardApply getCardApplyById(QueryWrapper<CardApply> queryWrapper) {
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

    Integer cardNum = cardApplyAddReq.getCardNum();

    List<CardInfo> cardInfoList = new ArrayList<>();
    Long writeCardId = sequenceService.nextNo(
        SequenceTypeEnum.CARDID.getCode(), cardApplyAddReq.getMerCode(), startId,
        cardNum
    );

    for (int i = 0; i < cardNum; i++) {
      CardInfo cardInfo = new CardInfo();
      cardInfo.setApplyCode(cardApply.getApplyCode());

      cardInfo.setCardId(prefix + cardApplyAddReq.getMerCode() + writeCardId);
      cardInfo.setMagneticStripe(prefix + GenerateCodeUtil.UUID());
      cardInfo.setCardStatus(WelfareConstant.CardStatus.NEW.code());
      cardInfo.setDeleted(false);
      cardInfo.setCreateUser(cardApply.getCreateUser());

      cardInfoList.add(cardInfo);
      writeCardId--;
    }
    

    return cardApplyDao.save(cardApply) && cardInfoDao.saveBatch(cardInfoList);
  }


  @Override
  public boolean update(CardApplyUpdateReq cardApplyUpdateReq) {
    CardApply cardApply = cardApplyDao.getById(cardApplyUpdateReq.getId());
    QueryWrapper<CardInfo> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq(CardInfo.APPLY_CODE, cardApply.getApplyCode());
    queryWrapper.ne(CardInfo.CARD_STATUS, WelfareConstant.CardStatus.NEW.code());
    List<CardInfo> cardInfoList = cardInfoDao.list(queryWrapper);
    // 已写卡或者绑卡的卡只能修改 卡名称
    if (CollectionUtils.isNotEmpty(cardInfoList)) {
      if (Strings.isNotEmpty(cardApplyUpdateReq.getCardName())) {
        cardApply.setCardName(cardApplyUpdateReq.getCardName());
      }
    } else {
      if (Strings.isNotEmpty(cardApplyUpdateReq.getCardName())) {
        cardApply.setCardName(cardApplyUpdateReq.getCardName());
      }

      if (Strings.isNotEmpty(cardApplyUpdateReq.getCardType())) {
        cardApply.setCardType(cardApplyUpdateReq.getCardType());
      }

      if (Strings.isNotEmpty(cardApplyUpdateReq.getCardMedium())) {
        cardApply.setCardMedium(cardApplyUpdateReq.getCardMedium());
      }

      if (Strings.isNotEmpty(cardApplyUpdateReq.getIdentificationCode())) {
        cardApply.setIdentificationCode(cardApplyUpdateReq.getIdentificationCode());
      }

      if (cardApplyUpdateReq.getIdentificationLength() != null) {
        cardApply.setIdentificationLength(cardApplyUpdateReq.getIdentificationLength());
      }

      if (Strings.isNotEmpty(cardApplyUpdateReq.getRemark())) {
        cardApply.setRemark(cardApplyUpdateReq.getRemark());
      }
    }

    boolean saveCardApply = cardApplyDao.saveOrUpdate(cardApply);

    return saveCardApply;
  }

  @Override
  public boolean updateStatus(Long id, Integer delete, Integer status) {
    CardApply cardApply = cardApplyDao.getById(id);
    boolean isDeletedCardInfo = true;
    boolean isDeletedCardApply = true;

    if (delete != null) {
      QueryWrapper<CardInfo> queryWrapperCardInfo = new QueryWrapper<>();
      queryWrapperCardInfo.eq(CardInfo.APPLY_CODE, cardApply.getApplyCode());
      queryWrapperCardInfo.ne(CardInfo.CARD_STATUS, WelfareConstant.CardStatus.NEW.code());
      List<CardInfo> cardInfoList = cardInfoDao.list(queryWrapperCardInfo);
      if (CollectionUtils.isNotEmpty(cardInfoList)) {
        throw new BusiException(ExceptionCode.BUSI_ERROR_NO_PERMISSION, "卡片已被写入或者绑定, 不能删除", null);
      } else {
        queryWrapperCardInfo.clear();
        queryWrapperCardInfo.eq(CardInfo.APPLY_CODE, cardApply.getApplyCode());
        cardInfoList = cardInfoDao.list(queryWrapperCardInfo);
        List<Long> ids = cardInfoList.stream().map(c -> c.getId()).collect(Collectors.toList());
        isDeletedCardInfo =  cardInfoDao.removeByIds(ids);
      }
      if(delete !=0) {
        isDeletedCardApply = cardApplyDao.removeById(cardApply.getId());
      }
    }
    if (status != null) {
      cardApply.setStatus(status);
    }

    return isDeletedCardApply && isDeletedCardInfo;
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