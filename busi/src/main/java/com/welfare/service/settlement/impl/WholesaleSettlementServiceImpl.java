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
import com.welfare.common.constants.WelfareConstant.MerCreditType;
import com.welfare.common.constants.WelfareSettleConstant;
import com.welfare.common.constants.WelfareSettleConstant.SettleRecStatusEnum;
import com.welfare.common.constants.WelfareSettleConstant.SettleStatusEnum;
import com.welfare.common.exception.BizAssert;
import com.welfare.common.exception.BizException;
import com.welfare.common.exception.ExceptionCode;
import com.welfare.persist.dao.WholesaleReceivableSettleDao;
import com.welfare.persist.dao.WholesaleReceivableSettleDetailDao;
import com.welfare.persist.dto.PlatformPayableSettleGroupDTO;
import com.welfare.persist.dto.SettleTaxSalesStatistics;
import com.welfare.persist.dto.WholesaleReceivableSettleDetailResp;
import com.welfare.persist.dto.WholesaleReceivableSettleResp;
import com.welfare.persist.dto.WholesaleReceiveSettleSummaryResp;
import com.welfare.persist.dto.query.GroupByTaxRateQuery;
import com.welfare.persist.dto.query.WholesaleReceivableSettleBillQuery;
import com.welfare.persist.dto.query.WholesaleReceiveSettleDetailPageQuery;
import com.welfare.persist.dto.query.WholesaleReceiveSettleDetailQuery;
import com.welfare.persist.dto.settlement.wholesale.PlatformWholesaleSettleDetailDTO;
import com.welfare.persist.dto.settlement.wholesale.PlatformWholesaleSettleGroupDTO;
import com.welfare.persist.dto.settlement.wholesale.param.PlatformWholesaleSettleDetailParam;
import com.welfare.persist.dto.settlement.wholesale.param.PlatformWholesaleSettleDetailSummaryDTO;
import com.welfare.persist.entity.OrderInfoDetail;
import com.welfare.persist.entity.WholesalePayableSettleDetail;
import com.welfare.persist.entity.WholesaleReceivableSettle;
import com.welfare.persist.entity.WholesaleReceivableSettleDetail;
import com.welfare.persist.mapper.OrderInfoDetailMapper;
import com.welfare.persist.mapper.WholesaleReceivableSettleDetailMapper;
import com.welfare.persist.mapper.WholesaleReceivableSettleMapper;
import com.welfare.service.dto.RestoreRemainingLimitReq;
import com.welfare.service.remote.MerchantCreditFeign;
import com.welfare.service.remote.entity.MerchantCreditResp;
import com.welfare.service.settlement.WholesaleSettlementService;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
@Slf4j
@RequiredArgsConstructor
public class WholesaleSettlementServiceImpl implements WholesaleSettlementService {
    private final WholesaleReceivableSettleMapper wholesaleReceivableSettleMapper;
    private final WholesaleReceivableSettleDetailMapper wholesaleReceivableSettleDetailMapper;

    private final WholesaleReceivableSettleDao wholesaleReceivableSettleDao;
    private final WholesaleReceivableSettleDetailDao wholesaleReceivableSettleDetailDao;
    private final OrderInfoDetailMapper orderInfoDetailMapper;
    private final ObjectMapper objectMapper;
    @Autowired(required = false)
    private MerchantCreditFeign merchantCreditFeign;
    @Override
    public Page<PlatformWholesaleSettleGroupDTO> pageQueryReceivable(String merCode,
        String merName,
                                                                         String supplierCode,
                                                                         Date transTimeStart,
                                                                         Date transTimeEnd,
                                                                         int current,
                                                                         int size){
            Page<PlatformWholesaleSettleGroupDTO> page = new Page<>(current, size);

            return wholesaleReceivableSettleDetailMapper.queryReceivable(page, merCode, merName,supplierCode, transTimeStart, transTimeEnd);
    }

    @Override
    public List<PlatformWholesaleSettleGroupDTO> queryReceivable(String merCode, String merName,String supplierCode, Date transTimeStart, Date transTimeEnd) {

      return null;
    }

    @Override
    public PlatformWholesaleSettleGroupDTO queryReceivableSummary(String merCode,String merName,
                                                                  String supplierCode,
                                                                  Date transTimeStart,
                                                                  Date transTimeEnd){
        return wholesaleReceivableSettleDetailMapper.queryReceivableSummary(merCode,merName, supplierCode, transTimeStart, transTimeEnd);
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
    @Transactional(rollbackFor = Exception.class)
    public WholesaleReceivableSettle generateReceivableSettle(PlatformWholesaleSettleDetailParam param) {
        param.setSettleFlag(SettleStatusEnum.UNSETTLED.code());
        List<PlatformWholesaleSettleDetailDTO> settleDetailDTOList = wholesaleReceivableSettleDetailMapper.queryReceivableDetails(param);
        if(CollectionUtils.isEmpty(settleDetailDTOList)){
            throw new BizException(ExceptionCode.ILLEGALITY_ARGUMENTS, "没有可以结算的明细数据", null);
        }


        Set<String> orderNoSet = new HashSet<>();
        BigDecimal totalTransAmount = BigDecimal.ZERO;
        BigDecimal totalSettleAmount = BigDecimal.ZERO;
        for (PlatformWholesaleSettleDetailDTO dto : settleDetailDTOList) {
            orderNoSet.add(dto.getOrderNo());
            if (WelfareConstant.TransType.CONSUME.code().equals(dto.getTransType())) {
                totalTransAmount = totalTransAmount.add(dto.getSaleAmount());
                totalSettleAmount = totalSettleAmount.add(dto.getSettleAmount());

            } else if (WelfareConstant.TransType.REFUND.code().equals(dto.getTransType())) {
                totalTransAmount = totalTransAmount.add(dto.getSaleAmount());
                totalSettleAmount = totalSettleAmount.add(dto.getSettleAmount());
                }
        }


        if(totalTransAmount.compareTo(new BigDecimal(0)) < 0){
            throw new BizException(ExceptionCode.ILLEGALITY_ARGUMENTS, "结算金额为负或者0，无法生成结算单", null);
        }

        List<SettleTaxSalesStatistics> settleTaxSalesStatisticsList = new ArrayList<>();
        WholesaleReceivableSettle wholesaleReceivableSettle = new WholesaleReceivableSettle();

        if(CollectionUtils.isNotEmpty(settleDetailDTOList)) {

            List<OrderInfoDetail> groupByTaxRateDetails = (orderInfoDetailMapper.queryByOrderIdAndTransNoGroupByTaxRate(GroupByTaxRateQuery
                                                                                                                            .ofRecieve(settleDetailDTOList)));
            Date now = Calendar.getInstance().getTime();
            DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            String settleNo = param.getMerCode() + dateFormat.format(now);
            wholesaleReceivableSettle.setSettleNo(settleNo);
            wholesaleReceivableSettle.setSettleStatus(WelfareSettleConstant.SettleStatusEnum.SETTLING.code());
            wholesaleReceivableSettle.setRecStatus(SettleRecStatusEnum.UNCONFIRMED.code());
            wholesaleReceivableSettle.setMerCode(param.getMerCode());
            wholesaleReceivableSettle.setOrderNum(orderNoSet.size());
            wholesaleReceivableSettle.setSendStatus(WelfareSettleConstant.SettleSendStatusEnum.UNSENDED.code());
            wholesaleReceivableSettle.setSettleStartTime(settleDetailDTOList.get(settleDetailDTOList.size() -1).getTransTime());
            wholesaleReceivableSettle.setSettleEndTime(settleDetailDTOList.get(0).getTransTime());
            wholesaleReceivableSettle.setTransAmount(totalTransAmount);
            wholesaleReceivableSettle.setSettleAmount(totalSettleAmount);

            //
            List<String> idList = settleDetailDTOList.stream().map(PlatformWholesaleSettleDetailDTO::getId).collect(Collectors.toList());

            WholesaleReceivableSettleDetail settleDetail = new WholesaleReceivableSettleDetail();
            settleDetail.setSettleNo(wholesaleReceivableSettle.getSettleNo());
            settleDetail.setSettleFlag(WelfareSettleConstant.SettleStatusEnum.SETTLING.code());
            wholesaleReceivableSettleDetailMapper.update(settleDetail, Wrappers.<WholesaleReceivableSettleDetail>lambdaUpdate()
                .in(WholesaleReceivableSettleDetail::getId, idList));

            if (CollectionUtils.isNotEmpty(groupByTaxRateDetails)) {
                groupByTaxRateDetails.forEach(c->{
                    if(null == c.getWholesaleTaxRate())  {
                        c.setWholesaleTaxRate(BigDecimal.ZERO);
                    }
                });
                Map<BigDecimal, List<OrderInfoDetail>> map = groupByTaxRateDetails.stream().collect(Collectors
                                                                                                        .groupingBy(OrderInfoDetail::getWholesaleTaxRate));
                map.forEach((taxRate, list) -> {
                    BigDecimal settleAmount = list.stream().map(OrderInfoDetail::getTransAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
                    SettleTaxSalesStatistics settleTaxSalesStatistics = new SettleTaxSalesStatistics();
                    settleTaxSalesStatistics.setAmount(settleAmount);
                    settleTaxSalesStatistics.setTax(taxRate);
                    settleTaxSalesStatisticsList.add(settleTaxSalesStatistics);
                });
                wholesaleReceivableSettle.setSettleTaxSalesStatistics(JSON.toJSONString(settleTaxSalesStatisticsList));
            }

            wholesaleReceivableSettleMapper.insert(wholesaleReceivableSettle);
        }

        return wholesaleReceivableSettle;
    }

    @Override
    public WholesaleReceivableSettle updateReceivableStatus(Long settleId, String sendStatus, String settleStatus,  String recStatus) {
        WholesaleReceivableSettle settle = wholesaleReceivableSettleDao.getById(settleId);
        BizAssert.isTrue(settle !=null, ExceptionCode.ILLEGALITY_ARGUMENTS);
        if(!StringUtils.isEmpty(sendStatus)){
            settle.setSendTime(new Date());
            settle.setSendStatus(sendStatus);
        }
        if(!StringUtils.isEmpty(recStatus)){
            settle.setConfirmTime(new Date());
            settle.setRecStatus(recStatus);
        }
        if(!StringUtils.isEmpty(settleStatus)){
            settle.setSettleStatus(settleStatus);
            boolean update = wholesaleReceivableSettleDetailDao.update(
                    Wrappers.<WholesaleReceivableSettleDetail>lambdaUpdate()
                            .eq(WholesaleReceivableSettleDetail::getSettleNo, settle.getSettleNo())
                            .set(WholesaleReceivableSettleDetail::getSettleFlag, settleStatus));
            BizAssert.isTrue(update, ExceptionCode.DATA_BASE_ERROR);
            if(SettleStatusEnum.SETTLED.code().equals(settleStatus)) {
                RestoreRemainingLimitReq restoreRemainingLimitReq = new RestoreRemainingLimitReq();
                restoreRemainingLimitReq.setMerCode(settle.getMerCode());
                restoreRemainingLimitReq.setAmount(settle.getSettleAmount());
                restoreRemainingLimitReq.setTransNo(settle.getSettleNo());
                restoreRemainingLimitReq.setSettleType(MerCreditType.WHOLESALE_CREDIT.code());
                log.info("调用商户服务，恢复商户授信额度，请求参数：{}",JSON.toJSONString(restoreRemainingLimitReq));
                MerchantCreditResp merchantCreditResp = merchantCreditFeign.remainingLimit(restoreRemainingLimitReq, "api");
                if(merchantCreditResp.getCode()!=1){
                    throw new BizException(ExceptionCode.UNKNOWN_EXCEPTION, "恢复商户授信额度失败", null);
                }
            }
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
            if(Strings.isNotEmpty(settleTaxSalesStatistics)) {
                List<SettleTaxSalesStatistics> settleTaxSalesStatisticsList = objectMapper.readValue(settleTaxSalesStatistics, new TypeReference<List<SettleTaxSalesStatistics>>() {});

                receivableSettleResp.setSettleTaxSalesStatisticList(settleTaxSalesStatisticsList);
            }
        }

        return wholesaleReceivableSettleRespPageInfo;
    }

    @Override
    public Page<WholesaleReceivableSettleDetailResp> receivableBillDetailPage(Long id,
        WholesaleReceiveSettleDetailPageQuery query) {
        WholesaleReceivableSettle wholesaleReceivableSettle = wholesaleReceivableSettleDao.getById(id);

        if(wholesaleReceivableSettle == null) {
            return null;
        }
        query.setSettleNo(wholesaleReceivableSettle.getSettleNo());

        Page<WholesaleReceivableSettleDetailResp> page = new Page<>(query.getCurrent(), query.getSize());

        return wholesaleReceivableSettleMapper.receivableBillDetailPage(page, query);
    }

    @Override
    public WholesaleReceivableSettleResp receivableBillDetail(Long id)
        throws JsonProcessingException {

        WholesaleReceivableSettleResp receivableSettleResp = wholesaleReceivableSettleMapper. receivableBill(id);
        if(Strings.isNotEmpty(receivableSettleResp.getSettleTaxSalesStatistics())) {
            List<SettleTaxSalesStatistics> settleTaxSalesStatisticsList = objectMapper.readValue(receivableSettleResp.getSettleTaxSalesStatistics(), new TypeReference<List<SettleTaxSalesStatistics>>() {});

            receivableSettleResp.setSettleTaxSalesStatisticList(settleTaxSalesStatisticsList);
        }
        return receivableSettleResp;
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
        WholesaleReceivableSettle wholesaleReceivableSettle = wholesaleReceivableSettleDao.getById(id);

        if(wholesaleReceivableSettle == null) {
            return null;
        }
        query.setSettleNo(wholesaleReceivableSettle.getSettleNo());

        return  wholesaleReceivableSettleMapper.receivableBillDetailSummary(query);
    }
}
