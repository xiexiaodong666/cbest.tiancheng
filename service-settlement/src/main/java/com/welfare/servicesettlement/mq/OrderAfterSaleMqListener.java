package com.welfare.servicesettlement.mq;

import com.alibaba.fastjson.JSON;
import com.welfare.common.annotation.DistributedLock;
import com.welfare.common.exception.BizAssert;
import com.welfare.common.exception.ExceptionCode;
import com.welfare.persist.dao.OrderInfoDao;
import com.welfare.persist.entity.OrderInfo;
import com.welfare.servicesettlement.dto.mall.AftersaleOrderMqInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

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
public class OrderAfterSaleMqListener implements RocketMQListener<AftersaleOrderMqInfo> {
    private final OrderInfoDao orderInfoDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    @DistributedLock(lockPrefix = "order-save",lockKey = "#aftersaleOrderMqInfo.orgOrderNo")
    public void onMessage(AftersaleOrderMqInfo aftersaleOrderMqInfo) {
        log.info("return order rocketmq msg received:{}", JSON.toJSONString(aftersaleOrderMqInfo));
        String tradeNo = aftersaleOrderMqInfo.getTradeNo();
        String orderNo = aftersaleOrderMqInfo.getOrgOrderNo().toString();
        OrderInfo orderInfo = orderInfoDao.getOneByOrderNo(orderNo);
        BizAssert.notNull(orderInfo, ExceptionCode.DATA_NOT_EXIST,"正向订单不存在");
        orderInfo.setReturnTransNo(tradeNo);
        orderInfoDao.updateById(orderInfo);
    }
}
