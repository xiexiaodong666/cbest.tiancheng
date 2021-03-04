package com.welfare.service.settlement;


import com.welfare.common.base.BasePageVo;
import com.welfare.service.dto.EmployeeSettlePageReq;
import com.welfare.service.dto.EmployeeSettleResp;

/**
 * 商户员工结算账单服务接口
 *
 * @author Yuxiang Li
 * @since 2021-03-03 17:44:04
 * @description 由 Mybatisplus Code Generator 创建
 */
public interface EmployeeSettleService {

    /**
     * 分页查询员工结算账单
     * @param employeeSettlePageReq
     * @return
     */
    BasePageVo<EmployeeSettleResp> pageQuery(EmployeeSettlePageReq employeeSettlePageReq);

}
