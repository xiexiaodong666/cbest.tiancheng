package com.welfare.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.welfare.common.base.BasePageVo;
import com.welfare.persist.dao.SettleDetailDao;
import com.welfare.persist.dto.query.WelfareSettleDetailQuery;
import com.welfare.persist.dto.query.WelfareSettleQuery;
import com.welfare.persist.entity.MonthSettle;
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
        return null;
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
        MonthSettle monthSettle = settleDetailMapper.getSettleByCondition(welfareSettleDetailQuery);
        monthSettleMapper.insert(monthSettle);
    }

    @Override
    public SettleMerInfoResp getAccountInfo(Long id) {
        return null;
    }

    @Override
    public Page<SettleMerTransDetailResp> getAccountTransDetail(Long id) {
        return null;
    }
}
