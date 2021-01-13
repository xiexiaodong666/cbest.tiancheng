package com.welfare.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.persist.dto.MonthSettleDetailDTO;
import com.welfare.persist.dto.query.MonthSettleDetailQuery;
import com.welfare.persist.entity.MonthSettle;
import com.welfare.service.dto.MonthSettleDetailReq;
import com.welfare.service.dto.MonthSettleDetailResp;
import com.welfare.service.dto.MonthSettleReq;
import com.welfare.service.dto.MonthSettleResp;

import java.util.List;

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
     * 根据主键账单id、部分限制条件，分页查询月账单明细
     * @param monthSettleDetailReq
     * @return
     */
    Page<MonthSettleDetailResp> pageQueryMonthSettleDetail(String id, MonthSettleDetailReq monthSettleDetailReq);

    /**
     * 根据主键账单id、部分限制条件，导出账单数据
     * @param id
     */
    List<MonthSettleDetailResp> queryMonthSettleDetail(String id, MonthSettleDetailReq monthSettleDetailReq);

    /**
     * 月账单发送
     * @param id
     */
    Integer monthSettleSend(String id);

    /**
     * 月账单确认
     * @param id
     */
    Integer monthSettleConfirm(String id);

    /**
     * 月账单完成
     * @param id
     */
    Integer monthSettleFinish(String id);

    /**
     * 按条件查询生成商户月账单
     * @param monthSettleDetailQuery
     * @return
     */
    MonthSettle getMonthSettle(MonthSettleDetailQuery monthSettleDetailQuery);

    Integer addMonthSettle(MonthSettle monthSettle);
}
