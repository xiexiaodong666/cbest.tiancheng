package com.welfare.persist.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.persist.dto.CardInfoDTO;
import com.welfare.persist.entity.CardInfo;
import java.util.Date;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 卡信息(card_info)数据Mapper
 *
 * @author Yuxiang Li
 * @description 由 Mybatisplus Code Generator 创建
 * @since 2021-01-06 13:49:25
 */
@Mapper
public interface CardInfoMapper extends BaseMapper<CardInfo> {

  public String getCardId(@Param("merCode") String merCode);

  public Page<CardInfoDTO> list(Page<CardInfo> page,@Param("applyCode")String applyCode, @Param("cardName") String cardName,
      @Param("merCode") String merCode,
      @Param("cardType") String cardType, @Param("cardMedium") String cardMedium,
      @Param("cardStatus") String cardStatus, @Param("writtenStartTime") Date writtenStartTime,
      @Param("writtenEndTime") Date writtenEndTime, @Param("startTime") Date startTime,
      @Param("endTime") Date endTime, @Param("bindStartTime") Date bindStartTime,
      @Param("bindEndTime") Date bindEndTime);

}
