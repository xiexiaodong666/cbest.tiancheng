package com.welfare.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.persist.dto.CardApplyDTO;
import com.welfare.persist.dto.query.CardApplyAddReq;
import com.welfare.persist.dto.query.CardApplyUpdateReq;
import com.welfare.persist.entity.CardApply;
import com.welfare.persist.entity.MerchantStoreRelation;
import io.swagger.models.auth.In;
import java.util.Date;
import java.util.List;

/**
 * 制卡信息服务接口
 *
 * @author Yuxiang Li
 * @since 2021-01-06 13:49:25
 * @description 由 Mybatisplus Code Generator 创建
 */
public interface CardApplyService {

  Page<CardApplyDTO> pageQuery(Page<CardApply> page,String cardName,String merCode, String cardType, String cardMedium,
      Integer status, Date startTime,Date endTime);

  List<CardApplyDTO> exportCardApplys(String cardName,String merCode, String cardType, String cardMedium,
      Integer status, Date startTime,Date endTime);

  CardApply getMerchantStoreRelationById(
      QueryWrapper<CardApply> queryWrapper);

  boolean add(CardApplyAddReq cardApplyAddReq);

  boolean update(CardApplyUpdateReq cardApplyUpdateReq);

  boolean updateStatus(Long id, Integer delete, Integer status);

  /**
   * 根据申请号查询
   * @param applyCode
   * @return
   */
  CardApply queryByApplyCode(String applyCode);

}
