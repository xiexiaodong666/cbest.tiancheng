package com.welfare.service.settlement;


import java.util.Date;

/**
 * 服务接口
 *
 * @author Yuxiang Li
 * @since 2021-03-03 17:44:04
 * @description 由 Mybatisplus Code Generator 创建
 */
public interface EmployeeSettleDetailService {

  /**
   * 指定某一日期将员工授信额度、溢缴款流水同步到表employee_settle_detail
   * @param date
   */
  void pullAccountDetailByDate(Date date);
}
