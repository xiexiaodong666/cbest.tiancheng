package com.welfare.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.persist.dto.*;
import com.welfare.persist.dto.query.*;
import com.welfare.persist.dto.settlement.wholesale.PlatformWholesaleSettleDetailDTO;
import com.welfare.persist.entity.WholesalePayableSettle;
import com.welfare.service.dto.WholesalePaySettleDetailReq;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/4/29 1:54 下午
 */
public interface WholesalePayableSettletService {


    /**
     * 分页查询平台应付未结算分组汇总
     * @param query
     * @return
     */
    Page<PlatformPayableSettleGroupDTO> pageQueryPayableSummary(PlatformWholesalePayablePageQuery query);

    /**
     * 查询平台应付未结算账单汇总
     * @param query
     * @return
     */
    PlatformWholesalePayableGroupDTO queryPayableSummary(PlatformWholesalePayableQuery query);

    /**
     * 分页查询平台应付未结算账单明细
     * @param query
     * @return
     */
    Page<PlatformWholesaleSettleDetailDTO> queryPagePayableDetails(PlatformWholesalePayableDetailPageQuery query);

    /**
     * 查询平台应付未结算帐单明细汇总
     * @param query
     * @return
     */
    PlatformWholesalePayableDetailSummaryDTO queryPayableDetailsSummary(PlatformWholesalePayableDetailQuery query);

    /**
     * 查询平台应付未结算账单明细(不分页)
     * @param query
     * @return
     */
    List<PlatformWholesaleSettleDetailDTO> queryPayableDetails(PlatformWholesalePayableDetailQuery query);

    /**
     * 生成应付结算单
     * @param query
     * @return
     */
    WholesalePayableSettle generatePayableSettle(PlatformWholesalePayableDetailQuery query);

    /**
     * 平台发送账单
     * @param id
     * @return
     */
    public Integer settleSend(@PathVariable("id")Long id);

    /**
     * 平台确认账单完成
     * @param id
     * @return
     */
    public Integer settleFinish(@PathVariable("id")Long id);


    /**
     * (商户应收结算单)商户确认账单
     * @param id
     * @return
     */
    public Integer settleConfirm(@PathVariable("id")Long id);


    /**
     * 分页查询应付结算单分组列表
     * @param query
     * @return
     */
    Page<WholesalePayableSettleResp> payableBillPage(WholesalePayableSettleBillQuery query);

    /**
     * 通过id查询结算单
     * @param id
     * @return
     */
    WholesalePayableSettleResp payableBillById(Long id);

    /**
     * (商户应收结算单)分页查询某个应付结算单明细列表
     * @param id
     * @param query
     * @return
     */
    Page<WholesalePayableSettleDetailResp> payableBillDetailPage(@PathVariable("id") Long id, WholesalePaySettleDetailPageQuery query);

    /**
     * (商户应收结算单)查询应付结算单明细数据汇总
     * @param id
     * @param query
     * @return
     */
    WholesalePayableBillGroupDTO payableBillDetailSummary(@PathVariable("id") Long id, WholesalePaySettleDetailReq query);

    /**
     * (商户应收结算单)查询某个应付结算单明细列表
     * @param id
     * @param query
     * @return
     */
    List<WholesalePayableSettleDetailResp> queryPayableBillDetail(@PathVariable("id") Long id, WholesalePaySettleDetailReq query);

    /**
     * 查询结算单下所有的消费门店
     * @param id
     * @return
     */
    List<StoreCodeAndNameDTO> storesBySettleId(Long id, String merCode);

    /**
     * 查询结算单下所有的消费客户
     * @param id
     * @return
     */
    List<MerCodeAndNameDTO> customerMersBySettleId(Long id);
}
