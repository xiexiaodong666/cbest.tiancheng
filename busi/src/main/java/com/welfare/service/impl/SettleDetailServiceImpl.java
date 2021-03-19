package com.welfare.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.welfare.common.base.BasePageVo;
import com.welfare.common.constants.WelfareConstant;
import com.welfare.common.constants.WelfareSettleConstant;
import com.welfare.common.exception.BizException;
import com.welfare.common.exception.ExceptionCode;
import com.welfare.persist.dao.MerchantDao;
import com.welfare.persist.dao.MerchantStoreRelationDao;
import com.welfare.persist.dao.SettleDetailDao;
import com.welfare.persist.dao.SupplierStoreDao;
import com.welfare.persist.dto.*;
import com.welfare.persist.dto.query.MerTransDetailQuery;
import com.welfare.persist.dto.query.ProprietaryConsumePageQuery;
import com.welfare.persist.dto.query.WelfareSettleDetailQuery;
import com.welfare.persist.dto.query.WelfareSettleQuery;
import com.welfare.persist.entity.*;
import com.welfare.persist.mapper.MerchantBillDetailMapper;
import com.welfare.persist.mapper.MerchantCreditMapper;
import com.welfare.persist.mapper.MonthSettleMapper;
import com.welfare.persist.mapper.SettleDetailMapper;
import com.welfare.service.MerchantAccountTypeService;
import com.welfare.service.MerchantStoreRelationService;
import com.welfare.service.SettleDetailService;
import com.welfare.service.dto.*;
import com.welfare.service.dto.proprietary.ProprietaryConsumePageReq;
import com.welfare.service.operator.merchant.RebateLimitOperator;
import com.welfare.service.operator.merchant.domain.MerchantAccountOperation;
import com.welfare.service.utils.PageUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author qiang.deng
 * @version 1.0.0
 * @date 2021/1/16 10:44 上午
 * @desc
 */
@Service
@Slf4j
public class SettleDetailServiceImpl implements SettleDetailService {

    @Autowired
    private SettleDetailMapper settleDetailMapper;

    @Autowired
    private SettleDetailDao settleDetailDao;

    @Autowired
    private MonthSettleMapper monthSettleMapper;

    @Autowired
    private MerchantCreditMapper merchantCreditMapper;

    @Autowired
    private MerchantBillDetailMapper merchantBillDetailMapper;
    @Autowired
    private MerchantStoreRelationDao merchantStoreRelationDao;
    @Value("${pos.onlines:1001}")
    private String posOnlines;
    @Autowired
    private RebateLimitOperator rebateLimitOperator;
    @Autowired
    private SupplierStoreDao supplierStoreDao;
    @Autowired
    private MerchantAccountTypeService accountTypeService;
    @Autowired
    private MerchantStoreRelationService merchantStoreRelationService;

    @Autowired
    private MerchantDao merchantDao;

    @Override
    public WelfareSettleSumDTO queryWelfareSettleSum(WelfareSettleQuery welfareSettleQuery){
        return settleDetailMapper.getWelfareSettleAllMerchant(welfareSettleQuery);
    }
    @Override
    public BasePageVo<WelfareSettleResp> queryWelfareSettlePage(WelfareSettlePageReq welfareSettlePageReq) {
        WelfareSettleQuery welfareSettleQuery = new WelfareSettleQuery();
        BeanUtils.copyProperties(welfareSettlePageReq, welfareSettleQuery);
        PageInfo<WelfareSettleResp> welfareSettleDTOPageInfo = PageHelper.startPage(welfareSettlePageReq.getCurrent(), welfareSettlePageReq.getSize())
                .doSelectPageInfo(() -> {
                    settleDetailMapper.getWelfareSettle(welfareSettleQuery).stream().map(welfareSettleDTO -> {
                        WelfareSettleResp welfareSettleResp = new WelfareSettleResp();
                        BeanUtils.copyProperties(welfareSettleDTO, welfareSettleResp);
                        return welfareSettleResp;
                    }).collect(Collectors.toList());
                });

        BasePageVo<WelfareSettleResp> welfareSettleRespBasePageVo = new BasePageVo<>(welfareSettlePageReq.getCurrent(),
                welfareSettlePageReq.getSize(), welfareSettleDTOPageInfo.getTotal(), welfareSettleDTOPageInfo.getList());

        return welfareSettleRespBasePageVo;
    }

    @Override
    public List<WelfareSettleDetailResp> queryWelfareSettleDetail(WelfareSettleDetailReq welfareSettleDetailReq) {
        WelfareSettleDetailQuery welfareSettleDetailQuery = new WelfareSettleDetailQuery();
        BeanUtils.copyProperties(welfareSettleDetailReq, welfareSettleDetailQuery);
        welfareSettleDetailQuery.setPosOnlines(posOnlines);
        welfareSettleDetailQuery.setLimit(WelfareSettleConstant.LIMIT);
        List<WelfareSettleDetailResp> welfareSettleDetailRespList = settleDetailMapper.getSettleDetailInfo(welfareSettleDetailQuery).stream().map(welfareSettleDetailDTO -> {
            WelfareSettleDetailResp welfareSettleDetailResp = new WelfareSettleDetailResp();
            BeanUtils.copyProperties(welfareSettleDetailDTO, welfareSettleDetailResp);
            return welfareSettleDetailResp;
        }).collect(Collectors.toList());

        return welfareSettleDetailRespList;
    }
    @Override
    public WelfareSettleSummaryDTO queryWelfareSettleDetailSummary(WelfareSettleDetailReq welfareSettleDetailReq) {
        WelfareSettleDetailQuery welfareSettleDetailQuery = new WelfareSettleDetailQuery();
        BeanUtils.copyProperties(welfareSettleDetailReq, welfareSettleDetailQuery);
        WelfareSettleSummaryDTO welfareSettleSummaryDTO = settleDetailMapper.getSettleDetailInfoSummary(welfareSettleDetailQuery);
        if(Objects.isNull(welfareSettleSummaryDTO)){
            welfareSettleSummaryDTO = new WelfareSettleSummaryDTO();
            Merchant merchant = merchantDao.queryByCode(welfareSettleDetailReq.getMerCode());
            welfareSettleSummaryDTO.setMerCooperationMode(merchant.getMerCooperationMode());
            welfareSettleSummaryDTO.setBalanceConsumeAmount(BigDecimal.ZERO);
            welfareSettleSummaryDTO.setMerName(welfareSettleDetailReq.getMerCode());
            welfareSettleSummaryDTO.setOfflineConsumeAmount(BigDecimal.ZERO);
            welfareSettleSummaryDTO.setOnlineConsumeAmount(BigDecimal.ZERO);
            welfareSettleSummaryDTO.setBalanceConsumeAmount(BigDecimal.ZERO);
            welfareSettleSummaryDTO.setTotalConsumeAmount(BigDecimal.ZERO);
            welfareSettleSummaryDTO.setUnsettledAmount(BigDecimal.ZERO);
        }
        return welfareSettleSummaryDTO;
    }

    @Override
    public BasePageVo<WelfareSettleDetailResp> queryWelfareSettleDetailPage(WelfareSettleDetailPageReq welfareSettleDetailPageReq) {
        String merCode = welfareSettleDetailPageReq.getMerCode();

        if(StringUtils.isBlank(merCode)){
            throw new BizException(ExceptionCode.ILLEGALITY_ARGURMENTS, "商户编号不能为空", null);
        }

        WelfareSettleDetailQuery welfareSettleDetailQuery = new WelfareSettleDetailQuery();
        BeanUtils.copyProperties(welfareSettleDetailPageReq, welfareSettleDetailQuery);
        welfareSettleDetailQuery.setPosOnlines(posOnlines);

        PageInfo<WelfareSettleDetailResp> welfareSettleDetailRespPageInfo = PageHelper.startPage(welfareSettleDetailPageReq.getCurrent(), welfareSettleDetailPageReq.getSize())
                .doSelectPageInfo(() -> {
                    settleDetailMapper.getSettleDetailInfo(welfareSettleDetailQuery).stream().map(welfareSettleDetailDTO -> {
                        WelfareSettleDetailResp welfareSettleDetailResp = new WelfareSettleDetailResp();
                        BeanUtils.copyProperties(welfareSettleDetailDTO, welfareSettleDetailResp);
                        return welfareSettleDetailResp;
                    }).collect(Collectors.toList());
                });

        Map<String, Object> extInfo = queryWelfareSettleDetailExt(welfareSettleDetailPageReq);

        BasePageVo<WelfareSettleDetailResp> welfareSettleDetailRespBasePageVo = new BasePageVo<>(welfareSettleDetailPageReq.getCurrent(), welfareSettleDetailPageReq.getSize(),
                welfareSettleDetailRespPageInfo.getTotal(), welfareSettleDetailRespPageInfo.getList(), extInfo);

        return welfareSettleDetailRespBasePageVo;
    }

    @Override
    public Map<String, Object> queryWelfareSettleDetailExt(WelfareSettleDetailPageReq welfareSettleDetailPageReq) {
        WelfareSettleDetailQuery welfareSettleDetailQuery = new WelfareSettleDetailQuery();
        BeanUtils.copyProperties(welfareSettleDetailPageReq, welfareSettleDetailQuery);
        welfareSettleDetailQuery.setPosOnlines(posOnlines);
        Map<String, Object> extInfo = settleDetailMapper.getSettleDetailExtInfo(welfareSettleDetailQuery);
        return extInfo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void buildSettle(WelfareSettleDetailReq welfareSettleDetailReq) {
        WelfareSettleDetailQuery welfareSettleDetailQuery = new WelfareSettleDetailQuery();
        BeanUtils.copyProperties(welfareSettleDetailReq, welfareSettleDetailQuery);
        welfareSettleDetailQuery.setPosOnlines(posOnlines);
        MonthSettle monthSettle = settleDetailMapper.getSettleByCondition(welfareSettleDetailQuery);
        if(Objects.isNull(monthSettle)){
            throw new BizException(ExceptionCode.ILLEGALITY_ARGURMENTS, "构选的消费明细正在结算中或结算已完成。", null);
        }
        monthSettle.setSettleStatus(WelfareSettleConstant.SettleStatusEnum.SETTLING.code());
        if(monthSettle.getSettleAmount().compareTo(new BigDecimal(0)) < 0){
            throw new BizException(ExceptionCode.ILLEGALITY_ARGURMENTS, "结算金额为负，无法生成结算单", null);
        }

        List<SettleStatisticsInfoDTO> settleStatisticsInfoDTOList = settleDetailMapper.getSettleStatisticsInfoByCondition(welfareSettleDetailQuery);
        if (!settleStatisticsInfoDTOList.isEmpty()) {
            monthSettle.setSettleStatisticsInfo(JSONObject.toJSONString(settleStatisticsInfoDTOList));
        }

        monthSettleMapper.insert(monthSettle);

        welfareSettleDetailQuery.setLimit(WelfareSettleConstant.LIMIT);
        List<Long> idList;
        do {
            idList = settleDetailMapper.getSettleDetailIdList(welfareSettleDetailQuery);

            if (!idList.isEmpty()) {
                SettleDetail settleDetail = new SettleDetail();
                settleDetail.setSettleNo(monthSettle.getSettleNo());
                settleDetail.setSettleFlag(WelfareSettleConstant.SettleStatusEnum.SETTLING.code());
                settleDetailMapper.update(settleDetail, Wrappers.<SettleDetail>lambdaUpdate()
                        .in(SettleDetail::getId, idList));
            } else {
                break;
            }
        } while (true);
    }

    @Override
    public SettleMerInfoResp getMerAccountInfo(String merCode) {
        MerchantCredit merchantCredit = merchantCreditMapper.selectOne(Wrappers.<MerchantCredit>lambdaQuery()
                .eq(MerchantCredit::getMerCode, merCode)
                .eq(MerchantCredit::getDeleted, 0)
        );
        SettleMerInfoResp settleMerInfoResp = new SettleMerInfoResp();
        settleMerInfoResp.setMerCode(merCode);
        settleMerInfoResp.setCreditLimit(merchantCredit.getCreditLimit());
        settleMerInfoResp.setTransAmount(merchantCredit.getCurrentBalance());
        settleMerInfoResp.setRebateLimit(merchantCredit.getRebateLimit());
        settleMerInfoResp.setRemainingLimit(merchantCredit.getRemainingLimit());
        settleMerInfoResp.setRechargeLimit(merchantCredit.getRechargeLimit());
        return settleMerInfoResp;
    }

    @Override
    public BasePageVo<SettleMerTransDetailResp> getMerAccountTransPageDetail(String merCode, SettleMerTransDetailPageReq settleMerTransDetailReq) {
        MerTransDetailQuery merTransDetailQuery = new MerTransDetailQuery();
        BeanUtils.copyProperties(settleMerTransDetailReq, merTransDetailQuery);
        merTransDetailQuery.setMerCode(merCode);

        PageInfo<SettleMerTransDetailResp> selectPageInfo = PageHelper
                .startPage(settleMerTransDetailReq.getCurrent(), settleMerTransDetailReq.getSize())
                .doSelectPageInfo(() -> {
                    merchantBillDetailMapper.getMerTransDetail(merTransDetailQuery).stream()
                            .map(merTransDetailDTO -> {
                                SettleMerTransDetailResp settleMerTransDetailRespTemp = new SettleMerTransDetailResp();
                                BeanUtils.copyProperties(merTransDetailDTO, settleMerTransDetailReq);
                                return settleMerTransDetailRespTemp;
                            }).collect(Collectors.toList());
                });
        return new BasePageVo<>(settleMerTransDetailReq.getCurrent(), settleMerTransDetailReq.getSize(),
                selectPageInfo.getTotal(), selectPageInfo.getList());
    }

    @Override
    public List<SettleMerTransDetailResp> getMerAccountTransDetail(String merCode, SettleMerTransDetailReq settleMerTransDetailReq) {
        MerTransDetailQuery merTransDetailQuery = new MerTransDetailQuery();
        BeanUtils.copyProperties(settleMerTransDetailReq, merTransDetailQuery);
        merTransDetailQuery.setMerCode(merCode);


        return merchantBillDetailMapper.getMerTransDetail(merTransDetailQuery).stream()
                .map(merTransDetailDTO -> {
                    SettleMerTransDetailResp settleMerTransDetailRespTemp = new SettleMerTransDetailResp();
                    BeanUtils.copyProperties(merTransDetailDTO, settleMerTransDetailRespTemp);
                    return settleMerTransDetailRespTemp;
                }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void merRebate(Merchant merchant) {
        //查询该商户返点信息

        //修改数据
    }

    private SettleDetail calculateAndSetRebate(SettleDetail settleDetail) {
        String storeCode = settleDetail.getStoreCode();
        String merCode = settleDetail.getMerCode();
        MerchantStoreRelation relation = merchantStoreRelationDao.getOneByStoreCodeAndMerCodeCacheable(storeCode, merCode);

        // jian.zhou 2021-01-24
        // 处理未配置的商户和门店的情况
        if (relation == null){
            return settleDetail;
        }

        //自营不返利
        SupplierStore supplierStore = supplierStoreDao.getOne(Wrappers.<SupplierStore>lambdaQuery().eq(SupplierStore::getStoreCode, settleDetail.getStoreCode()));
        if(StringUtils.isNotBlank(relation.getMerCode()) && supplierStore.getMerCode().equals(settleDetail.getMerCode())){
            return settleDetail;
        }

        String rebateType = relation.getRebateType();
        if (Strings.isEmpty(rebateType)) {
            return settleDetail;
        }
        List<String> payCodes = Arrays.asList(rebateType.split(","));
        if (payCodes.contains(settleDetail.getPayCode())) {
            Assert.notNull(relation.getRebateRatio(), "配置了返利类型，没有返利比率,id:" + relation.getId());
            // transAmount  * (ratio/100) 4位小数，四舍五入
            BigDecimal rebateAmount = settleDetail.getTransAmount()
                    .multiply(
                            relation.getRebateRatio().divide(BigDecimal.valueOf(100),
                                    4,
                                    RoundingMode.HALF_UP
                            ),
                            new MathContext(4, RoundingMode.HALF_UP)
                    );

            if (WelfareConstant.TransType.CONSUME.code().equals(settleDetail.getTransType())) {
                //消费，正向返利
                settleDetail.setRebateAmount(rebateAmount);
            }else if(WelfareConstant.TransType.REFUND.code().equals(settleDetail.getTransType())){
                //退款，逆向返利
                settleDetail.setRebateAmount(rebateAmount.negate());
            }
            return settleDetail;
        }

        return settleDetail;

    }

    @Override
    public List<MerchantBillDetail> calculateAndSetRebate(MerchantCredit merchantCredit, List<SettleDetail> settleDetails) {
        return settleDetails.stream().sorted(Comparator.comparing(SettleDetail::getTransTime))
                .map(detail -> {
                    SettleDetail settleDetail = calculateAndSetRebate(detail);
                    if (Objects.isNull(detail.getRebateAmount()) || detail.getRebateAmount().equals(BigDecimal.ZERO)) {
                        //计算返利后，需要返利为0
                        return null;
                    }
                    if(WelfareConstant.TransType.CONSUME.code().equals(settleDetail.getTransType())){
                        return rebateLimitOperator.increase(
                                merchantCredit,
                                settleDetail.getRebateAmount().abs(),
                                settleDetail.getTransNo(),
                                WelfareConstant.TransType.REBATE_INCR.code()
                        );
                    } else if (WelfareConstant.TransType.REFUND.code().equals(settleDetail.getTransType())){
                        return rebateLimitOperator.decrease(
                                merchantCredit,
                                settleDetail.getRebateAmount().abs(),
                                settleDetail.getTransNo(),
                                WelfareConstant.TransType.REBATE_DECR.code()
                        );
                    } else{
                        throw new BizException(ExceptionCode.ILLEGALITY_ARGURMENTS,"计算返利时，数据异常",null);
                    }
                }).filter(operations -> !Objects.isNull(operations))
                .flatMap(Collection::stream)
                .map(MerchantAccountOperation::getMerchantBillDetail)
                .collect(Collectors.toList());
    }

    @Override
    public Page<ProprietaryConsumeResp> queryProprietaryConsumePage(ProprietaryConsumePageReq welfareSettleDetailPageReq) {
        String merCode = welfareSettleDetailPageReq.getMerCode();
        if(StringUtils.isBlank(merCode)){
            throw new BizException(ExceptionCode.ILLEGALITY_ARGURMENTS, "商户编号不能为空", null);
        }
        ProprietaryConsumePageQuery proprietaryConsumePageQuery = new ProprietaryConsumePageQuery();
        BeanUtils.copyProperties(welfareSettleDetailPageReq, proprietaryConsumePageQuery);
        proprietaryConsumePageQuery.setStoreType("self");

        Page<ProprietaryConsumeDTO> page = new Page<>();
        page.setCurrent(welfareSettleDetailPageReq.getCurrent());
        page.setSize(welfareSettleDetailPageReq.getSize());
        settleDetailMapper.queryProprietaryConsumeInfo(page, proprietaryConsumePageQuery);
        List<ProprietaryConsumeDTO> dtos = page.getRecords();
        List<ProprietaryConsumeResp> resps = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(dtos)) {
            dtos.forEach(proprietaryConsumeDTO -> {
                ProprietaryConsumeResp resp = new ProprietaryConsumeResp();
                BeanUtils.copyProperties(proprietaryConsumeDTO, resp);
                resps.add(resp);
            });
        }
        return PageUtils.toPage(page, resps);
    }

    @Override
    public List<ProprietaryConsumeResp> queryProprietaryConsume(ProprietaryConsumePageReq welfareSettleDetailPageReq) {
        String merCode = welfareSettleDetailPageReq.getMerCode();
        if(StringUtils.isBlank(merCode)){
            throw new BizException(ExceptionCode.ILLEGALITY_ARGURMENTS, "商户编号不能为空", null);
        }
        ProprietaryConsumePageQuery proprietaryConsumePageQuery = new ProprietaryConsumePageQuery();
        BeanUtils.copyProperties(welfareSettleDetailPageReq, proprietaryConsumePageQuery);
        proprietaryConsumePageQuery.setStoreType("self");

        List<ProprietaryConsumeDTO> dtos =  settleDetailMapper.queryProprietaryConsumeInfo(proprietaryConsumePageQuery);
        List<ProprietaryConsumeResp> resps = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(dtos)) {
            dtos.forEach(proprietaryConsumeDTO -> {
                ProprietaryConsumeResp resp = new ProprietaryConsumeResp();
                BeanUtils.copyProperties(proprietaryConsumeDTO, resp);
                //resp.setPhone(AccountUtil.desensitizedPhoneNumber(resp.getPhone()));
                resps.add(resp);
            });
        }
        return resps;
    }

    @Override
    public List<WelfareTypeTotalAmountResp> statisticalAmountGroupByWelfareTypeCode(ProprietaryConsumePageReq welfareSettleDetailPageReq) {
        String merCode = welfareSettleDetailPageReq.getMerCode();
        if(StringUtils.isBlank(merCode)){
            throw new BizException(ExceptionCode.ILLEGALITY_ARGURMENTS, "商户编号不能为空", null);
        }
        ProprietaryConsumePageQuery proprietaryConsumePageQuery = new ProprietaryConsumePageQuery();
        BeanUtils.copyProperties(welfareSettleDetailPageReq, proprietaryConsumePageQuery);
        proprietaryConsumePageQuery.setStoreType("self");
        List<WelfareTypeTotalAmountResp> typeTotalAmounts = new ArrayList<>();
        // 查询商户下所有的余额类型
        List<MerchantAccountType> accountTypeList = accountTypeService.queryAllByMerCode(merCode);
        if (CollectionUtils.isNotEmpty(accountTypeList)) {
            List<String> typeCodes = accountTypeList.stream().map(MerchantAccountType::getMerAccountTypeCode).collect(Collectors.toList());
            // 根据余额类型分组统计金额
            List<WelfareTypeTotalAmountDTO> amountDTOS = settleDetailMapper.statisticalAmountGroupByWelfareTypeCode(proprietaryConsumePageQuery);
            Map<String,WelfareTypeTotalAmountDTO> amountDTOMap = new HashMap<>();
            BigDecimal totalAmount = BigDecimal.ZERO;
            if (CollectionUtils.isNotEmpty(amountDTOS)) {
                amountDTOMap = amountDTOS.stream().collect(Collectors.toMap(WelfareTypeTotalAmountDTO::getType, a -> a,(k1,k2)->k1));
                totalAmount = amountDTOS.stream().map(WelfareTypeTotalAmountDTO::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            }
            Map<String, WelfareTypeTotalAmountDTO> finalAmountDTOMap = amountDTOMap;
            accountTypeList.forEach(merchantAccountType -> {
                WelfareTypeTotalAmountResp amountResp = new WelfareTypeTotalAmountResp();
                amountResp.setType(merchantAccountType.getMerAccountTypeCode());
                amountResp.setTypeName(merchantAccountType.getMerAccountTypeName());
                amountResp.setAmount(BigDecimal.ZERO);
                if (finalAmountDTOMap.containsKey(merchantAccountType.getMerAccountTypeCode())) {
                    amountResp.setAmount(finalAmountDTOMap.get(merchantAccountType.getMerAccountTypeCode()).getAmount());
                }
                typeTotalAmounts.add(amountResp);
            });
            // 计算总金额
            WelfareTypeTotalAmountResp totalAmountResp = new WelfareTypeTotalAmountResp();
            totalAmountResp.setTypeName("消费总金额");
            totalAmountResp.setAmount(totalAmount);
            typeTotalAmounts.add(totalAmountResp);
        }
        return typeTotalAmounts;
    }
}
