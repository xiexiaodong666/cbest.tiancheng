package com.welfare.service.settlement.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.welfare.common.base.BasePageVo;
import com.welfare.common.util.DateUtil;
import com.welfare.persist.dao.EmployeeSettleDao;
import com.welfare.persist.dto.EmployeeSettleBillDTO;
import com.welfare.persist.dto.EmployeeSettleConsumeDTO;
import com.welfare.persist.dto.EmployeeSettleSumDTO;
import com.welfare.persist.dto.query.EmployeeSettleBillQuery;
import com.welfare.persist.dto.query.EmployeeSettleConsumeQuery;
import com.welfare.persist.dto.query.EmployeeSettleDetailQuery;
import com.welfare.persist.mapper.EmployeeSettleDetailMapper;
import com.welfare.persist.mapper.EmployeeSettleMapper;
import com.welfare.service.dto.*;
import com.welfare.service.settlement.EmployeeSettleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

/**
 * 商户员工结算账单服务接口实现
 *
 * @author Yuxiang Li
 * @since 2021-03-03 17:44:04
 * @description 由 Mybatisplus Code Generator 创建
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class EmployeeSettleServiceImpl implements EmployeeSettleService {
    private final EmployeeSettleDao employeeSettleDao;
    private final EmployeeSettleMapper employeeSettleMapper;

    private final EmployeeSettleDetailMapper employeeSettleDetailMapper;

    @Override
    public Page<EmployeeSettleBillResp> pageQueryBill(EmployeeSettleBillPageReq billPageReq) {

        EmployeeSettleBillQuery billQuery = new EmployeeSettleBillQuery();
        BeanUtils.copyProperties(billPageReq, billQuery);

        //传入的日期，修订未当日最大和当日最小值
        if(billQuery.getStartTime()!=null){
            billQuery.setStartTime(DateUtil.getDayMin(billQuery.getStartTime(), 0));
        }
        if(billQuery.getEndTime()!=null){
            billQuery.setEndTime(DateUtil.getDayMax(billQuery.getEndTime(), 0));
        }

        PageInfo<EmployeeSettleBillDTO> dtoPage = PageHelper.startPage(billPageReq.getCurrent(), billPageReq.getSize()).doSelectPageInfo(
            () -> employeeSettleMapper.querySettleBills(billQuery));

        Page<EmployeeSettleBillResp> respPage = new Page<>(billPageReq.getCurrent(), billPageReq.getSize(),dtoPage.getTotal());

        if(dtoPage.getList().isEmpty()){
            return respPage;
        }

        respPage.setRecords(dtoPage.getList().stream().map(dto -> {
            EmployeeSettleBillResp resp = new EmployeeSettleBillResp();
            BeanUtils.copyProperties(dto, resp);
            return resp;
        }).collect(Collectors.toList()));

        return respPage;
    }

    @Override
    public BasePageVo<EmployeeSettleConsumeDTO> pageQuery(EmployeeSettleConsumePageReq employeeSettleConsumePageReq) {

        EmployeeSettleConsumeQuery employeeSettleConsumeQuery = new EmployeeSettleConsumeQuery();
        BeanUtils.copyProperties(employeeSettleConsumePageReq, employeeSettleConsumeQuery);
        PageInfo<EmployeeSettleConsumeDTO> employeeSettleDTOPageInfo = PageHelper.startPage(employeeSettleConsumePageReq.getCurrent(), employeeSettleConsumePageReq.getSize())
                .doSelectPageInfo(() -> employeeSettleDetailMapper.getEmployeeSettleConsumeList(employeeSettleConsumeQuery));

        BasePageVo<EmployeeSettleConsumeDTO> employeeSettleRespBasePageVo = new BasePageVo<>(employeeSettleConsumePageReq.getCurrent(),
                employeeSettleConsumePageReq.getSize(), employeeSettleDTOPageInfo.getTotal(), employeeSettleDTOPageInfo.getList());

        return employeeSettleRespBasePageVo;
    }

    @Override
    public EmployeeSettleSumDTO summary(EmployeeSettleConsumeQuery employeeSettleConsumeQuery) {
        return employeeSettleDetailMapper.getEmployeeSettleConsumeSum(employeeSettleConsumeQuery);
    }

    @Override
    public EmployeeSettleSumDTO detailSummary(String accountCode, EmployeeSettleDetailQuery employeeSettleDetailQuery) {
        return employeeSettleDetailMapper.getEmployeeSettleDetailSum(accountCode, employeeSettleDetailQuery);
    }
}