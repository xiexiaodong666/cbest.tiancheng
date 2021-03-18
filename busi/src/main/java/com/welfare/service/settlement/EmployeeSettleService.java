package com.welfare.service.settlement;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.service.dto.EmployeeSettleBillPageReq;
import com.welfare.service.dto.EmployeeSettleBillResp;
import com.welfare.service.dto.EmployeeSettleBuildReq;
import com.welfare.service.dto.EmployeeSettleFinishReq;

import java.util.List;

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
   * 按条件生成员工授信结算单
   * @param settleBuildReq
   */
  List<String> buildEmployeeSettle(EmployeeSettleBuildReq settleBuildReq);

  /**
   * 完成结算
   * @param employeeSettleFinishReq 结算单号
   */
  boolean finishEmployeeSettle(EmployeeSettleFinishReq employeeSettleFinishReq);

  /**
   * 查询结算单信息
   * @param settleNo 结算单号
   */
  EmployeeSettleBillResp queryBillInfo(String settleNo);
}
