package com.welfare.service.settlement.impl;

import com.alibaba.fastjson.JSON;
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
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
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
                                                                         int current,
                                                                         int size){
            Page<PlatformWholesaleSettleGroupDTO> page = new Page<>(current, size);

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

        List<PlatformWholesaleSettleDetailDTO>  platformWholesaleSettleDetailDTOList = wholesaleReceivableSettleDetailMapper.queryReceivableDetails(param);
        return platformWholesaleSettleDetailDTOList;
    }

    @Override
    public Page<PlatformWholesaleSettleDetailDTO> queryReceivableDetails(PlatformWholesaleSettleDetailParam param) {

        Page<PlatformWholesaleSettleDetailDTO> page = new Page<>(param.getCurrent(), param.getSize());
      return wholesaleReceivableSettleDetailMapper.queryReceivableDetails(page, param);
    }

    @Override
    public PlatformWholesaleSettleDetailSummaryDTO queryReceivableDetailsSummary(PlatformWholesaleSettleDetailParam param) {
        return wholesaleReceivableSettleDetailMapper.queryReceivableDetailsSummary(param);
    }

    @Override
    public WholesaleReceivableSettle generateReceivableSettle(PlatformWholesaleSettleDetailParam param) {
        List<PlatformWholesaleSettleDetailDTO> settleDetailDTOList = wholesaleReceivableSettleDetailMapper.queryReceivableDetails(param);
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
        Map<BigDecimal, BigDecimal> taxRateAndSettleAmountMap = new HashMap<>();

        WholesaleReceivableSettle wholesaleReceivableSettle = new WholesaleReceivableSettle();

        if(CollectionUtils.isNotEmpty(settleDetailDTOList)) {
            List<OrderInfoDetail> groupByTaxRateDetails = orderInfoDetailMapper.queryGroupByTaxRate(new ArrayList<>(orderNoSet));
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

            if (CollectionUtils.isNotEmpty(groupByTaxRateDetails)) {
                Map<BigDecimal, List<OrderInfoDetail>> map = groupByTaxRateDetails.stream().collect(Collectors
                                                                                                        .groupingBy(OrderInfoDetail::getWholesaleTaxRate));
                map.forEach((taxRate, list) -> {
                    BigDecimal settleAmount = list.stream().map(OrderInfoDetail::getWholesaleAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
                    taxRateAndSettleAmountMap.put(taxRate, settleAmount);
                });
                wholesaleReceivableSettle.setSettleTaxSalesStatistics(JSON.toJSONString(taxRateAndSettleAmountMap));
            }

            wholesaleReceivableSettleMapper.insert(wholesaleReceivableSettle);
        }

        return wholesaleReceivableSettle;
    }

    @Override
    public WholesaleReceivableSettle updateReceivableStatus(Long settleId, String sendStatus, String settleStatus,  String recStatus) {
        WholesaleReceivableSettle settle = wholesaleReceivableSettleDao.getById(settleId);
        if(!StringUtils.isEmpty(sendStatus)){
            settle.setSendStatus(sendStatus);
        }
        if(!StringUtils.isEmpty(recStatus)){
            settle.setRecStatus(recStatus);
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
    public Page<WholesaleReceivableSettleResp> receivableBillPage(WholesaleReceivableSettleBillQuery query)
        throws JsonProcessingException {
        Page<WholesaleReceivableSettleResp> page = new Page<>(query.getCurrent(), query.getSize());
        Page<WholesaleReceivableSettleResp> wholesaleReceivableSettleRespPageInfo =
            wholesaleReceivableSettleMapper.receivableBillPage(page, query);
        List<WholesaleReceivableSettleResp> wholesaleReceivableSettleResps = wholesaleReceivableSettleRespPageInfo.getRecords();
        for (WholesaleReceivableSettleResp receivableSettleResp:
        wholesaleReceivableSettleResps ) {
            String settleTaxSalesStatistics = receivableSettleResp.getSettleTaxSalesStatistics();
            List<SettleTaxSalesStatistics> settleTaxSalesStatisticsList = objectMapper.readValue(settleTaxSalesStatistics, new TypeReference<List<SettleTaxSalesStatistics>>() {});

            receivableSettleResp.setSettleTaxSalesStatisticList(settleTaxSalesStatisticsList);
        }

        return wholesaleReceivableSettleRespPageInfo;
    }

    @Override
    public Page<WholesaleReceivableSettleDetailResp> receivableBillDetailPage(Long id,
        WholesaleReceiveSettleDetailPageQuery query) {
        Page<WholesaleReceivableSettleDetailResp> page = new Page<>(query.getCurrent(), query.getSize());

        return wholesaleReceivableSettleMapper.receivableBillDetailPage(page, query);
    }

    @Override
    public List<WholesaleReceivableSettleDetailResp> receivableBillDetail(Long id,
        WholesaleReceiveSettleDetailPageQuery query) {
        WholesaleReceivableSettle wholesaleReceivableSettle = wholesaleReceivableSettleDao.getById(id);

        if(wholesaleReceivableSettle == null) {
            return null;
        }
        query.setSettleNo(wholesaleReceivableSettle.getSettleNo());
        return wholesaleReceivableSettleMapper.receivableBillDetailPage(query);
    }

    @Override
    public WholesaleReceiveSettleSummaryResp receivableBillDetailSummary(Long id,
        WholesaleReceiveSettleDetailQuery query) {

        return  wholesaleReceivableSettleMapper.receivableBillDetailSummary(query);
    }
}
