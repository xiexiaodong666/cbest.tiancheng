package com.welfare.service;

import com.welfare.common.domain.MerchantUserInfo;
import com.welfare.persist.entity.OrderInfo;
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

    List<OrderInfo> selectList(OrderReqDto orderReqDto , MerchantUserInfo merchantUserInfo);

    /**
     * 同步重百小票数据
     * @Return
     * @Exception
     */
    void syncOrderData();
}
