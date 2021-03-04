package com.welfare.service.settlement.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.welfare.common.base.BasePageVo;
import com.welfare.persist.dao.EmployeeSettleDao;
import com.welfare.persist.dto.query.EmployeeSettleQuery;
import com.welfare.persist.mapper.EmployeeSettleMapper;
import com.welfare.service.dto.EmployeeSettlePageReq;
import com.welfare.service.dto.EmployeeSettleResp;
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

    @Override
    public BasePageVo<EmployeeSettleResp> pageQuery(EmployeeSettlePageReq employeeSettlePageReq) {

        EmployeeSettleQuery employeeSettleQuery = new EmployeeSettleQuery();
        BeanUtils.copyProperties(employeeSettlePageReq, employeeSettleQuery);
        PageInfo<EmployeeSettleResp> employeeSettleDTOPageInfo = PageHelper.startPage(employeeSettlePageReq.getCurrent(), employeeSettlePageReq.getSize())
                .doSelectPageInfo(() -> {
                    employeeSettleMapper.getEmployeeSettleList(employeeSettleQuery).stream().map(employeeSettleDTO -> {
                        EmployeeSettleResp employeeSettleResp = new EmployeeSettleResp();
                        BeanUtils.copyProperties(employeeSettleDTO, employeeSettleResp);
                        return employeeSettleResp;
                    }).collect(Collectors.toList());
                });

        BasePageVo<EmployeeSettleResp> employeeSettleRespBasePageVo = new BasePageVo<>(employeeSettlePageReq.getCurrent(),
                employeeSettlePageReq.getSize(), employeeSettleDTOPageInfo.getTotal(), employeeSettleDTOPageInfo.getList());

        return employeeSettleRespBasePageVo;
    }
}