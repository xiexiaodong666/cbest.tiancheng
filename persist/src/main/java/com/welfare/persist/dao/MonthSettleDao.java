package com.welfare.persist.dao;

import com.welfare.persist.entity.MonthSettle;
import lombok.extern.slf4j.Slf4j;
import com.welfare.persist.mapper.MonthSettleMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Repository;

/**
 * 月度结算账单(month_settle)数据DAO
 *
 * @author Yuxiang Li
 * @since 2021-01-06 13:49:25
 * @description 由 Mybatisplus Code Generator 创建
 */
@Slf4j
@Repository
public class MonthSettleDao extends ServiceImpl<MonthSettleMapper, MonthSettle> {

}