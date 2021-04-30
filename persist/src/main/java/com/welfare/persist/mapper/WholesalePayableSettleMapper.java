package com.welfare.persist.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.persist.dto.WholesalePayableBillGroupDTO;
import com.welfare.persist.dto.WholesalePayableSettleResp;
import com.welfare.persist.dto.WholesaleReceivableSettleDetailResp;
import com.welfare.persist.dto.query.WholesalePaySettleDetailPageQuery;
import com.welfare.persist.dto.query.WholesalePaySettleDetailQuery;
import com.welfare.persist.dto.query.WholesalePayableSettleBillQuery;
import com.welfare.persist.entity.WholesalePayableSettle;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 批发应付结算账单(wholesale_payable_settle)数据Mapper
 *
 * @author Yuxiang Li
 * @since 2021-04-13 09:55:39
 * @description 由 Mybatisplus Code Generator 创建
*/
@Mapper
public interface WholesalePayableSettleMapper extends BaseMapper<WholesalePayableSettle> {

    Page<WholesalePayableSettleResp> payableBillPage(Page<WholesalePayableSettleResp> page, @Param("query") WholesalePayableSettleBillQuery query);



}
