package com.welfare.persist.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.welfare.persist.dto.EmployeeSettleBillDTO;
import com.welfare.persist.dto.EmployeeSettleDTO;
import com.welfare.persist.dto.query.EmployeeSettleBillQuery;
import com.welfare.persist.dto.query.EmployeeSettleQuery;
import com.welfare.persist.entity.EmployeeSettle;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商户员工结算账单(employee_settle)数据Mapper
 *
 * @author Yuxiang Li
 * @since 2021-03-03 17:44:04
 * @description 由 Mybatisplus Code Generator 创建
*/
@Mapper
public interface EmployeeSettleMapper extends BaseMapper<EmployeeSettle> {

  List<EmployeeSettleBillDTO> querySettleBills(@Param("query") EmployeeSettleBillQuery query);
    /**
     * 查询商户员工授信消费列表
     * @param employeeSettleQuery
     * @return
     */
    List<EmployeeSettleDTO> getEmployeeSettleList(EmployeeSettleQuery employeeSettleQuery);

}
