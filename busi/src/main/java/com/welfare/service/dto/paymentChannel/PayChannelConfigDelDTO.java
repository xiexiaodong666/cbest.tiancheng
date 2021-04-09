package com.welfare.service.dto.paymentChannel;

import com.welfare.persist.entity.PaymentChannel;
import com.welfare.service.sync.event.MerStoreConsumeTypeChangeEvt;
import com.welfare.service.sync.event.SupplierConsumeTypeChangeEvt;
import lombok.Data;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author duanhy
 * @title: PayChannelConfigDelDTO
 * @description: TODO
 * @date 2021/3/2817:12
 */
@Data
public class PayChannelConfigDelDTO {

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
    private String paymentChannel;

    public static List<PayChannelConfigDelDTO> of(SupplierConsumeTypeChangeEvt evt) {
        Assert.notNull(evt, "SupplierConsumeTypeDelEvt不能为空");
        Assert.notEmpty(evt.getDelConsumeTypes(), "evt.delConsumeTypes不能为空");
        Assert.hasText(evt.getStoreCode(), "evt.storeCode不能为空");
        Assert.notNull(evt.getActionType(), "变更类型不能为空");
        List<PayChannelConfigDelDTO> delDTOS = new ArrayList<>();
        evt.getDelConsumeTypes().forEach(consumeTypeEnum -> {
            PayChannelConfigDelDTO delDTO = new PayChannelConfigDelDTO();
            delDTO.setStoreCode(evt.getStoreCode());
            delDTO.setConsumeType(consumeTypeEnum.getCode());
            delDTOS.add(delDTO);
        });
        return delDTOS;
    }

    public static List<PayChannelConfigDelDTO> of(MerStoreConsumeTypeChangeEvt evt) {
        Assert.notNull(evt, "MerStoreConsumeTypeChangeEvt不能为空");
        Assert.notEmpty(evt.getChangeStoreConsumes(), "商户门店消费方式变更不能为空");
        Assert.hasText(evt.getMerCode(), "商户编码不能为空");
        Assert.notNull(evt.getActionType(), "变更类型不能为空");
        List<PayChannelConfigDelDTO> delDTOS = new ArrayList<>();
        evt.getChangeStoreConsumes().forEach(storeConsumeType -> {
            if (!CollectionUtils.isEmpty(storeConsumeType.getConsumeType())) {
                storeConsumeType.getConsumeType().forEach(consumeTypeEnum -> {
                    PayChannelConfigDelDTO dto = new PayChannelConfigDelDTO();
                    dto.setMerCode(evt.getMerCode());
                    dto.setStoreCode(storeConsumeType.getStoreCode());
                    dto.setConsumeType(consumeTypeEnum.getCode());
                    delDTOS.add(dto);
                });
            } else {
                PayChannelConfigDelDTO dto = new PayChannelConfigDelDTO();
                dto.setMerCode(evt.getMerCode());
                dto.setStoreCode(storeConsumeType.getStoreCode());
                delDTOS.add(dto);
            }
        });
        return delDTOS;
    }

    public static List<PayChannelConfigDelDTO> of(String merCode, List<PaymentChannel> delPaymentChannels) {
        Assert.hasText(merCode, "商户编码不能为空");
        Assert.notEmpty(delPaymentChannels, "delPaymentChannels不能为空");
        List<PayChannelConfigDelDTO> delDTOS = new ArrayList<>();
        delPaymentChannels.forEach(paymentChannel -> {
            PayChannelConfigDelDTO delDTO = new PayChannelConfigDelDTO();
            delDTO.setMerCode(merCode);
            delDTO.setPaymentChannel(paymentChannel.getCode());
            delDTOS.add(delDTO);
        });
        return delDTOS;
    }

}
