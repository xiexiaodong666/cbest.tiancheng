package com.welfare.service.settlement;

import com.github.pagehelper.PageInfo;
import com.welfare.persist.dto.settlement.wholesale.PlatformWholesaleSettleGroupDTO;

import java.util.Date;

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
     * @return 应收汇总
     */
    PlatformWholesaleSettleGroupDTO queryReceivableSummary(String merCode,
                                                           String supplierCode,
                                                           Date transTimeStart,
                                                           Date transTimeEnd);
}
