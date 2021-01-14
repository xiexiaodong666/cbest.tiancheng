package com.welfare.persist.mapper;

import com.welfare.persist.dto.MonthSettleDTO;
import com.welfare.persist.dto.MonthSettleDetailDTO;
import com.welfare.persist.dto.query.MonthSettleDetailQuery;
import com.welfare.persist.dto.query.MonthSettleQuery;
import com.welfare.persist.entity.MonthSettle;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 月度结算账单(month_settle)数据Mapper
 *
 * @author Yuxiang Li
 * @since 2021-01-06 13:49:25
 * @description 由 Mybatisplus Code Generator 创建
*/
@Mapper
public interface MonthSettleMapper extends BaseMapper<MonthSettle> {

    List<MonthSettleDTO> selectMonthSettle(MonthSettleQuery monthSettleQuery);

    MonthSettle sumSettleDetailToMonthSettle(MonthSettleDetailQuery monthSettleDetailQuery);

    Map<String, Object> selectMonthSettleSummaryInfo(MonthSettleQuery monthSettleQuery);
}
