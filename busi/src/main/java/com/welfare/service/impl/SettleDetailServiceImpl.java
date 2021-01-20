package com.welfare.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.welfare.common.base.BasePageVo;
import com.welfare.common.constants.WelfareSettleConstant;
import com.welfare.persist.dao.SettleDetailDao;
import com.welfare.persist.dto.SettleStatisticsInfoDTO;
import com.welfare.persist.dto.query.MerTransDetailQuery;
import com.welfare.persist.dto.query.WelfareSettleDetailQuery;
import com.welfare.persist.dto.query.WelfareSettleQuery;
import com.welfare.persist.entity.Merchant;
import com.welfare.persist.entity.MerchantCredit;
import com.welfare.persist.entity.MonthSettle;
import com.welfare.persist.entity.SettleDetail;
import com.welfare.persist.mapper.MerchantBillDetailMapper;
import com.welfare.persist.mapper.MerchantCreditMapper;
import com.welfare.persist.mapper.MonthSettleMapper;
import com.welfare.persist.mapper.SettleDetailMapper;
import com.welfare.service.SettleDetailService;
import com.welfare.service.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
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

    @Value("${pos.onlines:1001}")
    private String posOnlines;


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

        List<WelfareSettleDetailResp> welfareSettleDetailRespList = settleDetailMapper.getSettleDetailInfo(welfareSettleDetailQuery).stream().map(welfareSettleDetailDTO -> {
            WelfareSettleDetailResp welfareSettleDetailResp = new WelfareSettleDetailResp();
            BeanUtils.copyProperties(welfareSettleDetailDTO, welfareSettleDetailResp);
            return welfareSettleDetailResp;
        }).collect(Collectors.toList());

        return welfareSettleDetailRespList;
    }

    @Override
    public BasePageVo<WelfareSettleDetailResp> queryWelfareSettleDetailPage(WelfareSettleDetailPageReq welfareSettleDetailPageReq) {
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
    @Transactional
    public void buildSettle(WelfareSettleDetailReq welfareSettleDetailReq) {
        WelfareSettleDetailQuery welfareSettleDetailQuery = new WelfareSettleDetailQuery();
        BeanUtils.copyProperties(welfareSettleDetailReq, welfareSettleDetailQuery);
        welfareSettleDetailQuery.setPosOnlines(posOnlines);
        MonthSettle monthSettle = settleDetailMapper.getSettleByCondition(welfareSettleDetailQuery);
        
        List<SettleStatisticsInfoDTO> settleStatisticsInfoDTOList = settleDetailMapper.getSettleStatisticsInfoByCondition(welfareSettleDetailQuery);
        if(!settleStatisticsInfoDTOList.isEmpty()){
            monthSettle.setSettleStatisticsInfo(JSONObject.toJSONString(settleStatisticsInfoDTOList));
        }

        monthSettleMapper.insert(monthSettle);

        welfareSettleDetailQuery.setLimit(WelfareSettleConstant.LIMIT);
        List<Long> idList;
        do {
            idList = settleDetailMapper.getSettleDetailIdList(welfareSettleDetailQuery);

            if(!idList.isEmpty()){
                SettleDetail settleDetail = new SettleDetail();
                settleDetail.setSettleNo(monthSettle.getSettleNo());
                settleDetail.setSettleFlag(WelfareSettleConstant.SettleStatusEnum.SETTLED.code());
                settleDetailMapper.update(settleDetail, Wrappers.<SettleDetail>lambdaUpdate()
                        .in(SettleDetail::getId, idList));
            }else{
                break;
            }
        }while(true);
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


        List<SettleMerTransDetailResp> settleMerTransDetailRespList = merchantBillDetailMapper.getMerTransDetail(merTransDetailQuery).stream()
                .map(merTransDetailDTO -> {
                    SettleMerTransDetailResp settleMerTransDetailRespTemp = new SettleMerTransDetailResp();
                    BeanUtils.copyProperties(merTransDetailDTO, settleMerTransDetailRespTemp);
                    return settleMerTransDetailRespTemp;
                }).collect(Collectors.toList());

        return settleMerTransDetailRespList;
    }

    @Override
    @Transactional
    public void merRebate(Merchant merchant) {
        //查询该商户返点信息

        //修改数据
    }
}
