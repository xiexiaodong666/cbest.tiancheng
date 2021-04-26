package com.welfare.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.welfare.common.base.BasePageVo;
import com.welfare.common.constants.WelfareSettleConstant;
import com.welfare.common.domain.MerchantUserInfo;
import com.welfare.common.domain.UserInfo;
import com.welfare.common.exception.BizException;
import com.welfare.common.exception.ExceptionCode;
import com.welfare.common.util.DateUtil;
import com.welfare.common.util.MerchantUserHolder;
import com.welfare.common.util.UserInfoHolder;
import com.welfare.persist.dao.MonthSettleDao;
import com.welfare.persist.dao.PaymentChannelDao;
import com.welfare.persist.dao.SettleDetailDao;
import com.welfare.persist.dao.SupplierStoreDao;
import com.welfare.persist.dto.*;
import com.welfare.persist.dto.query.MonthSettleDetailQuery;
import com.welfare.persist.dto.query.MonthSettleQuery;
import com.welfare.persist.entity.MonthSettle;
import com.welfare.persist.entity.PaymentChannel;
import com.welfare.persist.entity.SettleDetail;
import com.welfare.persist.mapper.MonthSettleMapper;
import com.welfare.persist.mapper.SettleDetailMapper;
import com.welfare.service.MonthSettleService;
import com.welfare.service.dto.*;
import com.welfare.service.remote.MerchantCreditFeign;
import com.welfare.service.remote.entity.MerchantCreditResp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 月度结算账单服务接口实现
 *
 * @author Yuxiang Li
 * @since 2021-01-06 13:49:25
 * @description 由 Mybatisplus Code Generator 创建
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class MonthSettleServiceImpl implements MonthSettleService {

    @Autowired
    private final MonthSettleMapper monthSettleMapper;
    @Autowired
    private final SettleDetailMapper settleDetailMapper;
    @Autowired
    private final MonthSettleDao monthSettleDao;
    @Value("${pos.onlines:1001}")
    private String posOnlines;
    @Autowired(required = false)
    private MerchantCreditFeign merchantCreditFeign;
    @Autowired
    private SettleDetailDao settleDetailDao;
    @Autowired
    private SupplierStoreDao supplierStoreDao;
    @Autowired
    private PaymentChannelDao paymentChannelDao;


    @Override
    public BasePageVo<MonthSettleResp> pageQuery(MonthSettlePageReq monthSettleReqDto) {

        MonthSettleQuery monthSettleQuery = new MonthSettleQuery();
        BeanUtils.copyProperties(monthSettleReqDto, monthSettleQuery);

        //传入的日期，修订未当日最大和当日最小值
        if(monthSettleReqDto.getStartDay()!=null){
            monthSettleQuery.setStartTime(DateUtil.getDayMin(monthSettleReqDto.getStartDay(), 0));
        }
        if(monthSettleReqDto.getEndDay()!=null){
            monthSettleQuery.setEndTime(DateUtil.getDayMax(monthSettleReqDto.getEndDay(), 0));
        }


        PageInfo<MonthSettleDTO> monthSettleDTOPageInfo = PageHelper.startPage(monthSettleReqDto.getCurrent(),monthSettleReqDto.getSize())
                .doSelectPageInfo(() -> monthSettleMapper.selectMonthSettle(monthSettleQuery));

        BasePageVo<MonthSettleResp> monthSettleRespPage = new BasePageVo<>(monthSettleReqDto.getCurrent(), monthSettleReqDto.getSize(),monthSettleDTOPageInfo.getTotal());

        if(monthSettleDTOPageInfo.getList().isEmpty()){
            return monthSettleRespPage;
        }

        monthSettleRespPage.setRecords(monthSettleDTOPageInfo.getList().stream().map(monthSettleDTO -> {
            MonthSettleResp monthSettleResp = new MonthSettleResp();
            BeanUtils.copyProperties(monthSettleDTO, monthSettleResp);
            String settleStatisticsInfo = monthSettleDTO.getSettleStatisticsInfo();
            if(StringUtils.isNotBlank(settleStatisticsInfo)){
                List<SettleStatisticsInfoDTO> settleAccountInfos = JSON.parseArray(settleStatisticsInfo, SettleStatisticsInfoDTO.class);
                monthSettleResp.setSettleStatisticsInfoList(settleAccountInfos);
            }
            return monthSettleResp;
        }).collect(Collectors.toList()));


        return monthSettleRespPage;
    }

    @Override
    public MonthSettleResp queryById(Long id) {

        MonthSettleQuery monthSettleQuery = new MonthSettleQuery();
        monthSettleQuery.setId(id);

        List<MonthSettleDTO> monthSettleDTOS = monthSettleMapper.selectMonthSettle(monthSettleQuery);

        List<MonthSettleResp> collect = monthSettleDTOS.stream().map(monthSettleDTO -> {
            MonthSettleResp monthSettleResp = new MonthSettleResp();
            BeanUtils.copyProperties(monthSettleDTO, monthSettleResp);
            String settleStatisticsInfo = monthSettleDTO.getSettleStatisticsInfo();
            if (StringUtils.isNotBlank(settleStatisticsInfo)) {
                List<SettleStatisticsInfoDTO> settleAccountInfos = JSON.parseArray(settleStatisticsInfo, SettleStatisticsInfoDTO.class);
                monthSettleResp.setSettleStatisticsInfoList(settleAccountInfos);
            }
            return monthSettleResp;
        }).collect(Collectors.toList());

        if(!monthSettleDTOS.isEmpty()){
            return collect.get(0);
        }else{
            return null;
        }
    }

    @Override
    public BasePageVo<MonthSettleDetailResp> pageQueryMonthSettleDetail(Long id, MonthSettleDetailPageReq monthSettleDetailPageReq) {
        MonthSettleDetailReq monthSettleDetailReq = new MonthSettleDetailReq();
        BeanUtils.copyProperties(monthSettleDetailPageReq, monthSettleDetailReq);

        MonthSettleDetailQuery monthSettleDetailQuery = getMonthSettleDetailQuery(id, monthSettleDetailReq);

        Map<String, PaymentChannel> paymentChannelMap = paymentChannelDao.allMap();
        PageInfo<MonthSettleDetailResp> monthSettleDetailDTOPageInfo = PageHelper.startPage(monthSettleDetailPageReq.getCurrent(), monthSettleDetailPageReq.getSize())
                .doSelectPageInfo(() -> {
                    settleDetailMapper.selectMonthSettleDetail(monthSettleDetailQuery).stream().map(monthSettleDetailDTO -> {
                                MonthSettleDetailResp monthSettleDetailResp = new MonthSettleDetailResp();
                                BeanUtils.copyProperties(monthSettleDetailDTO, monthSettleDetailResp);
                                if (paymentChannelMap.containsKey(monthSettleDetailDTO.getPaymentChannel())) {
                                    monthSettleDetailResp.setPaymentChannel(paymentChannelMap.get(monthSettleDetailDTO.getPaymentChannel()).getName());
                                    monthSettleDetailDTO.setPaymentChannel(paymentChannelMap.get(monthSettleDetailDTO.getPaymentChannel()).getName());
                                }
                                return monthSettleDetailResp;
                    }).collect(Collectors.toList());
                });

        BasePageVo<MonthSettleDetailResp> monthSettleDetailRespPage = new BasePageVo<>(monthSettleDetailPageReq.getCurrent(),
                monthSettleDetailPageReq.getSize(),monthSettleDetailDTOPageInfo.getTotal(), monthSettleDetailDTOPageInfo.getList());
        return monthSettleDetailRespPage;
    }

    @Override
    public MonthSettleDetailSummaryDTO monthSettleDetailSummary(Long id, MonthSettleDetailReq monthSettleDetailReq) {
        MonthSettleDetailQuery monthSettleDetailQuery = getMonthSettleDetailQuery(id, monthSettleDetailReq);
        return settleDetailMapper.selectMonthSettleDetailSummary(monthSettleDetailQuery);
    }

    @Override
    public List<MonthSettleDetailResp> queryMonthSettleDetailLimit(Long id, MonthSettleDetailReq monthSettleDetailReq) {

        MonthSettleDetailQuery monthSettleDetailQuery = getMonthSettleDetailQuery(id, monthSettleDetailReq);

        monthSettleDetailQuery.setLimit(WelfareSettleConstant.LIMIT);
        List<MonthSettleDetailDTO> monthSettleDetailDTOS = settleDetailMapper.selectMonthSettleDetail(monthSettleDetailQuery);
        Map<String, PaymentChannel> paymentChannelMap = paymentChannelDao.allMap();
        List<MonthSettleDetailResp> monthSettleDetailResps = monthSettleDetailDTOS.stream().map(monthSettleDetailDTO -> {
            MonthSettleDetailResp monthSettleDetailResp = new MonthSettleDetailResp();
            BeanUtils.copyProperties(monthSettleDetailDTO, monthSettleDetailResp);
            if (paymentChannelMap.containsKey(monthSettleDetailDTO.getPaymentChannel())) {
                monthSettleDetailResp.setPaymentChannel(paymentChannelMap.get(monthSettleDetailDTO.getPaymentChannel()).getName());
                monthSettleDetailDTO.setPaymentChannel(paymentChannelMap.get(monthSettleDetailDTO.getPaymentChannel()).getName());
            }
            return monthSettleDetailResp;
        }).collect(Collectors.toList());

        return monthSettleDetailResps;
    }

    @Override
    public Integer monthSettleSend(Long id) {
        MonthSettle monthSettle = new MonthSettle();
        //修改账单发送状态为已发送
        monthSettle.setSendStatus(WelfareSettleConstant.SettleSendStatusEnum.SENDED.code());

        monthSettle.setSendTime(new Date());

        UserInfo userInfo = UserInfoHolder.getUserInfo();
        monthSettle.setCreateUser(userInfo.getUserId());

        return monthSettleMapper.update(monthSettle,
                Wrappers.<MonthSettle>lambdaUpdate()
                        .eq(MonthSettle::getSendStatus, WelfareSettleConstant.SettleSendStatusEnum.UNSENDED.code())
                        .eq(MonthSettle::getId, id)
        );
    }

    @Override
    public Integer monthSettleConfirm(Long id) {

        MonthSettle monthSettle = new MonthSettle();

        //修改账单确认状态为已确认
        monthSettle.setRecStatus(WelfareSettleConstant.SettleRecStatusEnum.CONFIRMED.code());

        MerchantUserInfo merchantUser = MerchantUserHolder.getMerchantUser();
        monthSettle.setConfirmTime(new Date());
        monthSettle.setUppdateUser(merchantUser.getUserCode());

        return monthSettleMapper.update(monthSettle,
                Wrappers.<MonthSettle>lambdaUpdate()
                        .eq(MonthSettle::getSendStatus, WelfareSettleConstant.SettleSendStatusEnum.SENDED.code())
                        .eq(MonthSettle::getId, id)
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer monthSettleFinish(Long id) {
        MonthSettle monthSettleTemp = monthSettleMapper.selectById(id);

        //修改账单结算状态为已结算
        MonthSettle monthSettle = new MonthSettle();
        monthSettle.setSettleStatus(WelfareSettleConstant.SettleStatusEnum.SETTLED.code());

        UserInfo userInfo = UserInfoHolder.getUserInfo();
        monthSettle.setUppdateUser(userInfo.getUserId());

        int i = monthSettleMapper.update(monthSettle,
                Wrappers.<MonthSettle>lambdaUpdate()
                        .eq(MonthSettle::getSettleStatus, WelfareSettleConstant.SettleStatusEnum.SETTLING.code())
                        .eq(MonthSettle::getRecStatus, WelfareSettleConstant.SettleRecStatusEnum.CONFIRMED.code())
                        .eq(MonthSettle::getId, id)
        );
        settleDetailDao.updateToSettled(monthSettleTemp.getSettleNo());
        RestoreRemainingLimitReq restoreRemainingLimitReq = new RestoreRemainingLimitReq();
        restoreRemainingLimitReq.setMerCode(monthSettleTemp.getMerCode());
        restoreRemainingLimitReq.setAmount(monthSettleTemp.getSettleAmount());
        restoreRemainingLimitReq.setTransNo(monthSettleTemp.getSettleNo());
        log.info("调用商户服务，恢复商户授信额度，请求参数：{}",JSON.toJSONString(restoreRemainingLimitReq));
        MerchantCreditResp merchantCreditResp = merchantCreditFeign.remainingLimit(restoreRemainingLimitReq, "api");
        if(merchantCreditResp.getCode()!=1){
            throw new BizException(ExceptionCode.UNKNOWN_EXCEPTION, "恢复商户授信额度失败", null);
        }
        return i;
    }

    @Override
    public MonthSettle getMonthSettle(MonthSettleDetailQuery monthSettleDetailQuery) {
        return monthSettleMapper.sumSettleDetailToMonthSettle(monthSettleDetailQuery);
    }

    @Override
    public Boolean addMonthSettleList(List<MonthSettle> monthSettleList) {
        return monthSettleDao.saveBatch(monthSettleList);
    }

    @Override
    public MonthSettle getMonthSettleById(Long id) {
        return monthSettleMapper.selectById(id);
    }

    @Override
    public List<Map<String, Object>> getAccoutType(String merCode) {
        return monthSettleMapper.getAccountType(merCode);
    }

    @Override
    public List<StoreCodeNameDTO> allStoresInMonthSettle(Long id) {
        MonthSettle monthSettle = monthSettleDao.getById(id);
        List<SettleDetail> settleDetails = settleDetailDao.query()
                .select(SettleDetail.STORE_CODE, SettleDetail.STORE_NAME)
                .eq(SettleDetail.SETTLE_NO, monthSettle.getSettleNo())
                .groupBy(SettleDetail.STORE_CODE, SettleDetail.STORE_NAME).list();
        return settleDetails.stream().map(settleDetail -> {
            StoreCodeNameDTO storeCodeNameDTO = new StoreCodeNameDTO();
            storeCodeNameDTO.setStoreName(settleDetail.getStoreName());
            storeCodeNameDTO.setStoreCode(settleDetail.getStoreCode());
            return storeCodeNameDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public List<MonthSettleDetailMerchantSummaryDTO> monthSettleDetailMerchantSummary(Long id, MonthSettleDetailReq monthSettleDetailReq) {
        MonthSettleDetailQuery monthSettleDetailQuery = getMonthSettleDetailQuery(id, monthSettleDetailReq);
        List<MonthSettleDetailMerchantSummaryDTO> monthSettleDetailMerchantSummaryDTOS = settleDetailMapper.sumSettleDetailGroupByMerAccountType(monthSettleDetailQuery);
        if(!CollectionUtils.isEmpty(monthSettleDetailMerchantSummaryDTOS)){
            //沃支付的流水，没有福利类型，不用汇总返回
            monthSettleDetailMerchantSummaryDTOS = monthSettleDetailMerchantSummaryDTOS.stream()
                    .filter(dto -> StringUtils.isNotEmpty(dto.getMerAccountType())).collect(Collectors.toList());
        }
        return monthSettleDetailMerchantSummaryDTOS;
    }

    /**
     * 根据账单编号及查询参数，获取查询账单明细限制查询条件
     * @param id 账单编号
     * @param monthSettleDetailReq
     * @return
     */
    private MonthSettleDetailQuery getMonthSettleDetailQuery(Long id, MonthSettleDetailReq monthSettleDetailReq){
        MonthSettle monthSettle = monthSettleMapper.selectById(id);

        if(monthSettle == null){
            throw new BizException(ExceptionCode.ILLEGALITY_ARGUMENTS,"参数异常，未获取到账单信息", null);
        }

        MonthSettleDetailQuery monthSettleDetailQuery = new MonthSettleDetailQuery();
        BeanUtils.copyProperties(monthSettleDetailReq, monthSettleDetailQuery);

        monthSettleDetailQuery.setPosOnlines(posOnlines);
        monthSettleDetailQuery.setSettleNo(monthSettle.getSettleNo());
        return monthSettleDetailQuery;
    }
}