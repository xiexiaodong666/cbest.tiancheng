package com.welfare.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.service.dto.MonthSettleDetailReq;
import com.welfare.service.dto.MonthSettleDetailResp;
import com.welfare.service.dto.MonthSettleReq;
import com.welfare.service.dto.MonthSettleResp;

/**
 * 月度结算账单服务接口
 *
 * @author Yuxiang Li
 * @since 2021-01-06 13:49:25
 * @description 由 Mybatisplus Code Generator 创建
 */
public interface MonthSettleService {

    /**
     * 分页查询月账单列表
     * @param monthSettleReqDto
     * @return
     */
    Page<MonthSettleResp> pageQuery(MonthSettleReq monthSettleReqDto);

    /**
     * 分页查询月账单明细
     * @param monthSettleDetailReqDto
     * @return
     */
    Page<MonthSettleDetailResp> pageQueryMonthSettleDetail(MonthSettleDetailReq monthSettleDetailReqDto);

    /**
     * 根据主键账单id，导出账单数据
     * @param id
     */
    void exportMonthSettleDetail(String id);

    /**
     * 月账单发送
     * @param id
     */
    void monthSettleSend(String id);

    /**
     * 月账单确认
     * @param id
     */
    void monthSettleConfirm(String id);

    /**
     * 月账单完成
     * @param id
     */
    void monthSettleFinish(String id);
}
