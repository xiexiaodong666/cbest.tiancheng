package com.welfare.persist.mapper;

import com.welfare.persist.dto.EmployeeSettleConsumeDTO;
import com.welfare.persist.dto.query.EmployeeSettleConsumeQuery;
import com.welfare.persist.entity.EmployeeSettleDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * (employee_settle_detail)数据Mapper
 *
 * @author Yuxiang Li
 * @since 2021-03-03 17:44:04
 * @description 由 Mybatisplus Code Generator 创建
*/
@Mapper
public interface EmployeeSettleDetailMapper extends BaseMapper<EmployeeSettleDetail> {

  /**
   * 查询获取账户交易明细
   * @param params
   * @return
   */
  List<EmployeeSettleDetail> getFromAccountDetail(Map<String, Object> params);

  /**
   * 查询商户员工授信消费列表
   * @param employeeSettleConsumeQuery
   * @return
   */
  List<EmployeeSettleConsumeDTO> getEmployeeSettleConsumeList(EmployeeSettleConsumeQuery employeeSettleConsumeQuery);
}
