package com.welfare.servicesettlement.mq;

import com.welfare.common.util.DistributedLockUtil;
import com.welfare.service.settlement.SettleDetailGenerateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 4/29/2021
 */
@Slf4j
@Component
@RequiredArgsConstructor
@RocketMQMessageListener(
        topic = "${rocketmq.topic.account-deduction-detail}",
        consumerGroup = MqConstant.ConsumerGroup.E_WELFARE_SETTLEMENT_GROUP
)
public class PayDeductionDetailListener implements RocketMQListener<List<Long>> {
    private final RedissonClient redissonClient;
    private static final String LOCK_PREFIX = "PayDeductionDetailEvt";
    private final SettleDetailGenerateService settleDetailGenerateService;

    @Override
    public void onMessage(List<Long> accountDeductionDetailId) {
        List<RLock> locks = new ArrayList<>();
        RLock multiLock;
        accountDeductionDetailId.forEach(id ->{
            RLock lock = redissonClient.getFairLock(LOCK_PREFIX + id);
            locks.add(lock);
        });
        multiLock = redissonClient.getMultiLock(locks.toArray(new RLock[]{}));
        try{
            multiLock.lock(-1, TimeUnit.SECONDS);
            settleDetailGenerateService.generateWholesaleDetails(accountDeductionDetailId);
        }finally {
            DistributedLockUtil.unlock(multiLock);
        }
    }
}
