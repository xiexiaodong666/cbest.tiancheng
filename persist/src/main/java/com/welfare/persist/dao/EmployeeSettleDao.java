package com.welfare.persist.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.welfare.persist.entity.EmployeeSettle;
import com.welfare.persist.mapper.EmployeeSettleMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

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

  public List<EmployeeSettle> listBySettleNos(List<String> settleNos) {
    if (CollectionUtils.isNotEmpty(settleNos)) {
      QueryWrapper<EmployeeSettle> queryWrapper = new QueryWrapper<>();
      queryWrapper.in(EmployeeSettle.SETTLE_NO, settleNos);
      return list(queryWrapper);
    }
    return Lists.newArrayList();
  }
}