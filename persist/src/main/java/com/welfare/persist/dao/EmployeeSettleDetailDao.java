package com.welfare.persist.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import com.welfare.persist.entity.EmployeeSettleDetail;
import com.welfare.persist.mapper.EmployeeSettleDetailMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * (employee_settle_detail)数据DAO
 *
 * @author Yuxiang Li
 * @since 2021-03-03 17:44:04
 * @description 由 Mybatisplus Code Generator 创建
 */
@Slf4j
@Repository
public class EmployeeSettleDetailDao extends ServiceImpl<EmployeeSettleDetailMapper, EmployeeSettleDetail> {

  public int countByTransTime(Date transTimeStart, Date transTimeEnd) {
    //QueryWrapper<>
    return 0;
  }

}