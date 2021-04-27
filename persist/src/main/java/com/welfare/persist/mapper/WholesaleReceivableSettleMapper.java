package com.welfare.persist.mapper;

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
    /**
     *  查询汇总明细
     * @param merCode 商户编码
     * @param supplierCode 供应商编码
     * @param transTimeStart 交易时间start
     * @param transTimeEnd 交易时间end
     * @return 应收汇总明细
     */
    List<PlatformWholesaleSettleGroupDTO> queryReceivable(@Param("merCode") String merCode,
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
     * @param param 查询参数
     * @return 应收结算明细
     */
    List<PlatformWholesaleSettleDetailDTO> queryReceivableDetails(PlatformWholesaleSettleDetailParam param);

    /**
     * 查询平台批发应收结算汇总
     * @param param 查询参数
     * @return 应收结算汇总
     */
    PlatformWholesaleSettleDetailSummaryDTO queryReceivableDetailsSummary(PlatformWholesaleSettleDetailParam param);
}
