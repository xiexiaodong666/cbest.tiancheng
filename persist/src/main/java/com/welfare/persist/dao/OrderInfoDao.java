package com.welfare.persist.dao;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.welfare.persist.entity.OrderInfo;
import com.welfare.persist.mapper.OrderInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * (order_info)数据DAO
 *
 * @author kancy
 * @since 2021-01-12 17:25:14
 * @description 由 Mybatisplus Code Generator 创建
 */
@Slf4j
@Repository
public class OrderInfoDao extends ServiceImpl<OrderInfoMapper, OrderInfo> {
    /**
     * 根据交易单号获取一条订单
     * @param transNo
     * @return
     */
    public OrderInfo getOneByTransNo(String transNo){
        return getOne(
                Wrappers.<OrderInfo>lambdaQuery()
                        .eq(OrderInfo::getTransNo,transNo)
                        .last("limit 1")
        );
    }

    public OrderInfo getOneByOrderNo(String orderNo){
        return getOne(
                Wrappers.<OrderInfo>lambdaQuery().eq(OrderInfo::getOrderId,orderNo).last("limit 1")
        );
    }
}