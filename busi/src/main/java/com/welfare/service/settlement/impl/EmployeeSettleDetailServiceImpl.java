package com.welfare.service.settlement.impl;

import com.welfare.persist.dao.EmployeeSettleDetailDao;
import com.welfare.service.settlement.EmployeeSettleDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 服务接口实现
 *
 * @author Yuxiang Li
 * @since 2021-03-03 17:44:04
 * @description 由 Mybatisplus Code Generator 创建
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class EmployeeSettleDetailServiceImpl implements EmployeeSettleDetailService {
    private final EmployeeSettleDetailDao employeeSettleDetailDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void pullAccountDetailByDate(String dateStr) {

    }
}