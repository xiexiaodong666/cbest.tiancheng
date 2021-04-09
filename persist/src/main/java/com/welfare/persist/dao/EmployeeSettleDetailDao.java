package com.welfare.persist.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.welfare.persist.entity.EmployeeSettleDetail;
import com.welfare.persist.mapper.EmployeeSettleDetailMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

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
    QueryWrapper<EmployeeSettleDetail> queryWrapper = new QueryWrapper<>();
    queryWrapper
            .ge(EmployeeSettleDetail.TRANS_TIME, transTimeStart)
            .le(EmployeeSettleDetail.TRANS_TIME, transTimeEnd);
    return this.count(queryWrapper);
  }

  public boolean batchUpdateStatusBySettleNo(String status, String updateUser, List<String> settleNos) {
    if (CollectionUtils.isNotEmpty(settleNos)) {
      return this.baseMapper.batchUpdateStatusBySettleNo(status, settleNos, updateUser) > 0;
    } else {
      return false;
    }
  }
}