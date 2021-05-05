package com.welfare.persist.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.persist.dto.PlatformPayableSettleGroupDTO;
import com.welfare.persist.dto.WholesaleReceivableSettleResp;
import com.welfare.persist.dto.query.WholesaleReceivableSettleBillQuery;
import com.welfare.persist.dto.settlement.wholesale.PlatformWholesaleSettleDetailDTO;
import com.welfare.persist.dto.settlement.wholesale.PlatformWholesaleSettleGroupDTO;
import com.welfare.persist.dto.settlement.wholesale.param.PlatformWholesaleSettleDetailParam;
import com.welfare.persist.dto.settlement.wholesale.param.PlatformWholesaleSettleDetailSummaryDTO;
import com.welfare.persist.entity.WholesaleReceivableSettleDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * (wholesale_receivable_settle_detail)数据Mapper
 *
 * @author Yuxiang Li
 * @since 2021-04-13 09:55:40
 * @description 由 Mybatisplus Code Generator 创建
*/
@Mapper
public interface WholesaleReceivableSettleDetailMapper extends BaseMapper<WholesaleReceivableSettleDetail> {
  /**
   *  查询汇总明细
   * @param merCode 商户编码
   * @param supplierCode 供应商编码
   * @param transTimeStart 交易时间start
   * @param transTimeEnd 交易时间end
   * @return 应收汇总明细
   */
  Page<PlatformWholesaleSettleGroupDTO> queryReceivable(Page<PlatformWholesaleSettleGroupDTO> page, @Param("merCode") String merCode,
      @Param("supplierCode") String supplierCode,
      @Param("transTimeStart") Date transTimeStart,
      @Param("transTimeEnd") Date transTimeEnd);
  /**
   *  查询汇总
   * @param merCode 商户编码
   * @param supplierCode 供应商编码
   * @param transTimeStart 交易时间start
   * @param transTimeEnd 交易时间end
   * @return 应收汇总
   */
  PlatformWholesaleSettleGroupDTO queryReceivableSummary(@Param("merCode") String merCode,
      @Param("supplierCode") String supplierCode,
      @Param("transTimeStart") Date transTimeStart,
      @Param("transTimeEnd") Date transTimeEnd);

  /**
   * 查询平台应收结算明细
   * @return 应收结算明细
   */
  Page<PlatformWholesaleSettleDetailDTO> queryReceivableDetails(Page<PlatformWholesaleSettleDetailDTO> page, @Param("param") PlatformWholesaleSettleDetailParam param);

  /**
   * 查询平台应收结算明细
   * @return 应收结算明细
   */
  List<PlatformWholesaleSettleDetailDTO> queryReceivableDetails(@Param("param") PlatformWholesaleSettleDetailParam param);

  /**
   * 查询平台批发应收结算汇总
   * @param param 查询参数
   * @return 应收结算汇总
   */
  PlatformWholesaleSettleDetailSummaryDTO queryReceivableDetailsSummary(PlatformWholesaleSettleDetailParam param);
}
