package com.welfare.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.common.constants.WelfareSettleConstant;
import  com.welfare.persist.dao.MonthSettleDao;
import com.welfare.persist.entity.MonthSettle;
import com.welfare.persist.mapper.MonthSettleMapper;
import com.welfare.service.dto.MonthSettleDetailReq;
import com.welfare.service.dto.MonthSettleDetailResp;
import com.welfare.service.dto.MonthSettleReq;
import com.welfare.service.dto.MonthSettleResp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.welfare.service.MonthSettleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
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

    @Override
    public Page<MonthSettleResp> pageQuery(MonthSettleReq monthSettleReqDto) {
        Page<MonthSettle> monthSettlePage = new Page<>();
        monthSettlePage.setPages(monthSettleReqDto.getPageIndex());
        monthSettlePage.setSize(monthSettleReqDto.getPageSize());

        List<MonthSettleResp> resultList = monthSettlePage.getRecords().stream().map(monthSettle -> {
            MonthSettleResp monthSettleResp = new MonthSettleResp();
            return monthSettleResp;
        }).collect(Collectors.toList());

        Page<MonthSettleResp> monthSettleRespPage = new Page<>();
        monthSettleRespPage.setRecords(resultList);

        return monthSettleRespPage;
    }

    @Override
    public Page<MonthSettleDetailResp> pageQueryMonthSettleDetail(MonthSettleDetailReq monthSettleDetailReqDto) {
        return null;

    }

    @Override
    public void exportMonthSettleDetail(String id) {

    }

    @Override
    public void monthSettleSend(String id) {
        MonthSettle monthSettle = monthSettleMapper.selectById(id);

        //修改账单发送状态为已发送
        monthSettle.setId(Long.parseLong(id));
        monthSettle.setSendStatus(WelfareSettleConstant.SettleSendStatusEnum.SENDED.code());

        if(monthSettle.getSendTime() == null){
            monthSettle.setSendTime(new Date());
        }

        monthSettleMapper.updateById(monthSettle);
    }

    @Override
    public void monthSettleConfirm(String id) {
        MonthSettle monthSettle = monthSettleMapper.selectById(id);

        //修改账单确认状态为已确认
        monthSettle.setId(Long.parseLong(id));
        monthSettle.setRecStatus(WelfareSettleConstant.SettleRecStatusEnum.CONFIRMED.code());

        if(monthSettle.getConfirmTime() == null){
            monthSettle.setConfirmTime(new Date());
        }

        monthSettleMapper.updateById(monthSettle);
    }

    @Override
    public void monthSettleFinish(String id) {
        //修改账单结算状态为已结算
        MonthSettle monthSettle = new MonthSettle();
        monthSettle.setId(Long.parseLong(id));

        monthSettleMapper.updateById(monthSettle);
    }
}