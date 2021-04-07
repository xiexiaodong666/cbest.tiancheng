package com.welfare.servicesettlement.mq;

import com.welfare.servicesettlement.dto.mall.PlatformAftersaleDetailInfo;
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
        topic = "${rocketmq.topic.order-online-after-sale}",
        consumerGroup = MqConstant.ConsumerGroup.ONLINE_ORDER_AFTER_SALE_CONSUMER_GROUP
)
public class OrderAfterSaleMqListener implements RocketMQListener<PlatformAftersaleDetailInfo> {
    @Override
    public void onMessage(PlatformAftersaleDetailInfo platformAftersaleDetailInfo) {

    }
}
