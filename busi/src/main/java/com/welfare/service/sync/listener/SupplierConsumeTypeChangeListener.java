package com.welfare.service.sync.listener;

import com.alibaba.fastjson.JSON;
import com.welfare.common.enums.ShoppingActionTypeEnum;
import com.welfare.service.PaymentChannelConfigService;
import com.welfare.service.dto.paymentChannel.PayChannelConfigDelDTO;
import com.welfare.service.sync.event.SupplierConsumeTypeChangeEvt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;


/**
 * @author duanhy
 * @title: SupplierConsumeTypeDelListener
 * @description: TODO
 * @date 2021/3/2818:11
 */
@Component
@Slf4j
public class SupplierConsumeTypeChangeListener {

    @Autowired
    private PaymentChannelConfigService channelConfigService;

    @EventListener
    @Transactional(rollbackFor = Exception.class)
    public void onChange(SupplierConsumeTypeChangeEvt evt){
        Assert.hasText(evt.getStoreCode(), "门店编码不能为空");
        Assert.notEmpty(evt.getDelConsumeTypes(), "消费场景为空不能为空");
        Assert.notNull(evt.getActionType(), "变更类型不能为空");
        log.info("收到门店消费方式变更事件 storeCode:{} consumeType:{} actionType:{}",
                evt.getStoreCode(), JSON.toJSON(evt.getDelConsumeTypes()),  evt.getActionType().getDesc());
        if (evt.getActionType() == ShoppingActionTypeEnum.DELETE) {
            channelConfigService.batchDel(PayChannelConfigDelDTO.of(evt));
        }
        log.info("处理门店消费方式变更事件成功 storeCode:{} consumeType:{} actionType:{}",
                evt.getStoreCode(), JSON.toJSON(evt.getDelConsumeTypes()),  evt.getActionType().getDesc());
    }
}
