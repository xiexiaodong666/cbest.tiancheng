package com.welfare.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.common.domain.MerchantUserInfo;
import com.welfare.persist.entity.OrderInfo;
import com.welfare.persist.entity.OrderSummary;
import com.welfare.service.dto.OrderReqDto;

import java.util.List;

/**
 * @ProjectName: e-welfare
 * @Package: com.welfare.service
 * @ClassName: OrderService
 * @Author: jian.zhou
 * @Description: 订单
 * @Date: 2021/1/9 17:15
 * @Version: 1.0
 */
public interface OrderService {

    Page<OrderInfo> selectPage(Page page,OrderReqDto orderReqDto , MerchantUserInfo merchantUserInfo);

    List<OrderInfo> selectList(OrderReqDto orderReqDto , MerchantUserInfo merchantUserInfo);

    OrderSummary selectSummary(OrderReqDto orderReqDto , MerchantUserInfo merchantUserInfo);

    /**
     * 同步重百小票数据
     * @Return
     * @Exception
     */
    void syncOrderData();
}
