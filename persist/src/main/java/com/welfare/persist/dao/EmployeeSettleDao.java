package com.welfare.persist.dao;

import lombok.extern.slf4j.Slf4j;
import com.welfare.persist.entity.EmployeeSettle;
import com.welfare.persist.mapper.EmployeeSettleMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Repository;

/**
 * 商户员工结算账单(employee_settle)数据DAO
 *
 * @author Yuxiang Li
 * @since 2021-03-03 17:44:04
 * @description 由 Mybatisplus Code Generator 创建
 */
@Slf4j
@Repository
public class EmployeeSettleDao extends ServiceImpl<EmployeeSettleMapper, EmployeeSettle> {

}