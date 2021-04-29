package com.welfare.serviceaccount;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.welfare.service.sync.event.PayDeductionDetailEvt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.killbill.bus.api.PersistentBus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 4/29/2021
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class PayDeductionDetailEvtHandler {
    private final PersistentBus persistentBus;
    private final RocketMQTemplate rocketMQTemplate;
    @Value("${rocketmq.topic.account-deduction-detail:none}")
    private String topic;
    @PostConstruct
    private void register(){
        try {
            persistentBus.register(this);
        } catch (PersistentBus.EventBusException e) {
            log.error("error when register payDeductionDetailEvtHandler",e);
        }
    }

    @AllowConcurrentEvents
    @Subscribe
    public void payDeductionDetailEvt(PayDeductionDetailEvt payDeductionDetailEvt){
        Message<List<Long>> message = MessageBuilder.withPayload(payDeductionDetailEvt.getAccountDeductionDetailIds()).build();
        //messageDelayLevel=1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h , 其中3表示延时10s
        rocketMQTemplate.syncSend(topic,message,(long)rocketMQTemplate.getProducer().getSendMsgTimeout(),3);
    }

}
