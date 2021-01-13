package com.welfare.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.welfare.common.constants.WelfareSettleConstant;
import com.welfare.common.exception.BusiException;
import com.welfare.common.exception.ExceptionCode;
import com.welfare.common.util.DateUtil;
import com.welfare.common.util.ExcelUtil;
import  com.welfare.persist.dao.MonthSettleDao;
import com.welfare.persist.dto.MonthSettleDTO;
import com.welfare.persist.dto.MonthSettleDetailDTO;
import com.welfare.persist.dto.query.MonthSettleDetailQuery;
import com.welfare.persist.dto.query.MonthSettleQuery;
import com.welfare.persist.entity.MonthSettle;
import com.welfare.persist.mapper.MonthSettleMapper;
import com.welfare.persist.mapper.SettleDetailMapper;
import com.welfare.service.dto.MonthSettleDetailReq;
import com.welfare.service.dto.MonthSettleDetailResp;
import com.welfare.service.dto.MonthSettleReq;
import com.welfare.service.dto.MonthSettleResp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.welfare.service.MonthSettleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
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

    private final SettleDetailMapper settleDetailMapper;

    private final MonthSettleDao monthSettleDao;


    @Override
    public Page<MonthSettleResp> pageQuery(MonthSettleReq monthSettleReqDto) {

        MonthSettleQuery monthSettleQuery = new MonthSettleQuery();
        BeanUtils.copyProperties(monthSettleReqDto, monthSettleQuery);

        PageHelper.startPage(monthSettleReqDto.getCurrentPage(),monthSettleReqDto.getPageSize());
        List<MonthSettleDTO> monthSettleDTOS = monthSettleMapper.selectMonthSettle(monthSettleQuery);
        PageInfo<MonthSettleDTO> monthSettleDTOPageInfo = new PageInfo<>(monthSettleDTOS);

        Page<MonthSettleResp> monthSettleRespPage = new Page<>(monthSettleReqDto.getCurrentPage(), monthSettleReqDto.getPageSize(),monthSettleDTOPageInfo.getTotal());

        monthSettleRespPage.setRecords(monthSettleDTOPageInfo.getList().stream().map(monthSettleDTO -> {
            MonthSettleResp monthSettleResp = new MonthSettleResp();
            BeanUtils.copyProperties(monthSettleDTO, monthSettleResp);
            return monthSettleResp;
        }).collect(Collectors.toList()));

        return monthSettleRespPage;
    }

    @Override
    public Page<MonthSettleDetailResp> pageQueryMonthSettleDetail(String id, MonthSettleDetailReq monthSettleDetailReq) {

        MonthSettleDetailQuery monthSettleDetailQuery = getMonthSettleDetailQuery(id, monthSettleDetailReq);

        PageHelper.startPage(monthSettleDetailReq.getCurrentPage(), monthSettleDetailReq.getPageSize());
        List<MonthSettleDetailDTO> monthSettleDetailDTOS = settleDetailMapper.selectMonthSettleDetail(monthSettleDetailQuery);
        PageInfo<MonthSettleDetailDTO> monthSettleDetailDTOPageInfo = new PageInfo<>(monthSettleDetailDTOS);

        Page<MonthSettleDetailResp> monthSettleDetailRespPage = new Page<>(monthSettleDetailReq.getCurrentPage(),
                monthSettleDetailReq.getPageSize(),monthSettleDetailDTOPageInfo.getTotal());

        monthSettleDetailRespPage.setRecords(monthSettleDetailDTOPageInfo.getList().stream().map(monthSettleDetailDTO -> {
            MonthSettleDetailResp monthSettleDetailResp = new MonthSettleDetailResp();
            BeanUtils.copyProperties(monthSettleDetailDTO, monthSettleDetailResp);
            return monthSettleDetailResp;
        }).collect(Collectors.toList()));

        return monthSettleDetailRespPage;
    }

    @Override
    public List<MonthSettleDetailResp> queryMonthSettleDetail(String id, MonthSettleDetailReq monthSettleDetailReq) {

        MonthSettleDetailQuery monthSettleDetailQuery = getMonthSettleDetailQuery(id, monthSettleDetailReq);

        List<MonthSettleDetailDTO> monthSettleDetailDTOS = settleDetailMapper.selectMonthSettleDetail(monthSettleDetailQuery);

        List<MonthSettleDetailResp> monthSettleDetailResps = monthSettleDetailDTOS.stream().map(monthSettleDetailDTO -> {
            MonthSettleDetailResp monthSettleDetailResp = new MonthSettleDetailResp();
            BeanUtils.copyProperties(monthSettleDetailDTO, monthSettleDetailResp);
            return monthSettleDetailResp;
        }).collect(Collectors.toList());

        return monthSettleDetailResps;
    }

    @Override
    public Integer monthSettleSend(String id) {
        MonthSettle monthSettle = new MonthSettle();

        //修改账单发送状态为已发送
        monthSettle.setId(Long.parseLong(id));
        monthSettle.setSendStatus(WelfareSettleConstant.SettleSendStatusEnum.SENDED.code());

        monthSettle.setSendTime(new Date());

        return monthSettleMapper.update(monthSettle,
                Wrappers.<MonthSettle>lambdaUpdate().
                        eq(MonthSettle::getSendStatus, WelfareSettleConstant.SettleSendStatusEnum.UNSENDED.code())
        );
    }

    @Override
    public Integer monthSettleConfirm(String id) {
        MonthSettle monthSettle = new MonthSettle();

        //修改账单确认状态为已确认
        monthSettle.setId(Long.parseLong(id));
        monthSettle.setRecStatus(WelfareSettleConstant.SettleRecStatusEnum.CONFIRMED.code());
        monthSettle.setConfirmTime(new Date());

        return monthSettleMapper.update(monthSettle,
                Wrappers.<MonthSettle>lambdaUpdate().
                        eq(MonthSettle::getSendStatus, WelfareSettleConstant.SettleSendStatusEnum.SENDED.code())
        );
    }

    @Override
    public Integer monthSettleFinish(String id) {
        //修改账单结算状态为已结算
        MonthSettle monthSettle = new MonthSettle();
        monthSettle.setId(Long.parseLong(id));
        monthSettle.setSettleStatus(WelfareSettleConstant.SettleStatusEnum.SETTLED.code());

        return monthSettleMapper.update(monthSettle,
                Wrappers.<MonthSettle>lambdaUpdate().
                        eq(MonthSettle::getSettleStatus, WelfareSettleConstant.SettleStatusEnum.UNSETTLED.code())
        );
    }

    @Override
    public MonthSettle getMonthSettle(MonthSettleDetailQuery monthSettleDetailQuery) {
        return monthSettleMapper.sumSettleDetailToMonthSettle(monthSettleDetailQuery);
    }

    @Override
    public Integer addMonthSettle(MonthSettle monthSettle) {
        return monthSettleMapper.insert(monthSettle);
    }

    /**
     * 根据账单编号及查询参数，获取查询账单明细限制查询条件
     * @param id 账单编号
     * @param monthSettleDetailReq
     * @return
     */
    private MonthSettleDetailQuery getMonthSettleDetailQuery(String id, MonthSettleDetailReq monthSettleDetailReq){
        List<MonthSettle> list = monthSettleDao.list();
        MonthSettle byId = monthSettleDao.getById(id);
        MonthSettle monthSettle = monthSettleMapper.selectById(id);

        
        
        if(monthSettle == null){
            throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS,"参数异常，未获取到账单信息", null);
        }

        MonthSettleDetailQuery monthSettleDetailQuery = new MonthSettleDetailQuery();
        BeanUtils.copyProperties(monthSettleDetailReq, monthSettleDetailQuery);
        monthSettleDetailQuery.setMerCode(monthSettle.getMerCode());

        //限制查询起始结束时间
        String settleMonth = monthSettle.getSettleMonth();
        Date dayMax = null;
        Date dayMin = null;
        try {
            dayMax = DateUtil.getDayMaxByMontStr(settleMonth);
            dayMin = DateUtil.getDayMinByMonthStr(settleMonth);
        } catch (ParseException e) {
            throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS,"参数异常，错误到月份格式："+settleMonth, null);
        }
        if(monthSettleDetailReq.getStartTime() == null || monthSettleDetailReq.getStartTime().before(dayMin)){
            monthSettleDetailQuery.setStartTime(dayMin);
        }
        if(monthSettleDetailReq.getEndTime() == null || monthSettleDetailReq.getEndTime().after(dayMax)){
            monthSettleDetailQuery.setEndTime(dayMax);
        }
        return monthSettleDetailQuery;
    }
}