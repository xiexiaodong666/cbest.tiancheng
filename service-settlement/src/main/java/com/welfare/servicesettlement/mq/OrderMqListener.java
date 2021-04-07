package com.welfare.servicesettlement.mq;

import com.alibaba.fastjson.JSON;
import com.welfare.persist.entity.OrderInfo;
import com.welfare.service.OrderService;
import com.welfare.servicesettlement.dto.mall.OrderMqInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;


/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 3/29/2021
 */
@Slf4j
@Component
@RequiredArgsConstructor
@RocketMQMessageListener(
        topic = "${rocketmq.topic.order-online}",
        consumerGroup = MqConstant.ConsumerGroup.ONLINE_ORDER_CONSUMER_GROUP
)
public class OrderMqListener implements RocketMQListener<OrderMqInfo> {
    private final OrderService orderService;
    @Override
    public void onMessage(OrderMqInfo orderDTO) {
        log.info("rocketmq msg received:{}", JSON.toJSONString(orderDTO));
        //OrderInfo orderInfo = orderDTO.parseToOrderInfo();
        //log.info("ready to save orderInfo:{}",JSON.toJSONString(orderInfo));
    }
}
