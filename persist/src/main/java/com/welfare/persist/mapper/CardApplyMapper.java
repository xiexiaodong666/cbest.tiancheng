package com.welfare.persist.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.persist.dto.CardApplyDTO;
import com.welfare.persist.entity.CardApply;
import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 制卡信息(card_apply)数据Mapper
 *
 * @author Yuxiang Li
 * @description 由 Mybatisplus Code Generator 创建
 * @since 2021-01-06 13:49:25
 */
@Mapper
public interface CardApplyMapper extends BaseMapper<CardApply> {

  Page<CardApplyDTO> searchCardApplys(Page<CardApply> page, @Param("cardName") String cardName,
      @Param("merCode") String merCode, @Param("cardType") String cardType,
      @Param("cardMedium") String cardMedium,
      @Param("status") Integer status, @Param("startTime") Date startTime,
      @Param("endTime") Date endTime);

  List<CardApplyDTO> exportCardApplys( @Param("cardName") String cardName,
      @Param("merCode") String merCode, @Param("cardType") String cardType,
      @Param("cardMedium") String cardMedium,
      @Param("status") Integer status, @Param("startTime") Date startTime,
      @Param("endTime") Date endTime);
}
