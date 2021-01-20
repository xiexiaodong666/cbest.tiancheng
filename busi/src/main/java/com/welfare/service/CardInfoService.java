package com.welfare.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.persist.dto.CardInfoApiDTO;
import com.welfare.persist.dto.CardInfoDTO;
import com.welfare.persist.entity.CardInfo;

import java.util.Date;
import java.util.List;

/**
 * 卡信息服务接口
 *
 * @author Yuxiang Li
 * @description 由 Mybatisplus Code Generator 创建
 * @since 2021-01-06 13:49:25
 */
public interface CardInfoService {

  /**
   * 根据卡号获取卡信息
   */
  CardInfo getByCardNo(String cardNo);

  /**
   * 根据磁条号获取卡信息
   * @param magneticStripe
   * @return
   */
  CardInfo getByMagneticStripe(String magneticStripe);
  /**
   * 根据申请号查询出所有
   */
  List<CardInfoApiDTO> listByApplyCode(String applyCode, Integer status);

  /**
   * 更新卡信息为已写入
   */
  CardInfo updateWritten(CardInfo cardInfo);


  /**
   * 查询卡信息集合
   * @param currentPage
   * @param pageSize
   * @param cardName
   * @param merCode
   * @param cardType
   * @param cardMedium
   * @param cardStatus
   * @param writtenStartTime
   * @param writtenEndTime
   * @param startTime
   * @param endTime
   * @param bindStartTime
   * @param bindEndTime
   * @return
   */
  Page<CardInfoDTO> list(Integer currentPage, Integer pageSize, String cardId,String applyCode, String cardName, String merCode,
      String cardType, String cardMedium, Integer cardStatus, Date writtenStartTime,
      Date writtenEndTime, Date startTime, Date endTime, Date bindStartTime,
      Date bindEndTime);

  List<CardInfoDTO> exportCardInfo(String cardId, String cardName, String merCode,
      String cardType, String cardMedium, Integer cardStatus, Date writtenStartTime,
      Date writtenEndTime, Date startTime, Date endTime, Date bindStartTime,
      Date bindEndTime);

  boolean cardIsBind(String cardId);
}
