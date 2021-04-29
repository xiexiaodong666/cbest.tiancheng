package com.welfare.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.persist.dto.*;
import com.welfare.persist.dto.query.*;
import com.welfare.persist.dto.settlement.wholesale.PlatformWholesaleSettleDetailDTO;
import com.welfare.persist.dto.settlement.wholesale.PlatformWholesaleSettleGroupDTO;
import com.welfare.persist.entity.WholesalePayableSettle;
import com.welfare.persist.mapper.WholesalePayableSettleDetailMapper;
import com.welfare.service.WholesalePayableSettletService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/4/29 1:55 下午
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class WholesalePayableSettletServiceImpl implements WholesalePayableSettletService {

    private WholesalePayableSettleDetailMapper wholesalePayableSettleDetailMapper;

    @Override
    public Page<PlatformPayableSettleGroupDTO> pageQueryPayableSummary(PlatformWholesalePayablePageQuery query) {
        Page<PlatformPayableSettleGroupDTO> page = new Page<>(query.getCurrent(), query.getSize());
        return wholesalePayableSettleDetailMapper.pageUnsettleDataGroupBySupplierMerCode(page, query);
    }

    @Override
    public PlatformWholesalePayableGroupDTO queryPayableSummary(PlatformWholesalePayableQuery query) {
        return wholesalePayableSettleDetailMapper.summaryGroupBySupplierMerCode(query);
    }

    @Override
    public Page<PlatformWholesaleSettleDetailDTO> queryPagePayableDetails(PlatformWholesalePayableDetailPageQuery query) {
        return null;
    }

    @Override
    public PlatformWholesalePayableDetailSummaryDTO queryPayableDetailsSummary(PlatformWholesalePayableDetailQuery query) {
        return null;
    }

    @Override
    public List<PlatformWholesaleSettleDetailDTO> queryPayableDetails(PlatformWholesalePayableDetailQuery query) {
        return null;
    }

    @Override
    public WholesalePayableSettle generatePayableSettle(PlatformWholesalePayableDetailPageQuery query) {
        return null;
    }

    @Override
    public Integer monthSettleSend(Long id) {
        return null;
    }

    @Override
    public Integer monthSettleFinish(Long id) {
        return null;
    }

    @Override
    public Integer monthSettleConfirm(Long id) {
        return null;
    }

    @Override
    public Page<WholesalePayableSettleResp> payableBillPage(WholesalePayableSettleBillQuery query) {
        return null;
    }

    @Override
    public Page<WholesaleReceivableSettleDetailResp> payableBillDetailPage(Long id, WholesalePaySettleDetailPageQuery query) {
        return null;
    }

    @Override
    public WholesalePayableBillGroupDTO payableBillDetailSummary(Long id, WholesalePaySettleDetailQuery query) {
        return null;
    }

    @Override
    public List<WholesaleReceivableSettleDetailResp> queryPayableBillDetail(Long id, WholesalePaySettleDetailQuery query) {
        return null;
    }
}
