package com.welfare.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.common.base.BasePageVo;
import com.welfare.persist.dto.MonthSettleDetailDTO;
import com.welfare.persist.dto.MonthSettleDetailSummaryDTO;
import com.welfare.persist.dto.query.MonthSettleDetailQuery;
import com.welfare.persist.entity.MonthSettle;
import com.welfare.service.dto.*;

import java.util.List;
import java.util.Map;

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
    BasePageVo<MonthSettleResp> pageQuery(MonthSettlePageReq monthSettleReqDto);

    MonthSettleResp queryById(Long id);

    /**
     * 根据主键账单id、部分限制条件，分页查询月账单明细
     * @param monthSettleDetailReq
     * @return
     */
    Page<MonthSettleDetailResp> pageQueryMonthSettleDetail(Long id, MonthSettleDetailPageReq monthSettleDetailReq);

    /**
     * 根据主键账单id、部分限制条件，查询月账单明细统计信息
     * @param id
     * @param monthSettleDetailReq
     * @return
     */
    MonthSettleDetailSummaryDTO monthSettleDetailSummary(Long id, MonthSettleDetailReq monthSettleDetailReq);

    /**
     * 根据主键账单id、部分限制条件，导出账单数据
     * @param id
     */
    List<MonthSettleDetailResp> queryMonthSettleDetailLimit(Long id, MonthSettleDetailReq monthSettleDetailReq);

    /**
     * 月账单发送
     * @param id
     */
    Integer monthSettleSend(Long id);

    /**
     * 月账单确认
     * @param id
     */
    Integer monthSettleConfirm(Long id);

    /**
     * 月账单完成
     * @param id
     */
    Integer monthSettleFinish(Long id);

    /**
     * 按条件查询生成商户月账单
     * @param monthSettleDetailQuery
     * @return
     */
    MonthSettle getMonthSettle(MonthSettleDetailQuery monthSettleDetailQuery);

    Boolean addMonthSettleList(List<MonthSettle> monthSettleList);

    MonthSettle getMonthSettleById(Long id);

    List<Map<String, Object>> getAccoutType(String merCode);
}
