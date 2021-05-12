package com.welfare.servicesettlement.mq;

import com.alibaba.fastjson.JSON;
import com.welfare.common.annotation.DistributedLock;
import com.welfare.common.constants.WelfareConstant;
import com.welfare.common.exception.BizAssert;
import com.welfare.common.exception.ExceptionCode;
import com.welfare.persist.dao.OrderInfoDao;
import com.welfare.persist.dao.OrderInfoDetailDao;
import com.welfare.persist.entity.OrderInfo;
import com.welfare.persist.entity.OrderInfoDetail;
import com.welfare.servicesettlement.dto.mall.AftersaleOrderMqInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
    private final OrderInfoDetailDao orderInfoDetailDao;
    /**
     * 此种类型的pay_type需要忽略（表示老的员工卡支付的）
     */
    private static final  Integer IGNORE_PAY_TYPE = 6;
    @Override
    @Transactional(rollbackFor = Exception.class)
    @DistributedLock(lockPrefix = "order-save",lockKey = "#aftersaleOrderMqInfo.orgOrderNo")
    public void onMessage(AftersaleOrderMqInfo aftersaleOrderMqInfo) {
        log.info("return order rocketmq msg received:{}", JSON.toJSONString(aftersaleOrderMqInfo));
        String tradeNo = aftersaleOrderMqInfo.getTradeNo();
        if(Strings.isEmpty(tradeNo) ||  IGNORE_PAY_TYPE.equals(aftersaleOrderMqInfo.getPayType())){
            log.info("此逆向订单不需要保存");
            //没有交易单号，则没有支付过，不保存。老的员工卡也不保存
            return;
        }
        OrderInfo refundOrderInDb = orderInfoDao.getOneByTradeNo(tradeNo, WelfareConstant.TransType.REFUND.code());
        if(Objects.nonNull(refundOrderInDb)){
            log.info("此流水号对应的订单已经保存，不需要再次保存");
            return;
        }
        String orderNo = aftersaleOrderMqInfo.getOrgOrderNo().toString();
        OrderInfo originalOrder = orderInfoDao.getOneByOrderNo(orderNo, WelfareConstant.TransType.CONSUME.code());
        BizAssert.notNull(originalOrder, ExceptionCode.DATA_NOT_EXIST,"正向订单不存在");
        OrderInfo orderInfo = aftersaleOrderMqInfo.parseFromOriginalOrder(originalOrder);
        orderInfo.setOrderWholesaleAmount(aftersaleOrderMqInfo.getOrderWholesaleAmount());
        List<OrderInfoDetail> orderInfoDetails = aftersaleOrderMqInfo.parseOrderInfoDetails();
        orderInfoDetailDao.saveBatch(orderInfoDetails);
        orderInfoDao.save(orderInfo);
    }
}
