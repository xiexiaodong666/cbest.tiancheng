package com.welfare.service.sync.listener;

import com.alibaba.fastjson.JSON;
import com.welfare.common.constants.WelfareConstant;
import com.welfare.common.enums.ConsumeTypeEnum;
import com.welfare.common.enums.MerchantAccountTypeShowStatusEnum;
import com.welfare.common.enums.ShoppingActionTypeEnum;
import com.welfare.persist.entity.MerchantAccountType;
import com.welfare.service.MerchantAccountTypeService;
import com.welfare.service.PaymentChannelConfigService;
import com.welfare.service.dto.paymentChannel.PayChannelConfigDTO;
import com.welfare.service.dto.paymentChannel.PayChannelConfigDelDTO;
import com.welfare.service.sync.event.MerStoreConsumeTypeChangeEvt;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

/**
 * @author duanhy
 * @title: MerStoreConsumeTypeChangeListener
 * @description: TODO
 * @date 2021/3/2819:15
 */
@Component
@Slf4j
public class MerStoreConsumeTypeChangeListener {

    @Autowired
    private PaymentChannelConfigService channelConfigService;
    @Autowired
    private MerchantAccountTypeService merchantAccountTypeService;

    @EventListener
    @Transactional(rollbackFor = Exception.class)
    public void onChange(MerStoreConsumeTypeChangeEvt evt){
        Assert.hasText(evt.getMerCode(), "商户编码不能为空");
        Assert.notEmpty(evt.getChangeStoreConsumes(), "消费场景不能为空");
        Assert.notNull(evt.getActionType(), "变更类型不能为空");
        log.info("收到商户门店消费方式变更事件 merCode:{} changeStoreConsumes:{} actionType:{}",
                evt.getMerCode(), JSON.toJSON(evt.getChangeStoreConsumes()),  evt.getActionType().getDesc());
        if (evt.getActionType() == ShoppingActionTypeEnum.ADD) {
            // 在每个场景下配置甜橙卡的支付渠道
            channelConfigService.batchSave(PayChannelConfigDTO.of(evt, WelfareConstant.PaymentChannel.WELFARE));
        } else if (evt.getActionType() == ShoppingActionTypeEnum.DELETE) {
            channelConfigService.batchDel(PayChannelConfigDelDTO.of(evt));
        }
        log.info("处理商户门店消费方式变更事件成功 merCode:{} changeStoreConsumes:{} actionType:{}",
                evt.getMerCode(), JSON.toJSON(evt.getChangeStoreConsumes()),  evt.getActionType().getDesc());
    }
}
