package com.welfare.service.settlement;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.pagehelper.PageInfo;
import com.welfare.persist.dto.WholesaleReceivableSettleDetailResp;
import com.welfare.persist.dto.WholesaleReceivableSettleResp;
import com.welfare.persist.dto.WholesaleReceiveSettleSummaryResp;
import com.welfare.persist.dto.query.WholesaleReceivableSettleBillQuery;
import com.welfare.persist.dto.query.WholesaleReceiveSettleDetailPageQuery;
import com.welfare.persist.dto.query.WholesaleReceiveSettleDetailQuery;
import com.welfare.persist.dto.settlement.wholesale.PlatformWholesaleSettleDetailDTO;
import com.welfare.persist.dto.settlement.wholesale.PlatformWholesaleSettleGroupDTO;
import com.welfare.persist.dto.settlement.wholesale.param.PlatformWholesaleSettleDetailParam;
import com.welfare.persist.dto.settlement.wholesale.param.PlatformWholesaleSettleDetailSummaryDTO;
import com.welfare.persist.entity.WholesaleReceivableSettle;

import java.util.Date;
import java.util.List;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 4/26/2021
 */
public interface WholesaleSettlementService {
    /**
     *  查询汇总明细
     * @param merCode 商户编码
     * @param supplierCode 供应商编码
     * @param transTimeStart 交易时间start
     * @param transTimeEnd 交易时间end
     * @param pageIndex 页码
     * @param pageSize 分页大小
     * @return 应收汇总明细
     */
    PageInfo<PlatformWholesaleSettleGroupDTO> pageQueryReceivable(String merCode,
                                                                  String supplierCode,
                                                                  Date transTimeStart,
                                                                  Date transTimeEnd,
                                                                  int pageIndex,
                                                                  int pageSize);
    /**
     *  查询汇总明细
     * @param merCode 商户编码
     * @param supplierCode 供应商编码
     * @param transTimeStart 交易时间start
     * @param transTimeEnd 交易时间end
     * @return 应收汇总明细
     */
    List<PlatformWholesaleSettleGroupDTO> queryReceivable(String merCode,
                                                                  String supplierCode,
                                                                  Date transTimeStart,
                                                                  Date transTimeEnd);
    /**
     *  查询汇总明细
     * @param merCode 商户编码
     * @param supplierCode 供应商编码
     * @param transTimeStart 交易时间start
     * @param transTimeEnd 交易时间end
     * @return 应收汇总
     */
    PlatformWholesaleSettleGroupDTO queryReceivableSummary(String merCode,
                                                           String supplierCode,
                                                           Date transTimeStart,
                                                           Date transTimeEnd);

    /**
     * 分页查询平台应收结算明细
     * @param param 查询参数
     * @return 应收结算明细
     */
    PageInfo<PlatformWholesaleSettleDetailDTO> pageQueryReceivableDetails(PlatformWholesaleSettleDetailParam param);

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

    /**
     * 生成应收结算单
     * @param param 参数
     * @return 生成的应收结算单
     */
    WholesaleReceivableSettle generateReceivableSettle(PlatformWholesaleSettleDetailParam param);

    /**
     * 更新应收结算单状态
     * @param settleId 结算单id
     * @param sendStatus 发送状态
     * @param settleStatus 结算状态
     * @return
     */
    WholesaleReceivableSettle updateReceivableStatus(Long settleId, String sendStatus, String settleStatus);


    /**
     * 分页查询应收结算单分组列表
     * @param query
     * @return
     */
    PageInfo<WholesaleReceivableSettleResp> receivableBillPage(WholesaleReceivableSettleBillQuery query)
        throws JsonProcessingException;

    /**
     * 分页查询某个应收结算单明细列表
     * @param id
     * @param query
     * @return
     */
    PageInfo<WholesaleReceivableSettleDetailResp> receivableBillDetailPage(Long id, WholesaleReceiveSettleDetailPageQuery query);

    /**
     *
     * @param id
     * @param query
     * @return
     */
    WholesaleReceiveSettleSummaryResp receivableBillDetailSummary(Long id, WholesaleReceiveSettleDetailQuery query);
}
