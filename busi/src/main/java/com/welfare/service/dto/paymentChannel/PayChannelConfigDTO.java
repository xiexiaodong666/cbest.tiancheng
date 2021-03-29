package com.welfare.service.dto.paymentChannel;

import com.welfare.common.constants.WelfareConstant;
import com.welfare.service.sync.event.MerStoreConsumeTypeChangeEvt;
import lombok.Data;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * @author duanhy
 * @title: PayChannelConfigDTO
 * @description: TODO
 * @date 2021/3/2817:45
 */
@Data
public class PayChannelConfigDTO {

    /**
     * 商户编码
     */
    private String merCode;

    /**
     * 门店编码
     */
    private String storeCode;

    /**
     * 消费方式
     */
    private String consumeType;

    /**
     * 支付渠道
     */
    private String paymentChannelCode;

    /**
     * 支付渠道名称
     */
    private String paymentChannelName;

    public static List<PayChannelConfigDTO> of(MerStoreConsumeTypeChangeEvt evt, WelfareConstant.PaymentChannel paymentChannel) {
        List<PayChannelConfigDTO> dtos = new ArrayList<>();
        Assert.notNull(evt, "evt不能为空");
        Assert.hasText(evt.getMerCode(), "商户编码不能为空");
        Assert.notEmpty(evt.getChangeStoreConsumes(), "商户的门店消费场景不能为空");
        Assert.notNull(paymentChannel, "支付渠道不能为空");
        evt.getChangeStoreConsumes().forEach(storeConsumeType -> {
            storeConsumeType.getConsumeType().forEach(consumeTypeEnum -> {
                PayChannelConfigDTO dto = new PayChannelConfigDTO();
                dto.setMerCode(evt.getMerCode());
                dto.setStoreCode(storeConsumeType.getStoreCode());
                dto.setConsumeType(consumeTypeEnum.getCode());
                dto.setPaymentChannelCode(paymentChannel.code());
                dto.setPaymentChannelName(paymentChannel.desc());
                dtos.add(dto);
            });
        });
        return dtos;
    }
}
