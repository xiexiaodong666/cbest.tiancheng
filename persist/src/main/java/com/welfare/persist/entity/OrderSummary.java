package com.welfare.persist.entity;

import lombok.Data;

/**
 * @ProjectName: e-welfare
 * @Package: com.welfare.persist.entity
 * @ClassName: OrderSummary
 * @Author: jian.zhou
 * @Description: 订单汇总
 * @Date: 2021/1/14 14:59
 * @Version: 1.0
 */
@Data
public class OrderSummary {

    private String orderAmount;

    private Integer orderNum;
}
