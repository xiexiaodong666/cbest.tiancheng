package com.welfare.persist.mapper;

import com.welfare.persist.dto.WholesaleReceivableSettleResp;
import com.welfare.persist.dto.query.WholesaleReceivableSettleBillQuery;
import com.welfare.persist.dto.settlement.wholesale.PlatformWholesaleSettleDetailDTO;
import com.welfare.persist.dto.settlement.wholesale.PlatformWholesaleSettleGroupDTO;
import com.welfare.persist.dto.settlement.wholesale.param.PlatformWholesaleSettleDetailParam;
import com.welfare.persist.dto.settlement.wholesale.param.PlatformWholesaleSettleDetailSummaryDTO;
import com.welfare.persist.entity.WholesaleReceivableSettle;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 批发应收结算账单(wholesale_receivable_settle)数据Mapper
 *
 * @author Yuxiang Li
 * @since 2021-04-13 09:55:39
 * @description 由 Mybatisplus Code Generator 创建
*/
@Mapper
public interface WholesaleReceivableSettleMapper extends BaseMapper<WholesaleReceivableSettle> {

  List<WholesaleReceivableSettleResp> receivableBillPage(WholesaleReceivableSettleBillQuery query);
}
