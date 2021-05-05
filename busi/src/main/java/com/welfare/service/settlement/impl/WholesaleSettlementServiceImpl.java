package com.welfare.service.settlement.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.welfare.common.constants.WelfareConstant;
import com.welfare.common.constants.WelfareSettleConstant;
import com.welfare.common.exception.BizAssert;
import com.welfare.common.exception.ExceptionCode;
import com.welfare.persist.dao.WholesaleReceivableSettleDao;
import com.welfare.persist.dao.WholesaleReceivableSettleDetailDao;
import com.welfare.persist.dto.PlatformPayableSettleGroupDTO;
import com.welfare.persist.dto.SettleTaxSalesStatistics;
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
import com.welfare.persist.entity.OrderInfoDetail;
import com.welfare.persist.entity.WholesaleReceivableSettle;
import com.welfare.persist.entity.WholesaleReceivableSettleDetail;
import com.welfare.persist.mapper.OrderInfoDetailMapper;
import com.welfare.persist.mapper.WholesaleReceivableSettleDetailMapper;
import com.welfare.persist.mapper.WholesaleReceivableSettleMapper;
import com.welfare.service.settlement.WholesaleSettlementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 4/26/2021
 */
@Service
@RequiredArgsConstructor
public class WholesaleSettlementServiceImpl implements WholesaleSettlementService {
    private final WholesaleReceivableSettleMapper wholesaleReceivableSettleMapper;
    private final WholesaleReceivableSettleDetailMapper wholesaleReceivableSettleDetailMapper;

    private final WholesaleReceivableSettleDao wholesaleReceivableSettleDao;
    private final WholesaleReceivableSettleDetailDao wholesaleReceivableSettleDetailDao;
    private final OrderInfoDetailMapper orderInfoDetailMapper;
    private final ObjectMapper objectMapper;
    @Override
    public Page<PlatformWholesaleSettleGroupDTO> pageQueryReceivable(String merCode,
                                                                         String supplierCode,
                                                                         Date transTimeStart,
                                                                         Date transTimeEnd,
                                                                         int pageIndex,
                                                                         int pageSize){
            Page<PlatformWholesaleSettleGroupDTO> page = new Page<>(pageIndex, pageSize);

            return wholesaleReceivableSettleDetailMapper.queryReceivable(page, merCode, supplierCode, transTimeStart, transTimeEnd);
    }

    @Override
    public List<PlatformWholesaleSettleGroupDTO> queryReceivable(String merCode, String supplierCode, Date transTimeStart, Date transTimeEnd) {

      return null;
    }

    @Override
    public PlatformWholesaleSettleGroupDTO queryReceivableSummary(String merCode,
                                                                  String supplierCode,
                                                                  Date transTimeStart,
                                                                  Date transTimeEnd){
        return wholesaleReceivableSettleDetailMapper.queryReceivableSummary(merCode, supplierCode, transTimeStart, transTimeEnd);
    }

    @Override
    public List<PlatformWholesaleSettleDetailDTO> pageQueryReceivableDetails(PlatformWholesaleSettleDetailParam param) {

      return wholesaleReceivableSettleDetailMapper.queryReceivableDetails(param);
    }

    @Override
    public List<PlatformWholesaleSettleDetailDTO> queryReceivableDetails(PlatformWholesaleSettleDetailParam param) {

      return wholesaleReceivableSettleDetailMapper.queryReceivableDetails(param);
    }

    @Override
    public PlatformWholesaleSettleDetailSummaryDTO queryReceivableDetailsSummary(PlatformWholesaleSettleDetailParam param) {
        return wholesaleReceivableSettleDetailMapper.queryReceivableDetailsSummary(param);
    }

    @Override
    public WholesaleReceivableSettle generateReceivableSettle(PlatformWholesaleSettleDetailParam param) {
        List<PlatformWholesaleSettleDetailDTO> settleDetailDTOList = queryReceivableDetails(param);
        Set<String> orderNoSet = new HashSet<>();
        BigDecimal totalTransAmount = BigDecimal.ZERO;
        for (PlatformWholesaleSettleDetailDTO dto : settleDetailDTOList) {
            orderNoSet.add(dto.getOrderNo());
            if (WelfareConstant.TransType.CONSUME.code().equals(dto.getTransType())) {
                totalTransAmount = totalTransAmount.add(dto.getSaleAmount());
            } else if (WelfareConstant.TransType.REFUND.code().equals(dto.getTransType())) {
                totalTransAmount = totalTransAmount.add(dto.getSaleAmount().negate());
        //        totalTransAmount = totalTransAmount.add(dto.getTransAmount());
            }
        }
        List<OrderInfoDetail> groupByTaxRateDetails = orderInfoDetailMapper.queryGroupByTaxRate(new ArrayList<>(orderNoSet));
        WholesaleReceivableSettle wholesaleReceivableSettle = new WholesaleReceivableSettle();
        Date now = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String settleNo = param.getMerCode() + dateFormat.format(now);
        wholesaleReceivableSettle.setSettleNo(settleNo);
        wholesaleReceivableSettle.setSettleStatus(WelfareSettleConstant.SettleStatusEnum.SETTLING.code());
        wholesaleReceivableSettle.setMerCode(param.getMerCode());
        wholesaleReceivableSettle.setOrderNum(orderNoSet.size());
        wholesaleReceivableSettle.setSendStatus(WelfareSettleConstant.SettleSendStatusEnum.UNSENDED.code());
        wholesaleReceivableSettle.setSettleStartTime(param.getTransTimeStart());
        wholesaleReceivableSettle.setSettleEndTime(param.getTransTimeEnd());
        wholesaleReceivableSettle.setTransAmount(totalTransAmount);
        wholesaleReceivableSettle.setSettleAmount(totalTransAmount);
        return wholesaleReceivableSettle;
    }

    @Override
    public WholesaleReceivableSettle updateReceivableStatus(Long settleId, String sendStatus, String settleStatus) {
        WholesaleReceivableSettle settle = wholesaleReceivableSettleDao.getById(settleId);
        if(!StringUtils.isEmpty(settleId)){
            settle.setSendStatus(sendStatus);
        }
        if(!StringUtils.isEmpty(settleStatus)){
            settle.setSettleStatus(settleStatus);
            boolean update = wholesaleReceivableSettleDetailDao.update(
                    Wrappers.<WholesaleReceivableSettleDetail>lambdaUpdate()
                            .eq(WholesaleReceivableSettleDetail::getSettleNo, settle.getSettleNo())
                            .set(WholesaleReceivableSettleDetail::getSettleFlag, settleStatus));
            BizAssert.isTrue(update, ExceptionCode.DATA_BASE_ERROR);
        }
        wholesaleReceivableSettleDao.updateById(settle);
        return settle;
    }

    @Override
    public PageInfo<WholesaleReceivableSettleResp> receivableBillPage(WholesaleReceivableSettleBillQuery query)
        throws JsonProcessingException {
        PageInfo<WholesaleReceivableSettleResp> wholesaleReceivableSettleRespPageInfo = PageHelper
            .startPage(query.getCurrent(), query.getSize()).doSelectPageInfo(() -> {
                wholesaleReceivableSettleMapper.receivableBillPage(query);
            });
        List<WholesaleReceivableSettleResp> wholesaleReceivableSettleResps = wholesaleReceivableSettleRespPageInfo
            .getList();
        for (WholesaleReceivableSettleResp receivableSettleResp:
        wholesaleReceivableSettleResps ) {
            String settleTaxSalesStatistics = receivableSettleResp.getSettleTaxSalesStatistics();
            List<SettleTaxSalesStatistics> settleTaxSalesStatisticsList = objectMapper.readValue(settleTaxSalesStatistics, new TypeReference<List<SettleTaxSalesStatistics>>() {});

            receivableSettleResp.setSettleTaxSalesStatisticList(settleTaxSalesStatisticsList);
        }

        return wholesaleReceivableSettleRespPageInfo;
    }

    @Override
    public PageInfo<WholesaleReceivableSettleDetailResp> receivableBillDetailPage(Long id,
        WholesaleReceiveSettleDetailPageQuery query) {
        wholesaleReceivableSettleMapper.receivableBillDetailPage(query);
        return null;
    }

    @Override
    public WholesaleReceiveSettleSummaryResp receivableBillDetailSummary(Long id,
        WholesaleReceiveSettleDetailQuery query) {

        wholesaleReceivableSettleMapper.receivableBillDetailSummary(query);
        return null;
    }
}
