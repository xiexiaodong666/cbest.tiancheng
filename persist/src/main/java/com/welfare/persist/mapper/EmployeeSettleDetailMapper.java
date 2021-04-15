package com.welfare.persist.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.welfare.persist.dto.*;
import com.welfare.persist.dto.query.EmployeeSettleBuildQuery;
import com.welfare.persist.dto.query.EmployeeSettleConsumeQuery;
import com.welfare.persist.dto.query.EmployeeSettleDetailQuery;
import com.welfare.persist.entity.EmployeeSettleDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
  List<EmployeeSettleConsumeDTO> getEmployeeSettleConsumeList(@Param("query") EmployeeSettleConsumeQuery employeeSettleConsumeQuery);

  /**
   * 查询商户员工授信消费汇总
   * @param employeeSettleConsumeQuery
   * @return
   */
  EmployeeSettleSumDTO getEmployeeSettleConsumeSum(@Param("query") EmployeeSettleConsumeQuery employeeSettleConsumeQuery);

  EmployeeSettleSumDTO getEmployeeSettleDetailSum(@Param("query") EmployeeSettleDetailQuery employeeSettleDetailQuery);

  /**
   * 结算单明细列表
   * @param query
   * @return List<EmployeeSettleDetailDTO>
   **/
  List<EmployeeSettleDetailDTO> querySettleDetail(@Param("query")EmployeeSettleDetailQuery query);


  List<EmployeeSettleBuildDTO> getBuildDetailGroupByAccount(@Param("query") EmployeeSettleBuildQuery query);

  List<EmployeeSettleDetail> getBuildEmployeeSettleDetail(@Param("query") EmployeeSettleBuildQuery query);

  int batchUpdateStatusBySettleNo(@Param("status") String status, @Param("settleNos") List<String> settleNos,  @Param("updateUser") String updateUser);

  List<EmployeeSettleStoreDTO> allStoresInMonthSettle(@Param("settleNo")String settleNo, @Param("accountCode")String accountCode);
}
