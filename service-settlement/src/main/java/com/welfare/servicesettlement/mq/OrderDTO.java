package com.welfare.servicesettlement.mq;

import com.welfare.persist.entity.OrderInfo;
import lombok.Data;

import java.io.Serializable;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 3/29/2021
 */
@Data
public class OrderDTO implements Serializable {
    private String orderNo;

    public OrderInfo toOrderInfo(){
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderId(this.getOrderNo());
        return orderInfo;
    }
}
