package com.welfare.service.settlement.impl;

import com.welfare.persist.dao.EmployeeSettleDao;
import com.welfare.service.settlement.EmployeeSettleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

}