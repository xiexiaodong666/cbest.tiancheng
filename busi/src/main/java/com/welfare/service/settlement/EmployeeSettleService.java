package com.welfare.service.settlement;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.service.dto.EmployeeSettleBillPageReq;
import com.welfare.service.dto.EmployeeSettleBillResp;

import com.welfare.common.base.BasePageVo;
import com.welfare.service.dto.EmployeeSettleConsumePageReq;
import com.welfare.service.dto.EmployeeSettleConsumeResp;

/**
 * 商户员工结算账单服务接口
 *
 * @author Yuxiang Li
 * @since 2021-03-03 17:44:04
 * @description 由 Mybatisplus Code Generator 创建
 */
public interface EmployeeSettleService {


  /**
   * @author Rongya.huang
   * @date 17:02 2021/3/4
   * @description 分页查询员工结算账单
   **/
  Page<EmployeeSettleBillResp> pageQueryBill(EmployeeSettleBillPageReq billPageReq);
  /**
   * 分页查询员工消费
   * @param employeeSettleConsumePageReq
   * @return
   */
  BasePageVo<EmployeeSettleConsumeResp> pageQuery(EmployeeSettleConsumePageReq employeeSettleConsumePageReq);

}
