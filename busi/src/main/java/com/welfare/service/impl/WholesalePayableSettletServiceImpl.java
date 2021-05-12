package com.welfare.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.common.constants.RedisKeyConstant;
import com.welfare.common.constants.WelfareConstant;
import com.welfare.common.constants.WelfareSettleConstant;
import com.welfare.common.domain.MerchantUserInfo;
import com.welfare.common.domain.UserInfo;
import com.welfare.common.exception.BizAssert;
import com.welfare.common.exception.ExceptionCode;
import com.welfare.common.util.DistributedLockUtil;
import com.welfare.common.util.MerchantUserHolder;
import com.welfare.common.util.UserInfoHolder;
import com.welfare.persist.dto.*;
import com.welfare.persist.dto.query.*;
import com.welfare.persist.dto.settlement.wholesale.PlatformWholesaleSettleDetailDTO;
import com.welfare.persist.entity.*;
import com.welfare.persist.mapper.OrderInfoDetailMapper;
import com.welfare.persist.mapper.WholesalePayableSettleDetailMapper;
import com.welfare.persist.mapper.WholesalePayableSettleMapper;
import com.welfare.service.WholesalePayableSettletService;
import com.welfare.service.dto.WholesalePaySettleDetailReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/4/29 1:55 下午
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class WholesalePayableSettletServiceImpl implements WholesalePayableSettletService {

    private final WholesalePayableSettleDetailMapper wholesalePayableSettleDetailMapper;
    private final WholesalePayableSettleMapper wholesalePayableSettleMapper;
    private final OrderInfoDetailMapper orderInfoDetailMapper;

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
        Page<PlatformWholesaleSettleDetailDTO> page = new Page<>(query.getCurrent(), query.getSize());
        page.addOrder(OrderItem.desc(WholesalePayableSettleDetail.ID));
        return wholesalePayableSettleDetailMapper.queryPagePayableDetails(page, query);
    }

    @Override
    public PlatformWholesalePayableDetailSummaryDTO queryPayableDetailsSummary(PlatformWholesalePayableDetailQuery query) {
        return wholesalePayableSettleDetailMapper.queryPayableDetailsSummary(query);
    }

    @Override
    public List<PlatformWholesaleSettleDetailDTO> queryPayableDetails(PlatformWholesalePayableDetailQuery query) {
        return wholesalePayableSettleDetailMapper.queryPagePayableDetails(query);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public WholesalePayableSettle generatePayableSettle(PlatformWholesalePayableDetailQuery query) {
        BizAssert.notBlank(query.getMerCode(), ExceptionCode.ILLEGALITY_ARGUMENTS, "商户编码必传");
        RLock rLock = DistributedLockUtil.lockFairly(RedisKeyConstant.buidKey(RedisKeyConstant.BUILD_WHOLESALE_PAYABLE_SETTLE, query.getMerCode()));
        WholesalePayableSettle payableSettle;
        try {
            payableSettle = wholesalePayableSettleDetailMapper.buildPayableSettle(query);
            BizAssert.notNull(payableSettle, ExceptionCode.DATA_NOT_EXIST, "没有可以结算的明细数据");

            query.setLimit(WelfareSettleConstant.LIMIT);
            List<WholesalePayableSettleDetail> details;
            Map<BigDecimal, BigDecimal> taxRateAndSettleAmountMap = new HashMap<>();
            List<OrderInfoDetail> orderInfoDetails = new ArrayList<>();

            do {
                details = wholesalePayableSettleDetailMapper.getSettleDetailIdAndOrderIdList(query);
                if (!details.isEmpty()) {
                    List<Long> idList = details.stream().map(WholesalePayableSettleDetail::getId).collect(Collectors.toList());
                    WholesalePayableSettleDetail settleDetail = new WholesalePayableSettleDetail();
                    settleDetail.setSettleNo(payableSettle.getSettleNo());
                    settleDetail.setSettleFlag(WelfareSettleConstant.SettleStatusEnum.SETTLING.code());
                    wholesalePayableSettleDetailMapper.update(settleDetail, Wrappers.<WholesalePayableSettleDetail>lambdaUpdate()
                            .in(WholesalePayableSettleDetail::getId, idList));
                    // 计算各税点的商品结算金额
                    List<String> orderList = details.stream().map(WholesalePayableSettleDetail::getOrderId).collect(Collectors.toList());
                    orderInfoDetails.addAll(orderInfoDetailMapper.queryGroupByTaxRate(orderList));
                } else {
                    break;
                }
            } while (true);
            if (CollectionUtils.isNotEmpty(orderInfoDetails)) {
                Map<BigDecimal, List<OrderInfoDetail>> map = orderInfoDetails.stream().collect(Collectors.groupingBy(OrderInfoDetail::getWholesaleTaxRate));
                map.forEach((taxRate, list) -> {
                    BigDecimal settleAmount = list.stream().map(OrderInfoDetail::getWholesaleAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
                    taxRateAndSettleAmountMap.put(taxRate, settleAmount);
                });
                payableSettle.setSettleTaxSalesStatistics(JSON.toJSONString(taxRateAndSettleAmountMap));
            }
            wholesalePayableSettleMapper.insert(payableSettle);
        } finally {
            DistributedLockUtil.unlock(rLock);
        }
        return payableSettle;
    }

    @Override
    public Integer settleSend(Long id) {
        WholesalePayableSettle payableSettle = new WholesalePayableSettle();
        //修改账单发送状态为已发送
        payableSettle.setSendStatus(WelfareSettleConstant.SettleSendStatusEnum.SENDED.code());
        payableSettle.setSendTime(new Date());

        UserInfo userInfo = UserInfoHolder.getUserInfo();
        payableSettle.setCreateUser(userInfo.getUserId());

        return wholesalePayableSettleMapper.update(payableSettle,
                Wrappers.<WholesalePayableSettle>lambdaUpdate()
                        .eq(WholesalePayableSettle::getSendStatus, WelfareSettleConstant.SettleSendStatusEnum.UNSENDED.code())
                        .eq(WholesalePayableSettle::getId, id)
        );
    }

    @Override
        public Integer settleFinish(Long id) {
        //修改账单结算状态为已结算
        WholesalePayableSettle payableSettle = new WholesalePayableSettle();
        payableSettle.setSettleStatus(WelfareSettleConstant.SettleStatusEnum.SETTLED.code());

        UserInfo userInfo = UserInfoHolder.getUserInfo();
        payableSettle.setUpdateUser(userInfo.getUserId());

        int i = wholesalePayableSettleMapper.update(payableSettle,
                Wrappers.<WholesalePayableSettle>lambdaUpdate()
                        .eq(WholesalePayableSettle::getSettleStatus, WelfareSettleConstant.SettleStatusEnum.SETTLING.code())
                        .eq(WholesalePayableSettle::getRecStatus, WelfareSettleConstant.SettleRecStatusEnum.CONFIRMED.code())
                        .eq(WholesalePayableSettle::getId, id)
        );

        WholesalePayableSettle settle = wholesalePayableSettleMapper.selectById(id);
        BizAssert.notNull(settle, ExceptionCode.ILLEGALITY_ARGUMENTS, "结算单不存在");
        WholesalePayableSettleDetail detail = new WholesalePayableSettleDetail();
        detail.setSettleFlag(WelfareSettleConstant.SettleStatusEnum.SETTLED.code());
        wholesalePayableSettleDetailMapper.update(detail, Wrappers.<WholesalePayableSettleDetail>lambdaUpdate()
                        .eq(WholesalePayableSettleDetail::getSettleNo, settle.getSettleNo())
        );
        return i;
    }

    @Override
    public Integer settleConfirm(Long id) {
        WholesalePayableSettle payableSettle = new WholesalePayableSettle();

        //修改账单确认状态为已确认
        payableSettle.setRecStatus(WelfareSettleConstant.SettleRecStatusEnum.CONFIRMED.code());

        MerchantUserInfo merchantUser = MerchantUserHolder.getMerchantUser();
        payableSettle.setConfirmTime(new Date());
        payableSettle.setUpdateUser(merchantUser.getUserCode());

        return wholesalePayableSettleMapper.update(payableSettle,
                Wrappers.<WholesalePayableSettle>lambdaUpdate()
                        .eq(WholesalePayableSettle::getSendStatus, WelfareSettleConstant.SettleSendStatusEnum.SENDED.code())
                        .eq(WholesalePayableSettle::getId, id)
        );
    }

    @Override
    public Page<WholesalePayableSettleResp> payableBillPage(WholesalePayableSettleBillQuery query) {
        Page<WholesalePayableSettleResp> page = new Page<>(query.getCurrent(), query.getSize());
        page.addOrder(OrderItem.desc(WholesalePayableSettle.ID));
        page = wholesalePayableSettleMapper.payableBillPage(page, query);
        if (CollectionUtils.isNotEmpty(page.getRecords())) {
            page.getRecords().forEach(wholesalePayableSettleResp -> {
                wholesalePayableSettleResp
                        .setMerCooperationModeName(WelfareSettleConstant.WholesaleSettleMethodEnum
                                .findByCode(wholesalePayableSettleResp.getMerCooperationMode()).desc());
            });
        }
        return page;
    }

    @Override
    public WholesalePayableSettleResp payableBillById(Long id) {
        WholesalePayableSettleBillQuery query = new WholesalePayableSettleBillQuery();
        query.setId(id);
        Page<WholesalePayableSettleResp> page = payableBillPage(query);
        if (CollectionUtils.isNotEmpty(page.getRecords())) {
            return page.getRecords().get(0);
        } else {
            return null;
        }
    }

    @Override
    public Page<WholesalePayableSettleDetailResp> payableBillDetailPage(Long id, WholesalePaySettleDetailPageQuery query) {
        WholesalePaySettleDetailReq wholesalePaySettleDetailReq = new WholesalePaySettleDetailReq();
        BeanUtils.copyProperties(query, wholesalePaySettleDetailReq);
        WholesalePaySettleDetailQuery paySettleDetailQuery = getWholesalePaySettleDetailQuery(id, wholesalePaySettleDetailReq);
        Page<WholesaleReceivableSettleDetailResp> page = new Page<>(query.getCurrent(), query.getSize());
        page.addOrder(OrderItem.desc(WholesalePayableSettleDetail.ID));
        return wholesalePayableSettleDetailMapper.payableBillDetailPage(page, paySettleDetailQuery);
    }

    @Override
    public WholesalePayableBillGroupDTO payableBillDetailSummary(Long id, WholesalePaySettleDetailReq query) {
        WholesalePaySettleDetailQuery paySettleDetailQuery = getWholesalePaySettleDetailQuery(id, query);
        return wholesalePayableSettleDetailMapper.payableBillDetailSummary(paySettleDetailQuery);
    }

    @Override
    public List<WholesalePayableSettleDetailResp> queryPayableBillDetail(Long id, WholesalePaySettleDetailReq query) {
        WholesalePaySettleDetailQuery paySettleDetailQuery = getWholesalePaySettleDetailQuery(id, query);
        return wholesalePayableSettleDetailMapper.payableBillDetailPage(paySettleDetailQuery);
    }

    @Override
    public List<StoreCodeAndNameDTO> storesBySettleId(Long id, String merCode) {
        WholesalePayableSettle payableSettle = wholesalePayableSettleMapper.selectById(id);
        if (Objects.nonNull(payableSettle)) {
            List<WholesalePayableSettleDetail> details = wholesalePayableSettleDetailMapper.selectList(
                    Wrappers.<WholesalePayableSettleDetail>lambdaQuery()
                            .eq(WholesalePayableSettleDetail::getSettleNo, payableSettle.getSettleNo())
                            .eq(StringUtils.isNotEmpty(merCode), WholesalePayableSettleDetail::getMerCode, merCode)
            );
            return details.stream().map(detail -> {
                StoreCodeAndNameDTO storeCodeAndNameDTO = new StoreCodeAndNameDTO();
                storeCodeAndNameDTO.setStoreCode(detail.getStoreCode());
                storeCodeAndNameDTO.setStoreName(detail.getStoreName());
                return storeCodeAndNameDTO;
            }).collect(Collectors.collectingAndThen(Collectors.toCollection(() ->
                    new TreeSet<>(Comparator.comparing(StoreCodeAndNameDTO::getStoreCode))), ArrayList::new));
        }
        return null;
    }

    @Override
    public List<MerCodeAndNameDTO> customerMersBySettleId(Long id) {
        WholesalePayableSettle payableSettle = wholesalePayableSettleMapper.selectById(id);
        if (Objects.nonNull(payableSettle)) {
            List<WholesalePayableSettleDetail> details = wholesalePayableSettleDetailMapper.selectList(
                    Wrappers.<WholesalePayableSettleDetail>lambdaQuery().eq(WholesalePayableSettleDetail::getSettleNo, payableSettle.getSettleNo()));
            return details.stream().map(detail -> {
                MerCodeAndNameDTO merCodeAndNameDTO = new MerCodeAndNameDTO();
                merCodeAndNameDTO.setMerCode(detail.getMerCode());
                merCodeAndNameDTO.setMerName(detail.getMerName());
                return merCodeAndNameDTO;
            }).collect(Collectors.collectingAndThen(Collectors.toCollection(() ->
                    new TreeSet<>(Comparator.comparing(MerCodeAndNameDTO::getMerCode))), ArrayList::new));
        }
        return null;
    }

    @Override
    public List<StoreCodeAndNameDTO> storesByMerCode(String supplierMerCode, String merCode) {
        PlatformWholesalePayableDetailQuery detailQuery = new PlatformWholesalePayableDetailQuery();
        detailQuery.setMerCode(supplierMerCode);
        detailQuery.setCustomerMerCode(merCode);
        List<PlatformWholesaleSettleDetailDTO> details = wholesalePayableSettleDetailMapper.queryPagePayableDetails(detailQuery);
        return details.stream().map(detail -> {
            StoreCodeAndNameDTO storeCodeAndNameDTO = new StoreCodeAndNameDTO();
            storeCodeAndNameDTO.setStoreCode(detail.getStoreCode());
            storeCodeAndNameDTO.setStoreName(detail.getStoreName());
            return storeCodeAndNameDTO;
        }).collect(Collectors.collectingAndThen(Collectors.toCollection(() ->
                new TreeSet<>(Comparator.comparing(StoreCodeAndNameDTO::getStoreCode))), ArrayList::new));
    }

    @Override
    public List<MerCodeAndNameDTO> customerMersByMerCode(String supplierMerCode) {
        PlatformWholesalePayableDetailQuery detailQuery = new PlatformWholesalePayableDetailQuery();
        detailQuery.setMerCode(supplierMerCode);
        List<PlatformWholesaleSettleDetailDTO> details = wholesalePayableSettleDetailMapper.queryPagePayableDetails(detailQuery);
        return details.stream().map(detail -> {
            MerCodeAndNameDTO merCodeAndNameDTO = new MerCodeAndNameDTO();
            merCodeAndNameDTO.setMerCode(detail.getCustomerMerCode());
            merCodeAndNameDTO.setMerName(detail.getCustomerMerName());
            return merCodeAndNameDTO;
        }).collect(Collectors.collectingAndThen(Collectors.toCollection(() ->
                new TreeSet<>(Comparator.comparing(MerCodeAndNameDTO::getMerCode))), ArrayList::new));
    }

    private WholesalePaySettleDetailQuery getWholesalePaySettleDetailQuery(Long id, WholesalePaySettleDetailReq query) {
        WholesalePayableSettle payableSettle = wholesalePayableSettleMapper.selectById(id);
        BizAssert.notNull(payableSettle, ExceptionCode.DATA_NOT_EXIST, "结算单不存在");
        WholesalePaySettleDetailQuery paySettleDetailQuery = new WholesalePaySettleDetailQuery();
        BeanUtils.copyProperties(query, paySettleDetailQuery);
        paySettleDetailQuery.setSettleNo(payableSettle.getSettleNo());
        return paySettleDetailQuery;
    }
}
