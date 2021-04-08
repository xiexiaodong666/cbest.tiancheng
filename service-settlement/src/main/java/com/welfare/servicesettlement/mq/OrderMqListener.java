package com.welfare.servicesettlement.mq;

import com.alibaba.fastjson.JSON;
import com.welfare.common.constants.WelfareConstant;
import com.welfare.common.exception.BizAssert;
import com.welfare.common.exception.ExceptionCode;
import com.welfare.persist.dao.AccountDao;
import com.welfare.persist.dao.AccountDeductionDetailDao;
import com.welfare.persist.dao.MerchantDao;
import com.welfare.persist.dao.OrderInfoDao;
import com.welfare.persist.entity.Account;
import com.welfare.persist.entity.AccountDeductionDetail;
import com.welfare.persist.entity.Merchant;
import com.welfare.persist.entity.OrderInfo;
import com.welfare.service.OrderService;
import com.welfare.servicesettlement.dto.mall.OrderMqInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import java.util.List;


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
    private final OrderInfoDao orderInfoDao;
    private final AccountDeductionDetailDao accountDeductionDetailDao;
    private final AccountDao accountDao;
    private final MerchantDao merchantDao;
    @Override
    public void onMessage(OrderMqInfo orderDTO) {
        log.info("rocketmq msg received:{}", JSON.toJSONString(orderDTO));
        List<AccountDeductionDetail> accountDeductionDetails = accountDeductionDetailDao
                .queryByTransNoAndTransType(orderDTO.getTradeNo(), WelfareConstant.TransType.CONSUME.code());
        BizAssert.notEmpty(accountDeductionDetails, ExceptionCode.DATA_NOT_EXIST,"不存在流水号:"+orderDTO.getTradeNo());

        AccountDeductionDetail firstDetail = accountDeductionDetails.get(0);
        Account account = accountDao.queryByAccountCode(firstDetail.getAccountCode());
        Merchant merchant = merchantDao.queryByCode(account.getMerCode());
        OrderInfo orderInfo = OrderMqInfo.parseToOrderInfo(orderDTO,firstDetail,account,merchant);
        orderInfoDao.save(orderInfo);
        log.info("ready to save orderInfo:{}",JSON.toJSONString(orderInfo));
    }
}
