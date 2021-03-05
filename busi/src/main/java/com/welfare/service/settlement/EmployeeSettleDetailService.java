package com.welfare.service.settlement;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.common.base.BasePageVo;
import com.welfare.persist.dto.EmployeeSettleConsumeDTO;
import com.welfare.persist.dto.EmployeeSettleSumDTO;
import com.welfare.persist.dto.query.EmployeeSettleConsumeQuery;
import com.welfare.service.dto.EmployeeSettleConsumePageReq;
import com.welfare.service.dto.EmployeeSettleDetailPageReq;
import com.welfare.service.dto.EmployeeSettleDetailReq;
import com.welfare.service.dto.EmployeeSettleDetailResp;

import java.util.Date;
import java.util.List;

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

  /**
   * 分页查询员工消费
   * @param employeeSettleConsumePageReq
   * @return
   */
  BasePageVo<EmployeeSettleConsumeDTO> pageQuery(EmployeeSettleConsumePageReq employeeSettleConsumePageReq);

  /**
   * 分页查询员工授信额度消费汇总
   * @param employeeSettleConsumeQuery employeeSettleConsumeQuery
   * @return
   */
  EmployeeSettleSumDTO summary(EmployeeSettleConsumeQuery employeeSettleConsumeQuery);

  /**
   * 分页查询具体员工授信额度消费汇总
   * @param accountCode accountCode
   * @param employeeSettleDetailReq employeeSettleDetailReq
   * @return
   */
  EmployeeSettleSumDTO detailSummary(String accountCode, EmployeeSettleDetailReq employeeSettleDetailReq);
  /**
   * 分页查询具体员工授信额度消费详情
   * @param accountCode accountCode
   * @param employeeSettleDetailPageReq employeeSettleDetailPageReq
   * @return
   */
  BasePageVo<EmployeeSettleDetailResp> pageQueryDetail(String accountCode, EmployeeSettleDetailPageReq employeeSettleDetailPageReq);

  /**
   * 分页查询具体员工授信额度消费账单详情
   * @param settleNo settleNo
   * @param employeeSettlePageReq employeeSettlePageReq
   * @return
   */
  Page<EmployeeSettleDetailResp> pageQueryEmployeeSettleDetail(String settleNo, EmployeeSettleDetailPageReq employeeSettlePageReq);

  /**
   * 工授信额度消费详情导出
   * @param accountCode accountCode
   * @param employeeSettleDetailReq employeeSettleDetailReq
   * @return
   */
  List<EmployeeSettleDetailResp> detailExport(String accountCode, EmployeeSettleDetailReq employeeSettleDetailReq);

  /**
   * 账单页查询具体员工授信额度消费汇总
   * @param settleNo settleNo
   * @param employeeSettleDetailReq employeeSettleDetailReq
   * @return
   */
  EmployeeSettleSumDTO detailSummaryWithSettleNo(String settleNo, EmployeeSettleDetailReq employeeSettleDetailReq);
}
