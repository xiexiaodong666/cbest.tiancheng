package com.welfare.persist.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.persist.dto.*;
import com.welfare.persist.dto.query.*;
import com.welfare.persist.dto.settlement.wholesale.PlatformWholesaleSettleDetailDTO;
import com.welfare.persist.dto.settlement.wholesale.PlatformWholesaleSettleGroupDTO;
import com.welfare.persist.entity.WholesalePayableSettle;
import com.welfare.persist.entity.WholesalePayableSettleDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (wholesale_payable_settle_detail)数据Mapper
 *
 * @author Yuxiang Li
 * @since 2021-04-13 09:55:39
 * @description 由 Mybatisplus Code Generator 创建
*/
@Mapper
public interface WholesalePayableSettleDetailMapper extends BaseMapper<WholesalePayableSettleDetail> {

    Page<PlatformPayableSettleGroupDTO> pageUnsettleDataGroupBySupplierMerCode(Page<PlatformPayableSettleGroupDTO> page,
                                                                               @Param("query") PlatformWholesalePayablePageQuery query);

    PlatformWholesalePayableGroupDTO summaryGroupBySupplierMerCode(@Param("query") PlatformWholesalePayableQuery query);


    Page<PlatformWholesaleSettleDetailDTO> queryPagePayableDetails(Page<PlatformWholesaleSettleDetailDTO> pag,
                                                             @Param("query") PlatformWholesalePayableDetailPageQuery query);

    PlatformWholesalePayableDetailSummaryDTO queryPayableDetailsSummary(@Param("query") PlatformWholesalePayableDetailQuery query);

    List<PlatformWholesaleSettleDetailDTO> queryPagePayableDetails(@Param("query") PlatformWholesalePayableDetailQuery query);

    Page<WholesalePayableSettleDetailResp> payableBillDetailPage(Page<WholesaleReceivableSettleDetailResp> page, @Param("query") WholesalePaySettleDetailQuery query);

    WholesalePayableBillGroupDTO payableBillDetailSummary(@Param("query") WholesalePaySettleDetailQuery query);

    List<WholesalePayableSettleDetailResp> payableBillDetailPage(@Param("query") WholesalePaySettleDetailQuery query);

    WholesalePayableSettle buildPayableSettle(@Param("query") PlatformWholesalePayableDetailQuery query);
}
